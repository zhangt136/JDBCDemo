package com.zt.jdbc;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Test;

import com.mysql.jdbc.Driver;

public class JDBCTest {
	/**
	 * �汾1
	 * ͨ�õĸ��·���������insert , update, delete
	 * @param sql
	 */
	public void update(String sql){
		Connection connection = null;
		Statement statement = null;
		
		try {
			connection = JDBCTools.getConnection();
			statement = connection.createStatement();
			statement.executeUpdate(sql);
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			try {
				statement.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	@Test
	// һ���򵥵ĸ��·�������ɾ�ģ�
	public void testStatemenet() throws Exception{
		// 1.�������ݿ�
		Connection connection = getConnection2();
		// 2.׼��sql���
		String sql = "insert into user values(null,'lisi','123456','1367311')";
		
		// 3.ִ�в���
		// 1�� ��ȡstatement����ִ��sql
		Statement statement = connection.createStatement();
		// 2�� ����statement�����executeUpdate(sql)ִ��sql���
		statement.executeUpdate(sql);
		// 4.�ر�����
		statement.close();
		connection.close();
	}
	
	
	
	
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
		// ͨ�����䴴������������Ȼʹ����Driver�������޷����
		Driver driver = (Driver)Class.forName(driverClassName).newInstance();
		properties.put("user", user);
		properties.put("password", password);
		Connection connection = driver.connect(jdbcUrl, properties);
		return connection;
	}
	
	
	
}
