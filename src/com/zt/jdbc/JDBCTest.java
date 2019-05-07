package com.zt.jdbc;


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
	
	
	
}
