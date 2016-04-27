package me.neildennis.crypticrpg;

import java.util.ArrayList;

import org.bukkit.scheduler.BukkitTask;

public abstract class Manager {
	
	protected ArrayList<BukkitTask> tasks = new ArrayList<BukkitTask>();
	
	public abstract void registerTasks();
	
	public void cancelTasks(){
		for (BukkitTask task : tasks) task.cancel();
		tasks.clear();
	}

}
