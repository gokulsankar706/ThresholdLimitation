//$Id$
package com.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.validators.UserRequestLimitValidation;

public class ThresholdFilter extends HttpServlet{
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		
		String Ip = request.getRemoteAddr();
		boolean isValid = true;
		UserRequestLimitValidation.reqLimitValidation(Ip);
		
	}
}
