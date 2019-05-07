package com.zt.jdbc;


import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

import com.mysql.jdbc.Driver;


public class JDBCTest {
	
	@Test
	public void testGet(){
		// 需要使用sql的别名与类的属性名对应
		String sql = "select flowid flowId , type , idcard idCard from student where id = ?";
		Student student = get(Student.class, sql, 1);
		System.out.println(student);
	}
	
	
	public <T> T get(Class<T> clazz,String sql, Object... args){
		T entity = null;
		Connection connection =null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = JDBCTools.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			// 为每一个占位符赋值
			for(int i = 0 ; i< args.length; i++){
				preparedStatement.setObject(i+1, args[i]);
			}
			resultSet = preparedStatement.executeQuery();
			// 获取ResultSetMetaData对象
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			Map<String, Object> valusMap = new HashMap<>();
			while(resultSet.next()){
				// 打印每一列的列明和列值
				for(int i = 0 ; i < resultSetMetaData.getColumnCount(); i++){
					String columName = resultSetMetaData.getColumnLabel(i+1);
					Object columValue = resultSet.getObject(columName);
					valusMap.put(columName, columValue);
				}
			}
			
			// 给返回对象赋值
			for(Map.Entry<String, Object> entry : valusMap.entrySet()){
				String fieldName = entry.getKey();
				Object fieldValue = entry.getValue();
				ReflectionUtils.setFieldValue(entity, fieldName,fieldValue );
				Field field = clazz.getField(fieldName);
				field.set(entity, fieldValue);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCTools.release(resultSet, preparedStatement, connection);
		}
		return entity;
	}
	
	
	
	
	
	
	
	
	@Test
	public void testPreparedStatement(){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JDBCTools.getConnection();
			String sql = "insert into user values(?,?,?,?)";
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, null);
			preparedStatement.setString(2, "wangwu");
			preparedStatement.setString(3, "123321"); 
			preparedStatement.setString(4, "13673111");
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCTools.release(null, preparedStatement, connection);
		}
		
	}
	
	
	
	
	@Test
	public void addNewStudent(Student student){
		String sql = "insert into student values ";
	}
	
	@Test
	public void testResultSet(){
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		try {
			connection = JDBCTools.getConnection();
			statement = connection.createStatement();
			String sql = "select * from user where uid = 1";
			
			rs = statement.executeQuery(sql);
			
			while(rs.next()){
				int id = rs.getInt(1);
				String name = rs.getString(2);
				String ps = rs.getString(3);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCTools.release(rs, statement, connection);
		}
	}
	
	
	/**
	 * 版本1
	 * 通用的更新方法：包括insert , update, delete
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
	// 一个简单的更新方法（增删改）
	public void testStatemenet() throws Exception{
		// 1.链接数据库
		Connection connection = JDBCTools.getConnection();
		// 2.准备sql语句
		String sql = "insert into user values(null,'lisi','123456','1367311')";
		
		// 3.执行插入
		// 1） 获取statement对象执行sql
		Statement statement = connection.createStatement();
		// 2） 调用statement对象的executeUpdate(sql)执行sql语句
		statement.executeUpdate(sql);
		// 4.关闭链接
		statement.close();
		connection.close();
	}
	
	
	
	
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
		// 通过反射创建驱动对象（依然使用了Driver，还是无法解耦）
		Driver driver = (Driver)Class.forName(driverClassName).newInstance();
		properties.put("user", user);
		properties.put("password", password);
		Connection connection = driver.connect(jdbcUrl, properties);
		return connection;
	}
	
	
	
}
