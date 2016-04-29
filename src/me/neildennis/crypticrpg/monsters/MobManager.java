package me.neildennis.crypticrpg.monsters;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.Manager;
import me.neildennis.crypticrpg.cloud.Cloud;
import me.neildennis.crypticrpg.monsters.templates.SpawnTemplate;
import me.neildennis.crypticrpg.utils.Utils;

public class MobManager extends Manager{
	
	private static ArrayList<SpawnBlock> spawners;
	
	public MobManager(){
		spawners = new ArrayList<SpawnBlock>();
		
		Bukkit.getPluginManager().registerEvents(new SpawnPlaceListener(), Cryptic.getPlugin());
		Bukkit.getPluginManager().registerEvents(new MobListener(), Cryptic.getPlugin());
		
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
				Location loc = Utils.getLocFromString(data.getString("location"));
				int range = data.getInt("spawn_range");
				int minlvl = data.getInt("minlvl");
				int maxlvl = data.getInt("maxlvl");
				JsonArray monsters = (JsonArray) new JsonParser().parse(data.getString("monsters"));
				spawners.add(new SpawnBlock(id, loc, range, minlvl, maxlvl, monsters));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static SpawnBlock getSpawnBlock(int id){
		for (SpawnBlock blk : spawners)
			if (blk.getId() == id)
				return blk;
		return null;
	}
	
	public static SpawnBlock createNewSpawnBlock(Location loc, int range, int minlvl, int maxlvl){
		return createNewSpawnBlock(loc, range, minlvl, maxlvl, new ArrayList<SpawnTemplate>());
	}
	
	public static SpawnBlock createNewSpawnBlock(Location loc, int range, int minlvl, int maxlvl, ArrayList<SpawnTemplate> temps){
		SpawnBlock block = new SpawnBlock(0, loc, range, minlvl, maxlvl, temps);
		spawners.add(block);
		return block;
	}
	
	public static boolean addSpawnBlock(SpawnBlock spawner){
		if (spawner.getId() != 0 && getSpawnBlock(spawner.getId()) != null) return false;
		spawners.add(spawner);
		return true;
	}
	
	public static SpawnTemplate getCrypticEntity(Entity ent){
		for (SpawnBlock blk : spawners)
			for (SpawnTemplate temp : blk.getSpawns())
				if (temp.getEntity().equals(ent))
					return temp;
		return null;
	}

}
