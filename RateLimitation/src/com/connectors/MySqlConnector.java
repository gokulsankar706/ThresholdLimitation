//$Id$
package com.connectors;

import java.sql.*;

public class MySqlConnector {

	private static Connection connection;
	private static Statement statement;
	public static void getSQLConnection() throws Exception{
		String url = "jdbc:mysql://localhost:3306/Gokul";
		String uname = "root";
		String pass = "";
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection(url, uname, pass);
		statement = connection.createStatement();	
	}

	public static ResultSet executeQuery(String query){
		try {
			ResultSet resultSet = statement.executeQuery(query);
			return resultSet;
		}catch(Exception ex) {
			System.out.println("Exception while executing query..");
		}
		return null;
	}

	public static boolean updateQuery(String query) {
		try {
			int count = statement.executeUpdate(query);
			if(count>0) {
				return true;
			}
		}catch(Exception ex) {
			System.out.println("Exception while updating query"+ex);
		}
		return false;
	}

	public static void closeSQLConnection() throws Exception{
		statement.close();
		connection.close();
	}
}
