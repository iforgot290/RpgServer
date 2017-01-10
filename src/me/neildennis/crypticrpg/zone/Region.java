package me.neildennis.crypticrpg.zone;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.items.attribs.Rarity;
import me.neildennis.crypticrpg.items.type.CrypticItemType;
import me.neildennis.crypticrpg.moderation.MonsterSpawner;
import me.neildennis.crypticrpg.monsters.MobManager;
import me.neildennis.crypticrpg.monsters.MobType;
import me.neildennis.crypticrpg.monsters.MonsterContainer;
import me.neildennis.crypticrpg.monsters.SpawnType;
import me.neildennis.crypticrpg.monsters.generator.MonsterGenerator;
import me.neildennis.crypticrpg.utils.Utils;

public class Region extends MonsterSpawner{
	
	private ProtectedRegion region;
	
	private boolean town = false;
	private String announce;
	private String subtitle;
	
	public Region(ProtectedRegion region, JsonArray array){
		this.region = region;
		
		for (JsonElement ele : array){
			if (!ele.isJsonObject()) continue;
			JsonObject obj = (JsonObject) ele;
			
			int amount = obj.get("amount").getAsInt();
			long delay = obj.get("delay").getAsLong();
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
			
			for (int i = 0; i < amount; i++){
				dead.add(new RegionSpawn(gen, delay, this));
			}
		}
	}
	
	public Region(ProtectedRegion region, JsonArray array, String announce, String subtitle){
		this(region, array);
		
		this.town = true;
		this.announce = announce;
		this.subtitle = subtitle;
		
		if (announce != null)
			announce = ChatColor.translateAlternateColorCodes('&', announce);
		if (subtitle != null)
			subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
	}
	
	@Override
	public void tickSpawns(){
		if (!Utils.isPlayerNear(generateLocation())) return;
		
		for (MonsterContainer spawn : dead){
			if (spawn.shouldSpawn()){
				spawn.spawn(generateLocation());
				spawned.add(spawn);
				MobManager.registerMonster(spawn);
			}
		}
		
		for (MonsterContainer spawn : spawned){
			dead.remove(spawn);
			alive.add(spawn);
		}
		
		spawned.clear();
	}
	
	@Override
	public Location generateLocation(){
		BlockVector min = region.getMinimumPoint();
		BlockVector max = region.getMaximumPoint();
		Random rand = new Random();
		return generateLocation(rand, min, max, 0);
	}
	
	private Location generateLocation(Random rand, BlockVector min, BlockVector max, int attempt){
		if (attempt > 5) return null;
		
		int x = rand.nextInt(max.getBlockX() - min.getBlockX()) + min.getBlockX();
		int z = rand.nextInt(max.getBlockZ() - min.getBlockZ()) + min.getBlockZ();
		
		if (!region.contains(x, min.getBlockY(), z))
			return generateLocation(rand, min, max, attempt++);
		
		for (int y = min.getBlockY(); y < max.getBlockY(); y++){
			if (Cryptic.getMainWorld().getBlockAt(x, y, z).getType() == Material.AIR){
				return new Location(Cryptic.getMainWorld(), x, y, z);
			}
		}
		
		return generateLocation(rand, min, max, attempt++);
	}
	
	public ProtectedRegion getRegion(){
		return region;
	}
	
	public String getAnnounce(){
		return announce;
	}
	
	public String getSubtitle(){
		return subtitle;
	}
	
	public boolean isTown(){
		return town;
	}
	
	public class RegionSpawn extends MonsterContainer{
		
		public RegionSpawn(MonsterGenerator gen, long respawnDelay, Region spawner) {
			super(gen, respawnDelay, SpawnType.REGION, spawner);
		}
		
	}

}
