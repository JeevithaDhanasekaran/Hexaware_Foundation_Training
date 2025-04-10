package com.carrental.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBPropertyUtil {
	//static method to get the connection string from the filename(db.properties)
	public static String getConnectionString(String fileName) {
		StringBuilder propsString=new StringBuilder();//to store connection string
		Properties props=new Properties();
		
		try(FileInputStream fileInp=new FileInputStream(fileName)){
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
			e.printStackTrace();
		}
		return propsString.toString();
	}
}
