package me.neildennis.crypticrpg.monsters;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.neildennis.crypticrpg.monsters.templates.SpawnTemplate;

public class SpawnBlock {

	private Location loc;
	private int id;
	private int range;
	private ArrayList<SpawnTemplate> spawns;
	private boolean shown = false;

	private int minlvl;
	private int maxlvl;
	
	public SpawnBlock(int id, Location loc, int range, int minlvl, int maxlvl){
		this.id = id;
		this.loc = loc;
		this.range = range;
		this.minlvl = minlvl;
		this.maxlvl = maxlvl;
		spawns = new ArrayList<SpawnTemplate>();
	}

	public SpawnBlock(int id, Location loc, int range, int minlvl, int maxlvl, JsonArray array){
		this(id, loc, range, minlvl, maxlvl);
		
		for (JsonElement ele : array){
			if (!ele.isJsonObject()) continue;
			JsonObject obj = (JsonObject) ele;

			MobType type = MobType.valueOf(obj.get("type").getAsString());
			try {
				SpawnTemplate spawn = type.getHandleClass().newInstance();
				spawn.loadFromJson(obj);
				spawns.add(spawn);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public SpawnBlock(int id, Location loc, int range, int minlvl, int maxlvl, ArrayList<SpawnTemplate> spawns){
		this(id, loc, range, minlvl, maxlvl);
		
		this.spawns = spawns;
	}

	public void tickSpawns(){
		if (!loc.getChunk().isLoaded()) return;
		Random random = new Random();

		for (SpawnTemplate spawn : spawns){
			if (!spawn.shouldSpawn()) continue;
			int level = random.nextInt(maxlvl - minlvl) + minlvl;
			spawn.spawnMob(getRandomLocation(), level);
			spawn.applyGear();
		}
	}

	protected Location getRandomLocation(){
		Random random = new Random();
		int x = random.nextInt((range * 2) + 1) - range;
		int z = random.nextInt((range * 2) + 1) - range;

		Location spawnloc = loc.clone();
		spawnloc.add(x, 0, z);

		int spawnery = loc.getBlockY();
		for (int y = spawnery - range; y < spawnery + range; y++){
			spawnloc.setY(y);
			if (spawnloc.getBlock().getType() == Material.AIR)
				return spawnloc;
		}

		return getRandomLocation();
	}

	public Location getLocation(){
		return loc;
	}

	public int getId(){
		return id;
	}

	public int getRange(){
		return range;
	}

	public ArrayList<SpawnTemplate> getSpawns(){
		return spawns;
	}
	
	public boolean isBlockShown(){
		return shown;
	}

	public void setVisible(boolean show){
		Block blk = loc.getBlock();
		if (show) blk.setType(Material.MOB_SPAWNER);
		else blk.setType(Material.AIR);
		shown = show;
	}

	public boolean isInChunk(Chunk chunk){
		return chunk.equals(loc.getChunk());
	}

}
