package com.zt.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DAO {
	/**
	 * insert update delete 都包含其中
	 * @param sql
	 * @param args
	 */
	public void update(String sql, Object... args) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = JDBCTools.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			JDBCTools.release(null, preparedStatement, connection);
		}

	}

	/**
	 * 查询一条记录，并返回对象的List
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 */
	public <T> T get(Class<T> clazz, String sql, Object... args) {
		List<T> result = getForList(clazz, sql, args);
		return result.get(0);
	}
	
	public <T> List<T> getForList(Class<T> clazz, String sql, Object... args){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<T> result = new ArrayList<>();
		try {
			connection = JDBCTools.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			for (int i = 0; i < args.length; i++) {
				preparedStatement.setObject(i + 1, args[i]);
			}
			resultSet = preparedStatement.executeQuery();
			List<Map<String, Object>> values = handleResultSetToMapList(resultSet);
			result = transfterMapListToBeanList(clazz, values);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCTools.release(resultSet, preparedStatement, connection);
		}
		return result;
	}

	/**
	 * 将Map的List转为对应的集合
	 * @param clazz
	 * @param values
	 */
	private <T> List<T> transfterMapListToBeanList(Class<T> clazz, List<Map<String, Object>> values) throws Exception{
		T entity = null;
		List<T> list = new ArrayList<>();
		for(Map<String, Object> map: values){
			entity = clazz.newInstance();
			for(Map.Entry<String, Object> entry : map.entrySet()){
				String field = entry.getKey();
				Object value = entry.getValue();
				ReflectionUtils.setFieldValue(entity,field, value);
			}
			list.add(entity);
		}
		return list;
	}

	/**
	 * 处理结果集，将其转换为map，一个map对应一条记录
	 * @param clazz
	 * @param resultSet
	 * @param list
	 */
	private List<Map<String, Object>> handleResultSetToMapList(ResultSet resultSet) throws Exception {
		List<Map<String, Object>> values = new ArrayList<>();
		while (resultSet.next()) {
			Map<String, Object> valuesMap = new HashMap<>();
			List<String> columLables = getColumLables(resultSet);
			for (String columLable:columLables) {
				Object columValue = resultSet.getObject(columLable);
				valuesMap.put(columLable, columValue);
			}
			values.add(valuesMap);
		}
		return values;
	}
	
	/**
	 * 获取列的别名
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	private List<String> getColumLables(ResultSet rs) throws Exception{
		List<String> list = new ArrayList<>();
		ResultSetMetaData resultSetMetaData = rs.getMetaData();
		for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
			String columLable = resultSetMetaData.getColumnLabel(i + 1);
			list.add(columLable);
		}
		return list;
	}
	
	/**
	 * 返回某条记录的一个值
	 * @param sql
	 * @param args
	 * @return
	 */
	public <T> T getForValue(String sql, Object...args){
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = JDBCTools.getConnection();
			preparedStatement = connection.prepareStatement(sql);
			for(int i = 0; i< args.length ;i++){
				preparedStatement.setObject(i+1, args[i]);
			}
			rs = preparedStatement.executeQuery();
			if(rs.next())
				return (T)rs.getObject(1);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCTools.release(rs, preparedStatement, connection);
		}
		return null;
	}
	

}
