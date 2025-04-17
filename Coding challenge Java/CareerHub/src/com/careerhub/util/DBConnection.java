package com.careerhub.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {
private static Connection connection=null;
	
	public static Connection getConnection(String fileName){
		
		try {
			String connectionString=DBPropertyUtil.getConnectionString(fileName);
			String parts[]=connectionString.split(";");//splits using ;
			
			//maintain the order of appending in DBPropertyUtil
			String driver=parts[0];
			String url=parts[1];
			String user=parts[2];
			String password=parts[3];
			
			Class.forName(driver);
			
			connection=DriverManager.getConnection(url,user,password);
//			System.out.println("Database connected Successfully !");
			
			
		}  catch (ClassNotFoundException e) {
		    System.err.println("JDBC Driver not found: " + e.getMessage());
		    throw new RuntimeException("JDBC Driver not found", e);
		} catch (SQLException e) {
		    System.err.println("SQL Exception: " + e.getMessage());
		    throw new RuntimeException("Database Connection Failed", e);
		}
		return connection;
	}
}
