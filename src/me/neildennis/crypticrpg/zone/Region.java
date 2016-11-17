package me.neildennis.crypticrpg.zone;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.neildennis.crypticrpg.moderation.MonsterSpawner;
import me.neildennis.crypticrpg.monsters.MonsterContainer;
import me.neildennis.crypticrpg.monsters.SpawnType;
import me.neildennis.crypticrpg.monsters.generator.MonsterGenerator;

public class Region extends MonsterSpawner{
	
	private ProtectedPolygonalRegion region;
	private YamlConfiguration config;
	
	private boolean town;
	private String announce;
	private String subtitle;
	
	public Region(YamlConfiguration config, ProtectedRegion region){
		this.config = config;
		this.region = (ProtectedPolygonalRegion) region;
		
		announce = config.getString("announce");
		subtitle = config.getString("subtitle");
		town = announce != null;
		
		if (town)
			announce = ChatColor.translateAlternateColorCodes('&', announce);
		if (subtitle != null)
			subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
	}
	
	@Override
	public void tickSpawns(){
		for (MonsterContainer spawn : dead){
			if (spawn.shouldSpawn()){
				spawn.spawn(generateLocation());
				spawned.add(spawn);
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
		return null;
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
