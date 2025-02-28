package utils;

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
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		String contextPath = httpRequest.getContextPath();
		String requestURI = httpRequest.getRequestURI();
		String relativePath = requestURI.substring(contextPath.length());
		
		if (relativePath.equals("/login.jsp") || 
		    relativePath.equals("/LoginServlet") || 
		    relativePath.contains("/css/") || 
		    relativePath.contains("/js/") ||
		    relativePath.contains("/utils/") ||
		    relativePath.contains("/images/")) {
			chain.doFilter(request, response);
			return;
		}
		
		HttpSession session = httpRequest.getSession(false);
		if (session == null || session.getAttribute("user") == null) {
			httpResponse.sendRedirect(contextPath + "/login.jsp");
		} else {
			chain.doFilter(request, response);
		}
	}
	
	@Override
	public void destroy() {
	}
}