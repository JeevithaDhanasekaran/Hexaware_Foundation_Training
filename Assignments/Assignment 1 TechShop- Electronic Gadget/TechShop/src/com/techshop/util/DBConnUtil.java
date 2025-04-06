package com.techshop.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


public class DBConnUtil {
	private static Connection conn;
	public static Connection getConnection() throws Exception{
	if (conn == null || conn.isClosed()) {//handle null and reassigning
        try (InputStream input = DBConnUtil.class.getClassLoader().getResourceAsStream("dbconfig.properties")) {
            if (input == null) {
                throw new RuntimeException("Database properties file not found!");//if properties file is not present or any errors
            }

            Properties props = new Properties();
            props.load(input); // load props

            String url = props.getProperty("db.url");
            String userName = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            String driver = props.getProperty("db.driver");

            Class.forName(driver); // load JDBC driver

            conn = DriverManager.getConnection(url, userName, password);
            System.out.println("Database connected successfully");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection failed");
        }
    }
    return conn;
}

}
