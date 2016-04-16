package me.neildennis.crypticrpg.cloud;

import java.sql.ResultSet;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import io.netty.util.internal.ConcurrentSet;
import minecade.dungeonrealms.Main;

public class Cloud {

	private ConcurrentSet<BukkitTask> tasks;
	private static QueryThread querythread;

	public Cloud(){
		tasks = new ConcurrentSet<BukkitTask>();
		
		querythread = new QueryThread();
		registerTasks();
	}

	public void registerTasks(){
		BukkitTask querytask = Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, querythread);
		tasks.add(querytask);
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

}
