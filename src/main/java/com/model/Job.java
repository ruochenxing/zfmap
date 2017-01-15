package com.model;

import java.util.Date;

public class Job {

	private long id;
	private long ident;
	private String name;
	private String s_address;
	private Date date;
	private String salary;
	private String full_address;
	private String company;
	private String company_tags;
	private String company_size;
	private String company_type;
	private String detail;
	private String url;
	private int status;// -1:失效，0:待爬，1:已爬取
	private String lat;
	private String lng;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdent() {
		return ident;
	}

	public void setIdent(long ident) {
		this.ident = ident;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getS_address() {
		return s_address;
	}

	public void setS_address(String s_address) {
		this.s_address = s_address;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getFull_address() {
		return full_address;
	}

	public void setFull_address(String full_address) {
		this.full_address = full_address;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompany_tags() {
		return company_tags;
	}

	public void setCompany_tags(String company_tags) {
		this.company_tags = company_tags;
	}

	public String getCompany_size() {
		return company_size;
	}

	public void setCompany_size(String company_size) {
		this.company_size = company_size;
	}

	public String getCompany_type() {
		return company_type;
	}

	public void setCompany_type(String company_type) {
		this.company_type = company_type;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	@Override
	public String toString() {
		return "Job [id=" + id + ", ident=" + ident + ", name=" + name + ", s_address=" + s_address + ", date=" + date
				+ ", salary=" + salary + ", full_address=" + full_address + ", company=" + company + ", company_tags="
				+ company_tags + ", company_size=" + company_size + ", company_type=" + company_type + ", detail="
				+ detail + ", url=" + url + ", status=" + status + ", lat=" + lat + ", lng=" + lng + "]";
	}
}
