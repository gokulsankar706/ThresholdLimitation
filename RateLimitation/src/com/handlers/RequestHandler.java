//$Id$
package com.handlers;

import java.sql.ResultSet;

import com.connectors.MySqlConnector;

public class RequestHandler {

	public static boolean login(String username, String password) throws Exception{
		if(!password.isEmpty()) {
			String query = "select user_id from UserDetails where username = '"+username+"' and password = '"+password+"'";
			MySqlConnector.getSQLConnection();
			ResultSet resultSet = MySqlConnector.executeQuery(query);
			if(resultSet.next()) {
				return true;
			}
			MySqlConnector.closeSQLConnection();
			return false;
		}
		System.out.println("please valid details");
		return false;
	}

	public static boolean signUp(String username, String password) {
		try {
			String query = "INSERT INTO UserDetails (username, password) VALUES ('"+username+"','"+password+"')";
			MySqlConnector.getSQLConnection();
			boolean isUpdated = MySqlConnector.updateQuery(query);
			MySqlConnector.closeSQLConnection();
			if(isUpdated) {
				return true;
			}
		}catch(Exception ex) {
			System.out.println("Exception while updating details in DB"+ex);
		}
		return false;
	}
}
