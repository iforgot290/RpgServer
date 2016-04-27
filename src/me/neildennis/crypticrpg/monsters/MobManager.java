package me.neildennis.crypticrpg.monsters;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.Manager;
import me.neildennis.crypticrpg.cloud.Cloud;

public class MobManager extends Manager{
	
	private ArrayList<SpawnBlock> spawners;
	
	public MobManager(){
		spawners = new ArrayList<SpawnBlock>();
		loadMobSpawns();
		registerTasks();
	}
	
	@Override
	public void registerTasks(){
		tasks.add(Bukkit.getScheduler().runTaskTimer(Cryptic.getPlugin(), new Runnable(){
			
			@Override
			public void run(){
				for (SpawnBlock block : spawners) block.tickSpawns();
			}
			
		}, 20L, 20L));
	}
	
	public void loadMobSpawns(){
		ResultSet data = Cloud.sendQuery("SELECT * FROM monster_spawns");
		try {
			while (data.next()){
				int id = data.getInt("spawner_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
