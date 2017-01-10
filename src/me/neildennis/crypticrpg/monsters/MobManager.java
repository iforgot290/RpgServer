package me.neildennis.crypticrpg.monsters;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.Manager;
import me.neildennis.crypticrpg.cloud.Cloud;
import me.neildennis.crypticrpg.monsters.commands.CommandSpawner;
import me.neildennis.crypticrpg.monsters.spawnblock.SpawnBlock;
import me.neildennis.crypticrpg.monsters.spawnblock.SpawnBlockMonster;
import me.neildennis.crypticrpg.monsters.spawnblock.SpawnPlaceListener;
import me.neildennis.crypticrpg.monsters.type.CrypticMonster;
import me.neildennis.crypticrpg.utils.Utils;

public class MobManager extends Manager{
	
	private static ArrayList<SpawnBlock> spawners;
	private static ArrayList<MonsterContainer> monsters;
	
	public MobManager(){
		spawners = new ArrayList<SpawnBlock>();
		monsters = new ArrayList<MonsterContainer>();
		
		Bukkit.getPluginManager().registerEvents(new SpawnPlaceListener(), Cryptic.getPlugin());
		Bukkit.getPluginManager().registerEvents(new MobListener(), Cryptic.getPlugin());
		
		Cryptic.getPlugin().getCommand("spawner").setExecutor(new CommandSpawner());
		
		loadMobSpawns();
		registerTasks();
		
		//clear all mobs
		for (Entity ent : Cryptic.getMainWorld().getEntities())
			if (ent instanceof Monster) ent.remove();
	}
	
	@Override
	public void registerTasks(){
		tasks.add(Bukkit.getScheduler().runTaskTimer(Cryptic.getPlugin(), new Runnable(){
			
			@Override
			public void run(){
				for (SpawnBlock block : spawners){
					block.tickSpawns();
				}
			}
			
		}, 20L, 20L));
	}
	
	public void loadMobSpawns(){
		try {
			ResultSet data = Cloud.sendQuery("SELECT * FROM monster_spawns");
			while (data.next()){
				UUID id = UUID.fromString(data.getString("uuid"));
				Location loc = Utils.getLocFromString(data.getString("location"));
				int range = data.getInt("spawn_range");
				JsonArray monsters = (JsonArray) new JsonParser().parse(data.getString("monsters"));
				spawners.add(new SpawnBlock(id, loc, range, monsters));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<SpawnBlock> getSpawnBlocks(){
		return spawners;
	}
	
	public static SpawnBlock getSpawnBlock(UUID id){
		for (SpawnBlock blk : spawners)
			if (blk.getId().equals(id))
				return blk;
		return null;
	}
	
	public static SpawnBlock getSpawnBlock(Location loc){
		for (SpawnBlock blk : spawners)
			if (blk.getLocation().equals(loc))
				return blk;
		return null;
	}
	
	public static SpawnBlock createNewSpawnBlock(Location loc, int range){
		return createNewSpawnBlock(loc, range, new ArrayList<SpawnBlockMonster>());
	}
	
	public static SpawnBlock createNewSpawnBlock(Location loc, int range, ArrayList<SpawnBlockMonster> temps){
		SpawnBlock block = new SpawnBlock(UUID.randomUUID(), loc, range, temps);
		spawners.add(block);
		return block;
	}
	
	public static void addSpawnBlock(SpawnBlock spawner){
		spawners.add(spawner);
	}
	
	public static void registerMonster(MonsterContainer monster){
		monsters.add(monster);
	}
	
	public static void unregisterMonster(MonsterContainer monster){
		monsters.remove(monster);
	}
	
	public static CrypticMonster getMonster(Entity ent){
		for (MonsterContainer container : monsters)
			if (container.getMonster().getEntity() == ent)
				return container.getMonster();
		return null;
	}
	
	public static MonsterContainer getMonsterContainer(Entity ent){
		for (MonsterContainer container : monsters){
			if (container.getMonster() == null) continue;
			if (container.getMonster().getEntity() == ent)
				return container;
		}
		return null;
	}
	
	public static MonsterContainer getMonsterContainer(CrypticMonster monster){
		for (MonsterContainer container : monsters)
			if (container.getMonster() == monster)
				return container;
		return null;
	}

}
