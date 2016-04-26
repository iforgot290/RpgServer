package me.neildennis.crypticrpg.monsters.templates;

import org.bukkit.Location;

import com.google.gson.JsonObject;

import me.neildennis.crypticrpg.monsters.MobType;

public abstract class SpawnTemplate {
	
	protected MobType type;
	protected String name;
	protected int tier;
	protected int level;
	protected boolean elite;
	
	protected SpawnTemplate(){
		
	}
	
	protected SpawnTemplate(JsonObject obj){
		loadFromJson(obj);
	}
	
	public abstract void spawnMob(Location loc);
	
	public void loadFromJson(JsonObject obj){
		type = MobType.valueOf(obj.get("type").getAsString());
		tier = obj.get("tier").getAsInt();
		name = obj.has("name") ? obj.get("name").getAsString() : type.getDefaultName();
		level = obj.get("level").getAsInt();
		elite = obj.get("elite").getAsBoolean();
	}
	
	public MobType getType(){
		return type;
	}
	
	public String getName(){
		return name;
	}
	
	public int getTier(){
		return tier;
	}
	
	public int getLevel(){
		return level;
	}

}
