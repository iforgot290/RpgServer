package me.neildennis.crypticrpg.monsters;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.neildennis.crypticrpg.cloud.Cloud;
import me.neildennis.crypticrpg.cloud.ConnectionPool;
import me.neildennis.crypticrpg.monsters.templates.SpawnTemplate;
import me.neildennis.crypticrpg.utils.Log;
import me.neildennis.crypticrpg.utils.Utils;

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
		
		if (loc.getBlock().getType() == Material.MOB_SPAWNER) shown = true;
		else loc.getBlock().setType(Material.AIR);
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
		for (int y = spawnery + range; y > spawnery - range; y--){
			spawnloc.setY(y);
			if (spawnloc.getBlock().getType() == Material.AIR && spawnloc.add(0, -1, 0).getBlock().getType() != Material.AIR)
				return spawnloc.add(0, 1, 0);
		}

		return getRandomLocation();
	}
	
	public void save(){
		String loc = Utils.getStringFromLoc(this.loc);
		JsonArray array = new JsonArray();
		synchronized (spawns){
			for (SpawnTemplate temp : spawns){
				array.add(temp.saveToJson());
			}
		}
		if (id == 0){
			String savequery = "INSERT INTO monster_spawns(location, spawn_range, minlvl, maxlvl, monsters) VALUES('" +
					loc + "', '" + range + "', '" + minlvl + "', '" + maxlvl + "', '" + array.toString() + "')";
			try {
				PreparedStatement state = ConnectionPool.getConnection().prepareStatement(savequery, new String[]{"spawner_id"});
				state.executeUpdate();
				ResultSet res = state.getGeneratedKeys();
				if (res.next()){
					id = res.getInt(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			
		}
	}
	
	public void addMob(SpawnTemplate template){
		spawns.add(template);
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
