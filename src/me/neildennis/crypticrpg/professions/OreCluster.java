package me.neildennis.crypticrpg.professions;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;

import me.neildennis.crypticrpg.items.attribs.Tier;

public class OreCluster {
	
	private Tier tier;
	private UUID id;
	private Location loc;
	private Material destroyed;
	
	private long respawn = 0;
	
	public OreCluster(UUID id, Tier tier, Location loc, Material destroyed) {
		this.id = id;
		this.tier = tier;
		this.loc = loc;
		this.destroyed = destroyed;
	}
	
	public void spawn() {
		loc.getBlock().setType(tier.getOreType());
	}
	
	public void despawn() {
		loc.getBlock().setType(destroyed);
	}
	
	public boolean isSpawned() {
		return loc.getBlock().getType() != destroyed;
	}
	
	public UUID getUUID() {
		return id;
	}
	
	public Location getLocation() {
		return loc;
	}
	
	public Tier getTier() {
		return tier;
	}
	
	public void setTier(Tier tier) {
		this.tier = tier;
	}
	
	public Material getOreType() {
		return tier.getOreType();
	}
	
	public Material getDestroyedType() {
		return destroyed;
	}
	
	public void setRespawnTime(long respawn) {
		this.respawn = respawn;
	}
	
	public long getRespawnTime() {
		return respawn;
	}

}
