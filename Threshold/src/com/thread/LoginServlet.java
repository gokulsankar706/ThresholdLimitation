//$Id$
package com.thread;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import leakybucket.LeakyUserBucket;

public class LoginServlet extends HttpServlet {

	private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String userName = request.getParameter("Uname");
		String password = request.getParameter("Pass");
		String ip = request.getRemoteAddr();
		String reqLimit = null;;
		try {
			reqLimit = constants.ConstantsHandler.getValue();
		} catch (Exception e) {
			LOGGER.info("Exception while getting constant values from constants.xml");
		}

		PrintWriter out = response.getWriter();
		if(!ip.isEmpty() && !reqLimit.isEmpty()) {
			LeakyUserBucket user = new LeakyUserBucket(ip, Integer.parseInt(reqLimit));
			for(int i=0; i<12; i++) {
				if(user.userAccess(ip)) {
					System.out.println("Hello "+userName+"\nYour password is : "+password);
				}
				else {
					System.out.println("Sorry.... :)");
				}
			}
		}
	}
}
