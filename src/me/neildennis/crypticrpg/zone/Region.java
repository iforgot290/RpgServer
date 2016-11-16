package me.neildennis.crypticrpg.zone;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Region {
	
	private ProtectedPolygonalRegion region;
	private YamlConfiguration config;
	
	private boolean town;
	private String announce;
	private String subtitle;
	
	private List<RegionSpawn> alive;
	private List<RegionSpawn> dead;
	private List<RegionSpawn> spawned;
	
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
	
	public void tickSpawns(){
		for (RegionSpawn spawn : dead){
			if (spawn.shouldSpawn()){
				spawn.spawn();
				spawned.add(spawn);
			}
		}
		
		for (RegionSpawn spawn : spawned){
			dead.remove(spawn);
			alive.add(spawn);
		}
		
		spawned.clear();
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
	
	public class RegionSpawn{
		
		
		private float pct;
		
		public RegionSpawn(){
			
		}
		
		public boolean shouldSpawn(){
			return false;
		}
		
		public void spawn(){
			
		}
		
	}

}
