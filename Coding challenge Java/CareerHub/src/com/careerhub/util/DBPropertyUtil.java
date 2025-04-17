package com.careerhub.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class DBPropertyUtil {
	public static String getConnectionString(String fileName) {
		StringBuilder propsString=new StringBuilder();//to store connection string
		Properties props=new Properties();
		
		try{
			InputStream fileInp= DBPropertyUtil.class.getClassLoader().getResourceAsStream(fileName);
			props.load(fileInp);//load file
			
			String driver=props.getProperty("db.driver");
			String url=props.getProperty("db.url");
			String user=props.getProperty("db.user");
			String password=props.getProperty("db.password");
			
			propsString.append(driver).append(";")
			.append(url).append(";")
			.append(user).append(";")
			.append(password).append(";");
			
		} catch (IOException e) {
			System.err.println("Error loading database properties file.");
			e.printStackTrace();
		}
		return propsString.toString();
	}
}
