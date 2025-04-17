package com.hexaware.carrental.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {
	private static Connection connection=null;
	
	public static Connection getConnection(String fileName){
		
		try {
			String connectionString=DBPropertyUtil.getConnectionString(fileName);
			String parts[]=connectionString.split(";");//splits using ;
			
			
			String driver=parts[0];
			String url=parts[1];
			String user=parts[2];
			String password=parts[3];
			
			Class.forName(driver);
			
			connection=DriverManager.getConnection(url,user,password);
//			System.out.println("Database connected Successfully !");
			
			
		} catch (ClassNotFoundException |  SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Database Connection Failed");
		}
		return connection;
	}
}
