package me.neildennis.crypticrpg.cloud;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import io.netty.util.internal.ConcurrentSet;
import minecade.dungeonrealms.Main;

public class Cloud {
	
	private ConcurrentSet<BukkitTask> tasks;
	private static QueryThread querythread;
	
	public void onEnable(){
		querythread = new QueryThread();
	}
	
	public void registerTasks(){
		BukkitTask querytask = Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, querythread);
		tasks.add(querytask);
	}
	
	public static void sendQueryAsync(String query){
		querythread.addQuery(query);
	}

}
