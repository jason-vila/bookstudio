package com.bookstudio.utils;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*")
public class SessionFilter implements Filter {
	private static final String LOGIN_PAGE = "/login.jsp";
	private static final String LOGIN_SERVLET = "/LoginServlet";
	private static final String DASHBOARD_PAGE = "/dashboard.jsp";
	private static final String STATIC_RESOURCES = "/css/";
	private static final String JS_RESOURCES = "/js/";
	private static final String IMAGES_RESOURCES = "/images/";
	private static final String UTILS_RESOURCES = "/utils/";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String type = httpRequest.getParameter("type");
		if ("logout".equals(type)) {
			chain.doFilter(request, response);
			return;
		}

		String contextPath = httpRequest.getContextPath();
		String requestURI = httpRequest.getRequestURI();
		String relativePath = requestURI.substring(contextPath.length());

		HttpSession session = httpRequest.getSession(false);

		if (session != null && session.getAttribute("user") != null) {
			if (relativePath.equals(LOGIN_PAGE) || relativePath.equals(LOGIN_SERVLET)) {
				httpResponse.sendRedirect(contextPath + DASHBOARD_PAGE);
				return;
			}
		}

		if (session == null || session.getAttribute("user") == null) {
			if (relativePath.equals(LOGIN_PAGE) || relativePath.equals(LOGIN_SERVLET)) {
				chain.doFilter(request, response);
				return;
			}
			if (!(relativePath.contains(STATIC_RESOURCES) || relativePath.contains(JS_RESOURCES)
					|| relativePath.contains(UTILS_RESOURCES) || relativePath.contains(IMAGES_RESOURCES))) {
				httpResponse.sendRedirect(contextPath + LOGIN_PAGE);
				return;
			}
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}
}
