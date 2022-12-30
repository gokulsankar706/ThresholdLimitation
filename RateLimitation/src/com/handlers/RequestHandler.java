//$Id$
package com.handlers;

import java.sql.Connection;
import java.sql.ResultSet;

import com.connectors.MySqlConnector;

public class RequestHandler {

	public static boolean login(String username, String password) throws Exception{
		if(!password.isEmpty()) {
			String query = "select user_id from UserDetails where username = '"+username+"' and password = '"+password+"'";
			MySqlConnector mysqlconnector = MySqlConnector.getInstance();
			mysqlconnector.getConnection();
			ResultSet resultSet = mysqlconnector.executeQuery(query);
			if(resultSet.next()) {
				return true;
			}
			mysqlconnector.closeSQLConnection();
			return false;
		}
		System.out.println("please enter valid details");
		return false;
	}

	public static boolean signUp(String username, String password) {
		try {
			String query = "INSERT INTO UserDetails (username, password) VALUES ('"+username+"','"+password+"')";
			MySqlConnector mysqlconnector = MySqlConnector.getInstance();
			mysqlconnector.getConnection();
			boolean isUpdated = mysqlconnector.updateQuery(query);
			mysqlconnector.closeSQLConnection();
			if(isUpdated) {
				return true;
			}
		}catch(Exception ex) {
			System.out.println("Exception while updating details in DB"+ex);
		}
		return false;
	}
}
