package com.zt.jdbc;


import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import org.junit.Test;

import com.mysql.jdbc.Driver;

public class JDBCTest {
	
	@Test
	// 最为原始的链接数据库方法（此方法不通用）
	public void preJdbcTest() throws Exception{
		// 1.创建一个Driver驱动
		Driver driver = new Driver();
		// 2.准备链接数据库的信息，url,root,pwd
		String url = "jdbc:mysql://127.0.0.1:3306/day01";
		Properties properties = new Properties();
		properties.put("user", "root");
		properties.put("password", "123456");
		// 3.调用Driver接口的connect链接数据库
		Connection connection = driver.connect(url, properties);
		System.out.println(connection);
	}
	
	
	/**
	 * 通过配置文件，实现配置文件与程序的解耦
	 * @return 返回链接
	 * @throws Exception
	 */
	public Connection getConnection() throws Exception{
		String driverClassName = null;
		String jdbcUrl = null;
		String user = null;
		String password = null;
		
		//读取类路径下的jdbc.properties文件
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		Properties properties = new Properties();
		properties.load(inputStream);
		// 读取文件内容
		driverClassName = properties.getProperty("driver");
		jdbcUrl = properties.getProperty("jdbcUrl");
		user = properties.getProperty("user");
		password = properties.getProperty("password");
		// 通过反射创建驱动对象
		Driver driver = (Driver)Class.forName(driverClassName).newInstance();
		properties.put("user", user);
		properties.put("password", password);
		Connection connection = driver.connect(jdbcUrl, properties);
		return connection;
	}
	
	@Test
	public void testgetConnection() {
		try {
			System.out.println(getConnection());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
