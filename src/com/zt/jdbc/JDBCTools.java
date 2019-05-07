package com.zt.jdbc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

/**
 * 工具方法
 * @author zhangt
 *
 */
public class JDBCTools {
	
	/**
	 * 关闭连接数据
	 * @param statement
	 * @param connection
	 */
	public static void release(Statement statement , Connection connection){
		if(statement!=null){
			try {
				statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(connection!=null){
			try {
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 获取连接的方法
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
		//读取类路径下的jdbc.properties文件（仍旧有问题每次连接都会读取一次文件）
		InputStream inputStream = JDBCTools.class.getClassLoader().getResourceAsStream("jdbc.properties");
		Properties properties = new Properties();
		properties.load(inputStream);
		// 读取文件内容
		String driverClassName = properties.getProperty("driver");
		String jdbcUrl = properties.getProperty("jdbcUrl");
		String user = properties.getProperty("user");
		String password = properties.getProperty("password");
		properties.put("user", user);
		properties.put("password", password);
		// 在Driver中静态代码块中DriverManager.registerDriver(new Driver());
		Class.forName(driverClassName); 
		Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
		return connection;
	}
}
