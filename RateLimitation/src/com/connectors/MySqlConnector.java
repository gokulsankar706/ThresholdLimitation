//$Id$
package com.connectors;

import java.sql.*;

public class MySqlConnector {

	private static Connection connection = null;
	private static Statement statement = null;

	private void getDBConnection(){
		String url = "jdbc:mysql://localhost:3306/Gokul";
		String uname = "root";
		String pass = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url, uname, pass);	
		}catch(Exception ex) {
			System.out.println("Exception while connect with myslq "+ex);
		}
	}

	public Connection getConnection() throws SQLException {
		if(connection != null && !connection.isClosed()) {
			return connection;
		}
		else {
			getDBConnection();
		}
		return null;
	}

	public ResultSet executeQuery(String query){
		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			return resultSet;
		}catch(Exception ex) {
			System.out.println("Exception while executing query.. "+ex);
		}
		return null;
	}

	public boolean updateQuery(String query) {
		try {
			statement = connection.createStatement();
			int count = statement.executeUpdate(query);
			if(count>0) {
				return true;
			}
		}catch(Exception ex) {
			System.out.println("Exception while updating query"+ex);
		}
		return false;
	}

	public void closeSQLConnection() throws Exception{
		connection.close();
	}
}
