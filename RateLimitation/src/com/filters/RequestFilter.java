package com.filters;

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

import com.constants.Constants;
import com.validators.UserRequestLimitValidation;

@WebFilter("/*")
public class RequestFilter implements Filter {

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		System.out.println("Inside request filter");		
		String Ip = request.getRemoteAddr();
		String uri = request.getRequestURI();
		boolean isValidReq = UserRequestLimitValidation.reqLimitValidation(Ip, uri);
		if(isValidReq) {
			chain.doFilter(request, response);
		}
		else {
			try {
				HttpSession session = request.getSession();
				session.setAttribute("message", "you have reached throttling limit, kindly request after some time.. :)");
				response.sendRedirect("error.jsp");
			}catch(Exception ex) {
				System.out.println(ex);
			}
		}
	}
	
	public void destroy() {
	}
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
