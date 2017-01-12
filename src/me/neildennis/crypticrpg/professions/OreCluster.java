package me.neildennis.crypticrpg.professions;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;

import me.neildennis.crypticrpg.items.attribs.Tier;
import me.neildennis.crypticrpg.zone.Region;

public class OreCluster {
	
	private ArrayList<Location> locs;
	private Tier tier;
	private Region region;
	private UUID id;
	
	public OreCluster(){
		locs = new ArrayList<Location>();
		tier = Tier.ONE;
		this.id = null;
	}
	
	public void spawn(){
		for (Location loc : locs) loc.getBlock().setType(tier.getOreType());
	}
	
	public void despawn(){
		for (Location loc : locs) loc.getBlock().setType(Material.AIR);
	}
	
	public boolean isSpawned(){
		for (Location loc : locs)
			if (loc.getBlock().getType() != Material.AIR)
				return true;
		return false;
	}
	
	public UUID getUUID(){
		return id;
	}
	
	public void setUUID(UUID id){
		this.id = id;
	}
	
	public void addLocation(Location loc){
		locs.add(loc);
	}
	
	public boolean containsLocation(Location loc){
		return locs.contains(loc);
	}
	
	public void removeLocation(Location loc){
		locs.remove(loc);
	}
	
	public Location getLocation(){
		if (locs == null || locs.size() == 0) return null;
		
		return locs.get(0);
	}
	
	public ArrayList<Location> getLocations(){
		return locs;
	}
	
	public Tier getTier(){
		return tier;
	}
	
	public void setTier(Tier tier){
		this.tier = tier;
	}
	
	public Material getOreType(){
		return tier.getOreType();
	}
	
	public Region getRegion(){
		return region;
	}
	
	public void setRegion(Region region){
		this.region = region;
	}

}
