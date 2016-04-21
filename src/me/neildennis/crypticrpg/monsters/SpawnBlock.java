package me.neildennis.crypticrpg.monsters;

import java.util.ArrayList;

import org.bukkit.Location;
import com.google.gson.JsonObject;

import me.neildennis.crypticrpg.monsters.templates.SpawnTemplate;

public class SpawnBlock {
	
	private Location loc;
	private int id;
	private int range;
	private ArrayList<SpawnTemplate> spawns;
	
	public SpawnBlock(int id, Location loc, int range, JsonObject json){
		
	}

}
