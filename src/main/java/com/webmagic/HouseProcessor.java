package com.webmagic;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.JMException;

import org.apache.commons.lang3.StringUtils;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.processor.PageProcessor;

@HelpUrl("http://sz.58.com/zufang/0/pn\\d{1}")
public class HouseProcessor implements PageProcessor {
	private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
	Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
	@SuppressWarnings("deprecation")
	private Site site = Site.me().setDomain("58.com").addStartUrl("http://sz.58.com/zufang/0/pn1");

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
		List<String> links = page.getHtml().xpath("//a[@class=\"t\"]/@href").all();
		page.addTargetRequests(links);
		List<String> helpLinks = page.getHtml().xpath("//div[@class=\"pager\"]/a/@href").all();
		page.addTargetRequests(helpLinks);
		page.putField("p_time", page.getHtml().xpath("//li[@class=\"time\"]/text()").toString());
		page.putField("title", page.getHtml().xpath("//div[@class='bigtitle']/h1/text()").toString());
		String price = page.getHtml().xpath("//span[@class=\"bigpri arial\"]/text()").toString();
		int _price = 0;
		if (StringUtils.isNotBlank(price) && price.trim().matches("\\d+")) {
			_price = Integer.valueOf(price);
		}
		page.putField("price", _price);
		page.putField("type",
				page.getHtml().xpath("//ul[@class=\"suUl\"]/li[2]/div[@class=\"su_con\"]/text()").toString());
		String scope = page.getHtml()
				.xpath("//ul[@class=\"suUl\"]/li[4]/div[@class=\"su_con w382\" or @class=\"su_con w445\"]/html()")
				.toString();
		if (StringUtils.isNotBlank(scope) && !scope.equals("null")) {
			Matcher m_html = p_html.matcher(scope);
			scope = m_html.replaceAll("").replaceAll("\\s+|&nbsp;", ""); // 过滤html标签
			scope = scope.replaceAll("找附近工作", "").replaceAll("看中了，一键搬家", "");
		}
		page.putField("scope", scope);
		// TODO
		String address = page.getHtml()
				.xpath("//ul[@class=\"suUl\"]/li[5]/div[@class=\"su_con w382\" or @class=\"su_con w445\"]/text()")
				.toString();
		if (StringUtils.isNotBlank(address)) {
			address = address.trim();
		}
		page.putField("address", address);
	}

	public static void main(String[] args) throws JMException {
		Spider spider = Spider.create(new HouseProcessor()).addPipeline(new DBPipeline());
		spider.thread(10).run();
	}
}
