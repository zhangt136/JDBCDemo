package com.zt.jdbc;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * ���߷���
 * @author zhangt
 *
 */
public class JDBCTools {
	/**
	 * ִ��sql��䣬ʹ��preparedStatement,�ṩ��ռλ��
	 * @param sql
	 * @param args
	 */
	public static void update(String sql, Object... args){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JDBCTools.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			
			for(int i = 0 ; i < args.length; i++){
				preparedStatement.setObject(i+1, args[i]);
			}
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCTools.release(null, preparedStatement, connection);
		}
	}
	
	
	
	/**
	 * �ر���������
	 * @param statement
	 * @param connection
	 */
	public static void release(ResultSet rs, Statement statement , Connection connection){

		if(rs!=null){
			try {
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
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
	 * ��ȡ���ӵķ���
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection() throws Exception {
		//��ȡ��·���µ�jdbc.properties�ļ����Ծ�������ÿ�����Ӷ����ȡһ���ļ���
		InputStream inputStream = JDBCTools.class.getClassLoader().getResourceAsStream("jdbc.properties");
		Properties properties = new Properties();
		properties.load(inputStream);
		// ��ȡ�ļ�����
		String driverClassName = properties.getProperty("driver");
		String jdbcUrl = properties.getProperty("jdbcUrl");
		String user = properties.getProperty("user");
		String password = properties.getProperty("password");
		properties.put("user", user);
		properties.put("password", password);
		// ��Driver�о�̬�������DriverManager.registerDriver(new Driver());
		Class.forName(driverClassName); 
		Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
		return connection;
	}
}
