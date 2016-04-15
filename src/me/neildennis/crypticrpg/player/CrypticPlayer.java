package me.neildennis.crypticrpg.player;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import me.neildennis.crypticrpg.health.HealthData;
import minecade.dungeonrealms.Main;

public class CrypticPlayer {
	
	private UUID id;
	private OfflinePlayer player;
	
	private ArrayList<BukkitTask> tasks;
	private ArrayList<PlayerBuff> buffs;
	
	private HealthData healthData;
	
	public CrypticPlayer(UUID id){
		this.id = id;
		player = Bukkit.getOfflinePlayer(id);
		tasks = new ArrayList<BukkitTask>();
		buffs = new ArrayList<PlayerBuff>();
		
		healthData = new HealthData(this);
	}
	
	public void registerTasks(){
		healthData.registerTasks();
		
		BukkitTask tickBuff = Bukkit.getScheduler().runTaskTimer(Main.plugin, new Runnable(){
			
			public void run(){
				for (PlayerBuff buff : buffs){
					buff.tickBuff();
					if (buff.isExpired()) buffs.remove(buff);
				}
			}
			
		}, 5L, 1L);
		
		tasks.add(tickBuff);
	}
	
	public void cancelTasks(){
		for (BukkitTask task : tasks){
			task.cancel();
		}
		
		tasks.clear();
	}
	
	public void cancelBuffs(){
		buffs.clear();
	}
	
	public UUID getId(){
		return id;
	}
	
	public Player getPlayer(){
		if (player.isOnline()){
			return player.getPlayer();
		} else {
			return null;
		}
	}
	
	public OfflinePlayer getOfflinePlayer(){
		return player;
	}
	
	public boolean isOnline(){
		return player.isOnline();
	}
	
	public void addTask(BukkitTask task){
		tasks.add(task);
	}
	
	public void addBuff(PlayerBuff buff){
		buffs.add(buff);
	}
	
	public HealthData getHealthManager(){
		return healthData;
	}
	
	

}
