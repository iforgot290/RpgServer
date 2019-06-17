package me.neildennis.crypticrpg.cloud;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.Manager;

public class CloudManager extends Manager {

	//TODO fix concurrency
	private ArrayList<BukkitTask> tasks;
	private static QueryThread querythread;

	private static CrossServerReceive receive;
	private static CrossServerSend send;

	@Override
	public void onEnable() {
		tasks = new ArrayList<BukkitTask>();
		
		Cryptic.registerEvents(new CloudEventListener());

		querythread = new QueryThread();
		//receive = new CrossServerReceive();
		//send = new CrossServerSend();

		if (ConnectionPool.getCloudSocket() != null){
			System.out.println("Connected to the cloud");
		} else {
			System.out.println("Fatal error: No connection to the cloud");
		}

		if (ConnectionPool.getConnection() != null){
			System.out.println("Connected to the database");
		} else {
			System.out.println("Fatal error: No connection to the database");
		}

		registerTasks();
	}
	
	@Override
	public void onDisable() {
		
	}

	@Override
	public void registerTasks() {
		tasks.add(Bukkit.getScheduler().runTaskAsynchronously(Cryptic.getPlugin(), querythread));
		//tasks.add(Bukkit.getScheduler().runTaskAsynchronously(Cryptic.getPlugin(), receive));
		//tasks.add(Bukkit.getScheduler().runTaskAsynchronously(Cryptic.getPlugin(), send));
	}

	public static PreparedStatement getPreparedStatement(String str){
		try {
			return ConnectionPool.getConnection().prepareStatement(str, Statement.RETURN_GENERATED_KEYS);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void sendStatementAsync(String statement){
		querythread.addQuery(statement);
	}

	public static void sendStatement(String statementStr) throws SQLException{
		Statement statement = ConnectionPool.getConnection().createStatement();
		statement.execute(statementStr);
		statement.close();
	}

	public static ResultSet sendQuery(String query) throws SQLException{
		Statement statement = ConnectionPool.getConnection().createStatement();
		ResultSet res = statement.executeQuery(query);
		return res;
	}

	public static void sendCrossServer(int to, String query){
		send.sendCrossServer(to, query);
	}

}
