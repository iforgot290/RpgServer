package me.neildennis.crypticrpg.monsters;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.google.gson.JsonObject;

import me.neildennis.crypticrpg.monsters.templates.SpawnTemplate;

public class SpawnBlock {
	
	private static List<Block> shown = new ArrayList<Block>();
	
	private Location loc;
	private int id;
	private int range;
	private ArrayList<SpawnTemplate> spawns;
	
	private int minlvl;
	private int maxlvl;
	
	public SpawnBlock(int id, Location loc, int range, JsonObject json){
		this.id = id;
		this.loc = loc;
		this.range = range;
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
	
	public ArrayList<SpawnTemplate> getSpawnTemplates(){
		return spawns;
	}
	
	public void setVisible(boolean show){
		Block blk = loc.getBlock();
		if (show){
			if (shown.contains(blk)) shown.remove(blk);
			
			blk.setType(Material.MOB_SPAWNER);
			shown.add(blk);
		} else {
			if (shown.contains(blk)) shown.remove(blk);
			
			blk.setType(Material.AIR);
		}
	}
	
	public boolean isInChunk(Chunk chunk){
		return chunk.equals(loc.getChunk());
	}
	
	public void tickSpawns(){
		if (!loc.getChunk().isLoaded()) return;
	}

}
