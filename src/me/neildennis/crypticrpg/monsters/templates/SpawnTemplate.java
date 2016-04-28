package me.neildennis.crypticrpg.monsters.templates;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import com.google.gson.JsonObject;

import me.neildennis.crypticrpg.items.custom.CrypticGear;
import me.neildennis.crypticrpg.items.custom.CrypticWeapon;
import me.neildennis.crypticrpg.monsters.MobType;
import me.neildennis.crypticrpg.utils.Utils;
import net.md_5.bungee.api.ChatColor;

public abstract class SpawnTemplate {

	protected MobType type;
	protected String name;
	protected int tier;
	protected int level;
	protected boolean elite;

	protected Entity ent;
	protected long lastdeath = 0L;
	protected long lasthit = 0L;
	protected long respawn;

	protected List<CrypticGear> gear;
	protected CrypticWeapon weapon;
	
	protected boolean dead = true;

	protected SpawnTemplate(){
		gear = new ArrayList<CrypticGear>();
	}
	
	protected SpawnTemplate(MobType type, String name, boolean elite, int respawn){
		this();
		this.type = type;
		this.name = name;
		this.elite = elite;
		this.respawn = respawn;
	}

	protected SpawnTemplate(JsonObject obj){
		this();
		loadFromJson(obj);
	}
	
	public abstract boolean isAlive();
	public abstract void applyGear();
	public abstract void updateBar();

	public void spawnMob(Location loc, int level){
		this.level = level;
		this.tier = Utils.getTierFromLevel(level);
		this.dead = false;
		gear.clear();
		ent = loc.getWorld().spawnEntity(loc, type.getEntityType());
	}

	public abstract String getTierPrefix(int tier);

	public void loadFromJson(JsonObject obj){
		type = MobType.valueOf(obj.get("type").getAsString());
		name = obj.has("name") ? obj.get("name").getAsString() : type.getDefaultName();
		elite = obj.get("elite").getAsBoolean();
		respawn = obj.get("respawn").getAsInt();
	}
	
	public JsonObject saveToJson(){
		JsonObject obj = new JsonObject();
		
		obj.addProperty("type", type.name());
		obj.addProperty("name", name);
		obj.addProperty("elite", elite);
		obj.addProperty("respawn", respawn);
		
		return obj;
	}

	public void setLastDeath(){
		dead = true;
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
	
	public CrypticWeapon getWeapon(){
		return weapon;
	}
	
	public void hit(){
		lasthit = System.currentTimeMillis();
	}

}
