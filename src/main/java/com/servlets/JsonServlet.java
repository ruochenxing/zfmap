package com.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.services.HouseUtil;
import com.services.JobUtil;

public class JsonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		process(req, resp, "GET");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		process(req, resp, "POST");
	}

	protected void process(HttpServletRequest req, HttpServletResponse resp, String req_method) throws IOException {
		resp.setCharacterEncoding("UTF-8");
		// resp.setContentType("application/json; charset=utf-8");
		PrintWriter out = resp.getWriter();
		String type = req.getParameter("type");
		if ("job".equals(type)) {
			out.print(JobUtil.getJson());
		} else {
			out.print(HouseUtil.getJson());
		}
	}
}