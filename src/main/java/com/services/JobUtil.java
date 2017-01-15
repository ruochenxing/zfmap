package com.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.echarts.BaiduUtil;
import com.echarts.MarkPointData;
import com.google.gson.Gson;
import com.model.Job;
import com.util.JdbcDao;

//mvn exec:java -Dexec.mainClass="com.services.JobUtil"
public class JobUtil {
	public static final SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");

	public static void main(String[] args) {
		List<Job> g_list = getAnyJobs(0, Integer.MAX_VALUE);
		fillLatAndLng(g_list);
	}

	public static String getJson() {
		Gson gson = new Gson();
		java.util.Map<String, Object> result = new java.util.HashMap<String, Object>();
		List<Job> g_list = getAnyJobs(1, 1000);
		result.put("g_list", g_list);
		result.put("p_list", fillPoint(g_list));
		String str = gson.toJson(result);
		return str;
	}

	public static List<MarkPointData> fillPoint(List<Job> g_list) {
		List<MarkPointData> p_list = new ArrayList<MarkPointData>();
		for (Job job : g_list) {
			MarkPointData point1 = new MarkPointData();
			point1.name = job.getName();
			point1.date = date_format.format(job.getDate());
			point1.value = getSalary(job.getSalary());
			point1.company = job.getCompany();
			point1.address = getAddress(job);// 用于地图显示
			point1.title = job.getName();
			point1.salary = job.getSalary();
			point1.url = job.getUrl();
			p_list.add(point1);
		}
		return p_list;
	}

	public static int getSalary(String salary) {
		if (StringUtils.isBlank(salary)) {
			return 1;
		} else {
			Pattern p = Pattern.compile("(\\d+)");
			Matcher m = p.matcher(salary);
			while (m.find()) {
				String find = m.group(1).toString();
				int realSalary = 1;
				if (salary.contains("/年")) {
					if (salary.contains("万") || find.length() <= 2) {
						realSalary = Integer.valueOf(find) * 10000 / 12;
					} else {
						realSalary = Integer.valueOf(find) / 12;
					}
				} else {
					if (salary.contains("万") || find.length() <= 2) {
						realSalary = Integer.valueOf(find) * 1000;
					} else {
						realSalary = Integer.valueOf(find);
					}
				}
				return realSalary / 1000;
			}
			return 1;
		}
	}

	public static String getAddress(Job job) {
		if (job == null) {
			return "";
		} else if (StringUtils.isNotBlank(job.getFull_address())) {
			return job.getFull_address();
		} else if (StringUtils.isNotBlank(job.getS_address())) {
			return job.getS_address();
		}
		return "";
	}

	/**
	 * type -1 获取所有 0 获取没有经纬度的 1获取有经纬度的
	 */
	public static List<Job> getAnyJobs(int type, int limit) {
		JdbcDao dao = new JdbcDao();
		List<Job> datas = new ArrayList<Job>();
		String sql = "";
		if (type == -1) {
			sql = "select * from jobinfo ORDER BY `date` DESC limit ?";
		} else if (type == 0) {
			sql = "select * from jobinfo where `lat` is null and `lng` is null ORDER BY `date` DESC limit ?";
		} else {
			sql = "select * from jobinfo where `lat` is not null and `lng` is not null ORDER BY `date` DESC limit ?";
		}
		dao.doStatement(sql, new Object[] { limit });
		ResultSet result = dao.getResultSet();
		if (result != null) {
			try {
				while (result.next()) {
					Job data = new Job();
					long id = result.getLong("id");
					String s_address = result.getString("s_address");
					String full_address = result.getString("full_address");
					String salary = result.getString("salary");
					data.setId(id);
					data.setSalary(salary);
					data.setS_address(s_address);
					data.setFull_address(full_address);
					data.setCompany(result.getString("company"));
					data.setName(result.getString("name"));
					data.setLat(result.getString("lat"));
					data.setLng(result.getString("lng"));
					data.setUrl(result.getString("url"));
					data.setDate(result.getDate("date"));
					datas.add(data);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		dao.closed();
		return datas;
	}

	public static void fillLatAndLng(List<Job> datas) {
		for (Job j : datas) {
			if (j.getLat() != null && StringUtils.isNotBlank(j.getLat()) && j.getLng() != null
					&& StringUtils.isNotBlank(j.getLng())) {
				continue;
			}
			String address = j.getFull_address();
			if (address == null || StringUtils.isBlank(address)) {
				continue;
			}
			if (address.length() < 3) {
				if (!address.contains("深圳")) {
					address = j.getS_address() + address;
				}
			}
			if (!address.contains("深圳")) {
				address = "深圳" + address;
			}
			j.setFull_address(address);
			BaiduUtil.fillLatAndLng(j);
			System.out.println("address:" + j.getFull_address() + "\tlat:" + j.getLat() + "\tlng:" + j.getLng());
			if (j.getLat() != null && StringUtils.isNotBlank(j.getLat()) && j.getLng() != null
					&& StringUtils.isNotBlank(j.getLng())) {
				updateJobLatAndLng(j);
			}
		}
	}

	public static boolean updateJobLatAndLng(Job job) {
		String sql = "update jobinfo set `lat`=?,`lng`=? where `id`=?";
		Object[] params = new Object[] { job.getLat(), job.getLng(), job.getId() };
		JdbcDao dao = new JdbcDao();
		int i = dao.update(sql, params);
		dao.closed();
		return i == 1;
	}

	public static void updateStatus(long ident, int status) {
		String sql = "update jobinfo set status=? where ident=?";
		Object params[] = new Object[] { status, ident };
		JdbcDao dao = new JdbcDao();
		int i = dao.update(sql, params);
		if (i == 1) {
			System.out.println("更新成功:" + ident);
		} else {
			System.out.println("更新失败:" + ident);
		}
	}

	public static void updateJob(String full_address, String company_tags, String company_size, String company_type,
			String detail, long ident) {
		String sql = "update jobinfo set full_address=?,company_tags=?,company_size=?,company_type=?,detail=?,status=1 where ident=?";
		Object[] params = new Object[] { full_address, company_tags, company_size, company_type, detail, ident };
		JdbcDao dao = new JdbcDao();
		int i = dao.update(sql, params);
		if (i == 1) {
			System.out.println("更新成功:" + ident);
		} else {
			System.out.println("更新失败:" + ident);
		}
		dao.closed();
	}

	public static void saveJob(Job job) {
		String sql = "insert ignore into jobinfo(`ident`, `name`,`s_address`,`salary`,`date`,`company`,`url`,`status`) values(?,?,?,?,?,?,?,0)";
		Object params[] = new Object[] { job.getIdent(), job.getName(), job.getS_address(), job.getSalary(),
				job.getDate(), job.getCompany(), job.getUrl() };
		JdbcDao dao = new JdbcDao();
		int i = dao.update(sql, params);
		if (i == 1) {
			System.out.println("保存成功:" + job.getUrl());
		} else {
			System.out.println("保存失败:" + job.getUrl());
		}
		dao.closed();
	}
}
