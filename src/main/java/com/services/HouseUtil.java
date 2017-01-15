package com.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.echarts.BaiduUtil;
import com.echarts.MarkPointData;
import com.google.gson.Gson;
import com.model.House;
import com.util.JdbcDao;
//mvn exec:java -Dexec.mainClass="com.services.HouseUtil"
public class HouseUtil {
	public static void main(String []args){
		List<House> g_list=getAnyHouses(0);
		fillLatAndLng(g_list);
	}
	public static String getJson(){
		Gson gson = new Gson();
        java.util.Map<String,Object> result=new java.util.HashMap<String,Object>();
        List<House> g_list=getAnyHouses(1);
        result.put("g_list",g_list);
        result.put("p_list",fillPoint(g_list));
        String str = gson.toJson(result);
        return str;
	}
    public static List<MarkPointData> fillPoint(List<House> g_list){
        List<MarkPointData> p_list=new ArrayList<MarkPointData>();
        for(House h:g_list){
            MarkPointData point1=new MarkPointData();
            point1.name=h.getAddress();
            point1.value=h.getPrice()/100;
            p_list.add(point1);
        }
        return p_list;
    }
	public static void fillLatAndLng(List<House> datas){
		for(House h:datas){
			if(h.getLat()!=null&&StringUtils.isNotBlank(h.getLat())&&h.getLng()!=null&&StringUtils.isNotBlank(h.getLng())){
				continue;
			}
			BaiduUtil.fillLatAndLng(h);
			System.out.println("address:"+h.getAddress()+"\tlat:"+h.getLat()+"\tlng:"+h.getLng());
			if(h.getLat()!=null&&StringUtils.isNotBlank(h.getLat())&&h.getLng()!=null&&StringUtils.isNotBlank(h.getLng())){
				updateHouseLatAndLng(h);
			}
		}
	}
	/**
	 * type -1 获取所有 0 获取没有经纬度的 1获取有经纬度的
	 */
	public static List<House> getAnyHouses(int type){
		JdbcDao dao=new JdbcDao();
		List<House> datas=new ArrayList<House>();
		String sql="";
		if(type==-1){
			sql="select * from house1";
		}
		else if(type==0){
			sql="select * from house1 where `lat` is null and `lng` is null";
		}
		else{
			sql="select * from house1 where `lat` is not null and `lng` is not null";
		}
		dao.doStatement(sql, null);
		ResultSet result=dao.getResultSet();
		if(result!=null){
			try {
				while(result.next()){
					House data=new House();
					int id=result.getInt("id");
					String scope=result.getString("scope");
					String address=result.getString("address");
					int price=result.getInt("price");
					data.setId(id);
					data.setScope(scope);
					data.setAddress(address);
					data.setLat(result.getString("lat"));
					data.setLng(result.getString("lng"));
					data.setPrice(price);
					datas.add(data);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		dao.closed();
		return datas;		
	}
	public static List<House> getAllHouses(){
		JdbcDao dao=new JdbcDao();
		List<House> datas=new ArrayList<House>();
		String sql="select * from house1";
		dao.doStatement(sql, null);
		ResultSet result=dao.getResultSet();
		if(result!=null){
			try {
				while(result.next()){
					House data=new House();
					int id=result.getInt("id");
					String scope=result.getString("scope");
					String address=result.getString("address");
					data.setId(id);
					data.setScope(scope);
					data.setAddress(address);
					datas.add(data);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		dao.closed();
		return datas;
	}
	public static boolean updateHouseLatAndLng(House house){
		String sql="update house1 set `lat`=?,`lng`=? where `id`=?";
		Object []params=new Object[]{house.getLat(),house.getLng(),house.getId()};
		JdbcDao dao = new JdbcDao();
		int i = dao.update(sql, params);
		dao.closed();
		return i==1;
	}
	public static void saveHouse(Map<String, Object> result, String url) {
		String sql = "insert ignore into house1(`name`,`scope`,`address`,`p_time`,`price`,`p_type`,`url`,`url_hash`) values(?,?,?,?,?,?,?,?)";
		Object params[] = new Object[] { result.get("title") == null ? "" : result.get("title"),
				result.get("scope") == null ? "" : result.get("scope"),
				result.get("address") == null ? "" : result.get("address"),
				result.get("p_time") == null ? "" : result.get("p_time"), result.get("price"), result.get("type"), url,
				url.hashCode() };
		JdbcDao dao = new JdbcDao();
		int i = dao.update(sql, params);
		if (i == 1) {
			System.out.println("保存成功:" + result.get("title") + "|" + result.get("scope") + "|" + result.get("address")
					+ "|" + result.get("p_time") + "|" + result.get("price") + "|" + result.get("type"));
		} else {
			System.out.println("保存失败:" + result.get("title") + "|" + result.get("scope") + "|" + result.get("address")
					+ "|" + result.get("p_time") + "|" + result.get("price") + "|" + result.get("type"));
		}
		dao.closed();
	}
}
