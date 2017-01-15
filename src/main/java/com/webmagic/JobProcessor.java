package com.webmagic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.model.Job;
import com.services.JobUtil;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

public class JobProcessor implements PageProcessor {
	public static final SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
	private Site site = Site.me().setDomain("51job.com").setSleepTime(100);

	public static final String START_URL = "http://search.51job.com/jobsearch/search_result.php?"
			+ "fromJs=1&jobarea=040000&issuedate=9&providesalary=99&keyword=%s"
			+ "&keywordtype=2&lang=c&stype=2&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&fromType=1";
	public static final String NEXT_URL = "http://search.51job.com/jobsearch/search_result.php?"
			+ "fromJs=1&jobarea=040000,00&district=000000&funtype=0000&industrytype=00&issuedate=9&"
			+ "providesalary=99&keyword=%s&keywordtype=2&curr_page=%d&lang=c&stype=1&postchannel=0000&"
			+ "workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&lonlat=0,0&radius=-1&"
			+ "ord_field=0&list_type=0&fromType=14&dibiaoid=0&confirmdate=9";

	public static String keyword = "";

	@Override
	public void process(Page page) {
		if (page.getRequest().getUrl().startsWith("http://search.51job.com")) {
			parseList(page);
		} else if (page.getRequest().getUrl().startsWith("http://jobs.51job.com")) {
			parseDetail(page);
		}
	}

	public void parseDetail(Page page) {
		String url = page.getRequest().getUrl();
		String ident = url.split("/")[4].split("\\.")[0];
		if (!ident.matches("\\d+")) {
			return;
		}
		if (page.getHtml().toString().contains("很抱歉，你选择的职位目前已经暂停招聘")) {
			JobUtil.updateStatus(Long.valueOf(ident), -1);
			return;
		}
		String ltype = page.getHtml().xpath("//p[@class='msg ltype']/text()").toString();
		String[] typearr = ltype.split("\\|");
		String company_tags = "";
		String company_size = "";
		String company_type = "";
		if (typearr.length == 3) {
			company_type = typearr[0].replaceAll("&nbsp;", "").trim();
			company_size = typearr[1].replaceAll("&nbsp;", "").trim();
			company_tags = typearr[2].replaceAll("&nbsp;", "").trim();
		} else if (typearr.length == 2) {
			company_size = typearr[0].replaceAll("&nbsp;", "").trim();
			company_tags = typearr[1].replaceAll("&nbsp;", "").trim();
		}
		String bmsg = page.getHtml().xpath("//div[@class='bmsg inbox']/p[@class='fp']/text()").toString();
		String f_address = "";
		if (bmsg != null && !"null".equals(bmsg) && bmsg.length() > 0) {
			f_address = bmsg.replace("地图", "").trim().replace("上班地址：", "");
		}
		String detail = page.getHtml().xpath("//div[@class='bmsg job_msg inbox']/text()").toString();
		JobUtil.updateJob(f_address, company_tags, company_size, company_type, detail, Long.valueOf(ident));
	}

	public void parseList(Page page) {
		List<Selectable> divs = page.getHtml().xpath("//div[@id='resultList']/div[@class='el']").nodes();
		List<String> links = new ArrayList<String>();
		for (Selectable s : divs) {
			Selectable first = s.xpath("//p[@class='t1']");
			if (first.get() == null || "null".equals(first.get().toString())) {
				continue;
			}
			String title = first.xpath("//a/@title").toString();
			String url = first.xpath("//a/@href").toString();
			String ident = url.split("/")[4].split("\\.")[0];
			if (!ident.matches("\\d+")) {
				continue;
			}
			String company = s.xpath("//span[@class='t2']/a/@title").toString();
			String s_address = s.xpath("//span[@class='t3']/text()").toString();
			String salary = s.xpath("//span[@class='t4']/text()").toString();
			String date = s.xpath("//span[@class='t5']/text()").toString();
			Job job = new Job();
			job.setCompany(company);
			job.setUrl(url);
			job.setName(title);
			job.setIdent(Long.valueOf(ident));
			job.setS_address(s_address);
			job.setSalary(salary);
			job.setStatus(0);
			try {
				job.setDate(date_format.parse("2016-" + date));
			} catch (ParseException e) {
				try {
					job.setDate(date_format.parse("1900-01-01"));
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
			JobUtil.saveJob(job);
			links.add(url);
		}
		page.addTargetRequests(links);
		String current_page = page.getHtml().xpath("//li[@class='on']/text()").toString();
		int cpage = Integer.valueOf(current_page);
		if (cpage < 50) {
			cpage++;
			String nextUrl = String.format(NEXT_URL, keyword, cpage);
			System.out.println("add next url:" + nextUrl);
			page.addTargetRequest(nextUrl);
		}
	}

	@Override
	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		keyword = "java";
		Spider.create(new JobProcessor()).addUrl(String.format(START_URL, keyword)).run();
	}
}
