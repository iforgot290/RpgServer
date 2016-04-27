package me.neildennis.crypticrpg.cloud;

import java.sql.ResultSet;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import io.netty.util.internal.ConcurrentSet;
import me.neildennis.crypticrpg.Cryptic;

public class Cloud {

	private ConcurrentSet<BukkitTask> tasks;
	private static QueryThread querythread;
	
	private static CrossServerReceive receive;
	private static CrossServerSend send;

	public Cloud(){
		tasks = new ConcurrentSet<BukkitTask>();
		
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

	public void registerTasks(){
		tasks.add(Bukkit.getScheduler().runTaskAsynchronously(Cryptic.getPlugin(), querythread));
		//tasks.add(Bukkit.getScheduler().runTaskAsynchronously(Cryptic.getPlugin(), receive));
		//tasks.add(Bukkit.getScheduler().runTaskAsynchronously(Cryptic.getPlugin(), send));
	}

	public static void sendStatementAsync(String statement){
		querythread.addQuery(statement);
	}

	public static void sendStatement(String statementStr){
		try {
			Statement statement = ConnectionPool.getConnection().createStatement();
			statement.execute(statementStr);
			statement.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public static ResultSet sendQuery(String query){
		try {
			Statement statement = ConnectionPool.getConnection().createStatement();
			ResultSet res = statement.executeQuery(query);
			
			if (res.next()) return res;
			return null;
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static void sendCrossServer(int to, String query){
		send.sendCrossServer(to, query);
	}

}
