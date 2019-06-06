package me.neildennis.crypticrpg.monsters.spawnblock;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.neildennis.crypticrpg.items.attribs.Rarity;
import me.neildennis.crypticrpg.items.type.CrypticItemType;
import me.neildennis.crypticrpg.moderation.MonsterSpawner;
import me.neildennis.crypticrpg.monsters.MobManager;
import me.neildennis.crypticrpg.monsters.MobType;
import me.neildennis.crypticrpg.monsters.MonsterContainer;
import me.neildennis.crypticrpg.monsters.SpawnType;
import me.neildennis.crypticrpg.monsters.generator.MonsterGenerator;
import me.neildennis.crypticrpg.utils.Log;
import me.neildennis.crypticrpg.utils.Utils;

public class SpawnBlock extends MonsterSpawner{

	private Location loc;
	private UUID id;
	private int range;
	private boolean shown = false;

	public SpawnBlock(UUID id, Location loc, int range){
		this.id = id;
		this.loc = loc;
		this.range = range;

		if (loc.getBlock().getType() == Material.SPAWNER) shown = true; 
		else loc.getBlock().setType(Material.AIR);
	}

	public SpawnBlock(UUID id, Location loc, int range, JsonArray array){
		this(id, loc, range);

		for (JsonElement ele : array){
			if (!ele.isJsonObject()) continue;
			JsonObject obj = (JsonObject) ele;
			
			MobType type = MobType.valueOf(obj.get("type").getAsString());
			MonsterGenerator gen = new MonsterGenerator(type);
			
			if (obj.has("elite"))
				gen.setElite(obj.get("elite").getAsBoolean());
			if (obj.has("armordrop"))
				gen.setArmorDropChance(obj.get("armordrop").getAsFloat());
			if (obj.has("rarity"))
				gen.setGearRarity(Rarity.valueOf(obj.get("rarity").getAsString()));
			if (obj.has("weapontype"))
				gen.setWeaponType(CrypticItemType.valueOf(obj.get("weapontype").getAsString()));
			if (obj.has("name"))
				gen.setName(obj.get("name").getAsString());
			
			gen.setLvlRange(obj.get("minlvl").getAsInt(), obj.get("maxlvl").getAsInt());
			
			dead.add(new MonsterContainer(gen, obj.get("delay").getAsLong(), SpawnType.SPAWNBLOCK, this));
		}
	}

	public SpawnBlock(UUID id, Location loc, int range, ArrayList<SpawnBlockMonster> spawns){
		this(id, loc, range);

		this.dead.addAll(spawns);
	}

	public void tickSpawns(){
		if (!enabled) return;
		if (!loc.getChunk().isLoaded()) return;
		if (!Utils.isPlayerNear(loc)) return;

		for (MonsterContainer spawn : dead){
			if (!spawn.shouldSpawn()) continue;
			spawn.spawn(generateLocation());
			Log.debug("spawn success");
			MobManager.registerMonster(spawn);
			spawned.add(spawn);
		}
		
		for (MonsterContainer spawn : spawned){
			alive.add(spawn);
			dead.remove(spawn);
		}
		
		spawned.clear();
	}

	public Location generateLocation(){
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

		return generateLocation();
	}

	public Location getLocation(){
		return loc;
	}

	public UUID getId(){
		return id;
	}

	public int getRange(){
		return range;
	}
	
	public void setRange(int range){
		this.range = range;
	}

	public boolean isBlockShown(){
		return shown;
	}

	public void setVisible(boolean show){
		Block blk = loc.getBlock();
		if (show) blk.setType(Material.SPAWNER);
		else blk.setType(Material.AIR);
		shown = show;
	}

	public boolean isInChunk(Chunk chunk){
		return chunk.equals(loc.getChunk());
	}

}
