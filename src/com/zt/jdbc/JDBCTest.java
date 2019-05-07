package com.zt.jdbc;


import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

import org.junit.Test;

import com.mysql.jdbc.Driver;

public class JDBCTest {
	
	@Test
	// ��Ϊԭʼ���������ݿⷽ�����˷�����ͨ�ã�
	public void preJdbcTest() throws Exception{
		// 1.����һ��Driver����
		Driver driver = new Driver();
		// 2.׼���������ݿ����Ϣ��url,root,pwd
		String url = "jdbc:mysql://127.0.0.1:3306/day01";
		Properties properties = new Properties();
		properties.put("user", "root");
		properties.put("password", "123456");
		// 3.����Driver�ӿڵ�connect�������ݿ�
		Connection connection = driver.connect(url, properties);
		System.out.println(connection);
	}
	
	
	/**
	 * ͨ�������ļ���ʵ�������ļ������Ľ���
	 * @return ��������
	 * @throws Exception
	 */
	public Connection getConnection() throws Exception{
		String driverClassName = null;
		String jdbcUrl = null;
		String user = null;
		String password = null;
		
		//��ȡ��·���µ�jdbc.properties�ļ�
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("jdbc.properties");
		Properties properties = new Properties();
		properties.load(inputStream);
		// ��ȡ�ļ�����
		driverClassName = properties.getProperty("driver");
		jdbcUrl = properties.getProperty("jdbcUrl");
		user = properties.getProperty("user");
		password = properties.getProperty("password");
		// ͨ�����䴴����������
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
