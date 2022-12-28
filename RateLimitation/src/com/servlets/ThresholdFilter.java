//$Id$
package com.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.handlers.RequestHandler;
import com.validators.UserRequestLimitValidation;

public class ThresholdFilter extends HttpServlet{

	public void doPost(HttpServletRequest request, HttpServletResponse response) {

		String Ip = request.getRemoteAddr();
		String uri = request.getRequestURI();
		boolean isValidReq = UserRequestLimitValidation.reqLimitValidation(Ip, uri);
		if(isValidReq) {
			String userName = request.getParameter("Uname");
			String password = request.getParameter("Pass");
			boolean isValidUser;
			try {
				isValidUser = RequestHandler.login(userName, password);
				if(isValidUser) {
					HttpSession session = request.getSession();
					session.setAttribute("username", userName);
					response.sendRedirect("welcome.jsp");
				}else {
					HttpSession session = request.getSession();
					session.setAttribute("message", userName);
					response.sendRedirect("login.jsp");
				}
			} catch (Exception e) {
				System.out.println("Exception occured"+e);
			}
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
}
