package com.imooc.miaosha.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

	public static Connection getConn() throws Exception{
		String url = "jdbc:mysql://192.168.0.100:3306/miaosha?serverTimezone=GMT%2B8";
		String username = "root";
		String password = "7292183hkx";
		String driver = "com.mysql.cj.jdbc.Driver";
		Class.forName(driver);
		return DriverManager.getConnection(url,username, password);
	}
}
