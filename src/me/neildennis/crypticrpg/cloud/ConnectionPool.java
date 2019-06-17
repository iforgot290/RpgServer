package me.neildennis.crypticrpg.cloud;

import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionPool {

	private static String ip;
	private static String port;
	private static String database;
	private static String user;
	private static String password;

	private static String cloud_ip;
	private static int cloud_port;

	private static Connection con;
	private static Socket cloud;

	private static PrintWriter print;

	public static Connection getConnection(){
		try {
			if (con == null || con.isClosed()){
				
				ip = System.getProperty("crypticrpg.sql.ip");
				port = System.getProperty("crypticrpg.sql.port");
				database = System.getProperty("crypticrpg.sql.database");
				user = System.getProperty("crypticrpg.sql.user");
				password = System.getProperty("crypticrpg.sql.password");
				
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

	public static Socket getCloudSocket(){
		try {
			if (cloud == null || cloud.isClosed()){
				
				cloud_ip = System.getProperty("crypticrpg.cloud.ip");
				cloud_port = Integer.parseInt(System.getProperty("crypticrpg.cloud.port"));
				
				cloud = new Socket(cloud_ip, cloud_port);
			}
		} catch (Exception e){
			e.printStackTrace();
		}

		return cloud;
	}

	public static PrintWriter getCloudWriter(){
		try {
			if (print == null || cloud == null || cloud.isClosed()){
				Socket sock = getCloudSocket();
				print = new PrintWriter(sock.getOutputStream());
			}
		} catch (Exception e){
			e.printStackTrace();
			print = null;
		}
		
		return print;
	}

}
