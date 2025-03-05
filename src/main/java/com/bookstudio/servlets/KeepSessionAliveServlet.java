package com.bookstudio.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/KeepSessionAliveServlet")
public class KeepSessionAliveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final int SESSION_TIMEOUT_MINUTES = 15;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().setMaxInactiveInterval(SESSION_TIMEOUT_MINUTES * 60);
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
