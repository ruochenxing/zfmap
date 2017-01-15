package com.echarts;

import org.apache.commons.lang3.StringUtils;

import com.model.House;
import com.model.Job;

//mvn exec:java -Dexec.mainClass="com.echarts.BaiduUtil"
public class BaiduUtil {

	// -------------------------》关键代码根据地址获得坐标《--------------------------------
	public static void getPoint(AddressInfo shop) {
		try {
			java.io.InputStream l_urlStream;
			java.net.URL l_url = new java.net.URL(
					"http://api.map.baidu.com/geocoder/v2/?address=" + shop.getAddress().replaceAll(" ", "")
							+ "&output=json&ak=702632E1add3d4953d0f105f27c294b9&callback=showLocation");
			java.net.HttpURLConnection l_connection = (java.net.HttpURLConnection) l_url.openConnection();
			l_connection.connect();
			l_urlStream = l_connection.getInputStream();
			java.io.BufferedReader l_reader = new java.io.BufferedReader(new java.io.InputStreamReader(l_urlStream));
			String str = l_reader.readLine();
			// 用经度分割返回的网页代码
			String s = "," + "\"" + "lat" + "\"" + ":";
			String strs[] = str.split(s, 2);
			String s1 = "\"" + "lng" + "\"" + ":";
			String a[] = strs[0].split(s1, 2);
			shop.setLng(a[1]);
			s1 = "}" + "," + "\"";
			String a1[] = strs[1].split(s1, 2);
			shop.setLat(a1[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void fillLatAndLng(House house) {
		try {
			java.io.InputStream l_urlStream;
			String address = house.getAddress();
			if (StringUtils.isBlank(address)) {
				address = house.getScope().split(" ")[0].replaceAll("-", "");
			}
			if (StringUtils.isBlank(address)) {
				return;
			}
			java.net.URL l_url = new java.net.URL(
					"http://api.map.baidu.com/geocoder/v2/?address=" + address.replaceAll(" ", "")
							+ "&output=json&ak=702632E1add3d4953d0f105f27c294b9&callback=showLocation");
			java.net.HttpURLConnection l_connection = (java.net.HttpURLConnection) l_url.openConnection();
			l_connection.connect();
			l_urlStream = l_connection.getInputStream();
			java.io.BufferedReader l_reader = new java.io.BufferedReader(new java.io.InputStreamReader(l_urlStream));
			String str = l_reader.readLine();
			if (str.contains("lat") && str.contains("lng")) {
				// 用经度分割返回的网页代码
				String s = "," + "\"" + "lat" + "\"" + ":";
				String strs[] = str.split(s, 2);
				String s1 = "\"" + "lng" + "\"" + ":";
				String a[] = strs[0].split(s1, 2);
				if (a.length == 2) {
					house.setLng(a[1]);
					s1 = "}" + "," + "\"";
					String a1[] = strs[1].split(s1, 2);
					house.setLat(a1[0]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void fillLatAndLng(Job job) {
		try {
			java.io.InputStream l_urlStream;
			String address = job.getFull_address();
			if (StringUtils.isBlank(address)) {
				address = job.getS_address().replaceAll("-", "");
			}
			if (StringUtils.isBlank(address)) {
				return;
			}
			java.net.URL l_url = new java.net.URL(
					"http://api.map.baidu.com/geocoder/v2/?address=" + address.replaceAll(" ", "")
							+ "&output=json&ak=702632E1add3d4953d0f105f27c294b9&callback=showLocation");
			java.net.HttpURLConnection l_connection = (java.net.HttpURLConnection) l_url.openConnection();
			l_connection.connect();
			l_urlStream = l_connection.getInputStream();
			java.io.BufferedReader l_reader = new java.io.BufferedReader(new java.io.InputStreamReader(l_urlStream));
			String str = l_reader.readLine();
			if (str != null && str.contains("lat") && str.contains("lng")) {
				// 用经度分割返回的网页代码
				String s = "," + "\"" + "lat" + "\"" + ":";
				String strs[] = str.split(s, 2);
				String s1 = "\"" + "lng" + "\"" + ":";
				String a[] = strs[0].split(s1, 2);
				if (a.length == 2) {
					job.setLng(a[1]);
					s1 = "}" + "," + "\"";
					String a1[] = strs[1].split(s1, 2);
					job.setLat(a1[0]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static AddressInfo getAddressInfo(String name) {
		AddressInfo address = new AddressInfo(name);
		try {
			java.io.InputStream l_urlStream;
			java.net.URL l_url = new java.net.URL(
					"http://api.map.baidu.com/geocoder/v2/?address=" + address.getAddress().replaceAll(" ", "")
							+ "&output=json&ak=702632E1add3d4953d0f105f27c294b9&callback=showLocation");
			java.net.HttpURLConnection l_connection = (java.net.HttpURLConnection) l_url.openConnection();
			l_connection.connect();
			l_urlStream = l_connection.getInputStream();
			java.io.BufferedReader l_reader = new java.io.BufferedReader(new java.io.InputStreamReader(l_urlStream));
			String str = l_reader.readLine();
			// 用经度分割返回的网页代码
			String s = "," + "\"" + "lat" + "\"" + ":";
			String strs[] = str.split(s, 2);
			String s1 = "\"" + "lng" + "\"" + ":";
			String a[] = strs[0].split(s1, 2);
			address.setLng(a[1]);
			s1 = "}" + "," + "\"";
			String a1[] = strs[1].split(s1, 2);
			address.setLat(a1[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return address;
	}
}