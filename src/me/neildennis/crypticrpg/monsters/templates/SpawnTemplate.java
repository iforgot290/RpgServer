package me.neildennis.crypticrpg.monsters.templates;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.google.gson.JsonObject;

import me.neildennis.crypticrpg.items.ItemManager;
import me.neildennis.crypticrpg.items.custom.CrypticGear;
import me.neildennis.crypticrpg.monsters.MobType;

public abstract class SpawnTemplate {
	
	protected MobType type;
	protected String name;
	protected int tier;
	protected int level;
	protected boolean elite;
	
	protected Entity ent;
	protected long lastdeath = 0L;
	protected long respawn;
	protected int health = 50;
	protected int maxhealth = 50;
	protected List<CrypticGear> gear;
	
	protected SpawnTemplate(){
		gear = new ArrayList<CrypticGear>();
	}
	
	protected SpawnTemplate(JsonObject obj){
		this();
		loadFromJson(obj);
	}
	
	public void spawnMob(Location loc, int level){
		this.level = level;
		ent = loc.getWorld().spawnEntity(loc, type.getEntityType());
		
		ent.setCustomName(ItemManager.getTierColor(tier) + type.getDefaultName());
		ent.setCustomNameVisible(true);
		
		if (ent instanceof LivingEntity){
			LivingEntity le = (LivingEntity)ent;
		}
	}
	
	public void loadFromJson(JsonObject obj){
		type = MobType.valueOf(obj.get("type").getAsString());
		tier = obj.get("tier").getAsInt();
		name = obj.has("name") ? obj.get("name").getAsString() : type.getDefaultName();
		elite = obj.get("elite").getAsBoolean();
	}
	
	public void setLastDeath(){
		lastdeath = System.currentTimeMillis();
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
	
	public long getLastDeath(){
		return lastdeath;
	}
	
	public boolean shouldSpawn(){
		if (isAlive()) return false;
		if (System.currentTimeMillis() - lastdeath < respawn) return false;
		return true;
	}
	
	public Entity getEntity(){
		return ent;
	}
	
	public boolean isAlive(){
		if (ent == null) return false;
		if (health <= 0) return false;
		if (ent.isDead()) return false;
		return true;
	}

}
