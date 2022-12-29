//$Id$
package com.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.handlers.RequestHandler;
import com.validators.UserRequestLimitValidation;

@WebServlet("/login")
public class ThresholdFilter extends HttpServlet{

	public void doPost(HttpServletRequest request, HttpServletResponse response) {

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
				System.out.println("Exception occured "+e);
				try {
					HttpSession session = request.getSession();
					session.setAttribute("message", "Somthing went wrong kindly contact admin.. :)");
					response.sendRedirect("error.jsp");
				}catch(Exception ex) {
					System.out.println(ex);
				}
			}
	}
}
