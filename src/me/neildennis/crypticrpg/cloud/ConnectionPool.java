package me.neildennis.crypticrpg.cloud;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionPool {

	private static String ip;
	private static String port;
	private static String database;
	private static String user;
	private static String password;

	private static Connection con;

	public static Connection getConnection(){
		try {
			if (con == null || con.isClosed()){
				con = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + database, user, password);
			}
		} catch (Exception e){
			e.printStackTrace();
		}

		return con;
	}

	public static void refresh(){
		try {
			if (con != null){
				con.close();
			}
			
			getConnection();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

}
