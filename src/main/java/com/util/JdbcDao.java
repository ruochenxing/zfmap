package com.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库底层实现类
 * 需要使用mysql jar包
 * */
public class JdbcDao {
	private static String DRIVER="com.mysql.jdbc.Driver";
	private String URL="jdbc:mysql://localhost:3306/job?characterEncoding=UTF-8";
	private String USER="root";
	private String PASSWORD="password";
	private Connection conn;
	private PreparedStatement pst;
	static{
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			System.err.println("加载数据库驱动失败");
			e.printStackTrace();
		}
	}
	public JdbcDao(){}
	
	/**
	 * 获取数据库连接
	 * */
	public void getConn(){
		try {
			conn=DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			System.err.println("获取数据库接连失败");
			e.printStackTrace();
		}
	}
	
	/**
	 * 执行sql语句
	 * */
	public void doStatement(String sql,Object []params){
		if(sql!=null&&!sql.equals("")){
			getConn();
			try {
				//双向滚动，但不及时更新。就是如果数据库里的数据修改过，并不在ResultSet中反应出来。只读
				pst=conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				if(params==null){
					params=new Object[0];
				}
				for(int i=0;i<params.length;i++){
					pst.setObject(i+1, params[i]);
				}
				pst.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 返回执行结果集
	 * */
	public ResultSet getResultSet(){
		try {
			return pst.getResultSet();
		} catch (SQLException e) {
			System.err.println("返回执行结果集失败");
			e.printStackTrace();
			return null;
		}
	}
	
	public int getUpdate(){
		try {
			return pst.getUpdateCount();
		} catch (SQLException e) {			
			e.printStackTrace();
			return -1;
		}finally{
			if(pst!=null){
				try {
					pst.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public void closed(){
		try{
			if(pst!=null)
				pst.close();
		}catch(Exception e){
			System.out.println("关闭pst对象失败！");
		}
		try{
			if(conn!=null)
				conn.close();
		}catch(Exception e){
			System.out.println("关闭conn对象失败！");
		}
	}
	
	/******************测试************************/
	public static void main(String []args) throws Exception{
		
	}
	public int update(String sql,Object[] params){
		doStatement(sql,params);
		int i=getUpdate();
		return i;
	}
}