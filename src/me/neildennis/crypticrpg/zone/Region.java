package me.neildennis.crypticrpg.zone;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class Region {
	
	private ProtectedRegion region;
	private YamlConfiguration config;
	
	private boolean town;
	private String announce;
	private String subtitle;
	
	public Region(YamlConfiguration config, ProtectedRegion region){
		this.config = config;
		this.region = region;
		
		announce = config.getString("announce");
		subtitle = config.getString("subtitle");
		town = announce != null;
		
		if (town)
			announce = ChatColor.translateAlternateColorCodes('&', announce);
		if (subtitle != null)
			subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
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

}
