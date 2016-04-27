package me.neildennis.crypticrpg.monsters.templates;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import com.google.gson.JsonObject;

import me.neildennis.crypticrpg.items.ItemManager;
import me.neildennis.crypticrpg.items.custom.CrypticGear;
import me.neildennis.crypticrpg.items.custom.CrypticWeapon;
import me.neildennis.crypticrpg.items.metadata.ItemType;
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
	protected CrypticWeapon weapon;
	
	protected boolean dead = true;

	protected SpawnTemplate(){
		gear = new ArrayList<CrypticGear>();
	}

	protected SpawnTemplate(JsonObject obj){
		this();
		loadFromJson(obj);
	}

	public void spawnMob(Location loc, int level){
		this.level = level;
		this.dead = false;
		gear.clear();
		ent = loc.getWorld().spawnEntity(loc, type.getEntityType());
		setName();
	}

	public void applyGear(){
		if (ent instanceof LivingEntity && gear != null && !gear.isEmpty()){
			LivingEntity le = (LivingEntity)ent;
			for (CrypticGear gear : this.gear){
				if (gear.getType() == ItemType.HELMET) le.getEquipment().setHelmet(gear.getItemStack());
				else if (gear.getType() == ItemType.CHESTPLATE) le.getEquipment().setChestplate(gear.getItemStack());
				else if (gear.getType() == ItemType.LEGGINGS) le.getEquipment().setLeggings(gear.getItemStack());
				else if (gear.getType() == ItemType.BOOTS) le.getEquipment().setBoots(gear.getItemStack());
			}

			if (weapon != null)
				le.getEquipment().setItemInMainHand(weapon.getItemStack());
		}
	}

	public abstract String getTierPrefix(int tier);

	public void setName(){
		String name = getTierPrefix(tier) != null ? getTierPrefix(tier) + " " + type.getDefaultName() : type.getDefaultName();
		setName(name);
	}

	public void setName(String str){
		ent.setCustomName(ItemManager.getTierColor(tier) + str);
		ent.setCustomNameVisible(true);
	}

	public void loadFromJson(JsonObject obj){
		type = MobType.valueOf(obj.get("type").getAsString());
		tier = obj.get("tier").getAsInt();
		name = obj.has("name") ? obj.get("name").getAsString() : type.getDefaultName();
		elite = obj.get("elite").getAsBoolean();
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
	
	public void checkDead(){
		if (ent == null) dead = true;
		if (dead) return;
		if (ent.isDead() || health <= 0) setLastDeath();
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
		checkDead();
		return !dead;
	}

}
