package me.neildennis.crypticrpg.monsters.type;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import me.neildennis.crypticrpg.items.attribs.AttributeType;
import me.neildennis.crypticrpg.items.attribs.Tier;
import me.neildennis.crypticrpg.items.type.CrypticItem;
import me.neildennis.crypticrpg.items.type.armor.CrypticBoots;
import me.neildennis.crypticrpg.items.type.armor.CrypticChestplate;
import me.neildennis.crypticrpg.items.type.armor.CrypticHelmet;
import me.neildennis.crypticrpg.items.type.armor.CrypticLeggings;
import me.neildennis.crypticrpg.items.type.weapon.CrypticWeapon;
import me.neildennis.crypticrpg.monsters.MobType;
import me.neildennis.crypticrpg.monsters.MonsterCachedStats;
import me.neildennis.crypticrpg.monsters.SpawnType;

public abstract class CrypticMonster {
	
	protected Entity entity;
	
	protected MobType type;
	protected String name;
	
	protected boolean elite;
	protected int level;
	protected Tier tier;
	
	protected boolean willDrop;
	protected List<CrypticItem> drops;
	
	protected CrypticHelmet helm;
	protected CrypticChestplate chest;
	protected CrypticLeggings legs;
	protected CrypticBoots boots;
	protected CrypticWeapon weapon;
	
	protected MonsterCachedStats stats;
	
	protected double health;
	protected double maxhealth;
	
	protected SpawnType spawnType;
	
	public CrypticMonster(){
		
	}
	
	public Entity spawn(Location loc, SpawnType spawnType){
		this.spawnType = spawnType;
		entity = loc.getWorld().spawnEntity(loc, type.getEntityType());
		
		stats = new MonsterCachedStats(this);
		maxhealth = stats.getAttribute(AttributeType.HEALTH).genValue();
		health = maxhealth;
		
		updateNameplate();
		entity.setCustomNameVisible(true);
		
		return entity;
	}
	
	public MonsterCachedStats getStats(){
		return stats;
	}
	
	public void updateNameplate(){
		getEntity().setCustomName(ChatColor.GREEN + "[" + level + "] [" + (int) getHealth() + "/" + (int) getMaxHealth() + "] " + tier.getColor() + name);
	}
	
	public double getHealth(){
		return health;
	}
	
	public void setHealth(double health){
		this.health = health;
	}
	
	public double getMaxHealth(){
		return maxhealth;
	}
	
	public void setMaxHealth(double maxhealth){
		this.maxhealth = maxhealth;
	}
	
	public Entity getEntity(){
		return entity;
	}
	
	public boolean isElite(){
		return elite;
	}
	
	public void setElite(boolean elite){
		this.elite = elite;
	}
	
	public int getLevel(){
		return level;
	}
	
	public void setLevel(int level){
		this.level = level;
		this.tier = Tier.fromLevel(level);
	}
	
	public Tier getTier(){
		return tier;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public boolean willDropGear(){
		return willDrop;
	}
	
	public void setWillDrop(boolean willDrop){
		this.willDrop = willDrop;
	}
	
	public void setHelmet(CrypticHelmet helm){
		this.helm = helm;
	}
	
	public CrypticHelmet getHelmet(){
		return helm;
	}
	
	public void setChestplate(CrypticChestplate chest){
		this.chest = chest;
	}
	
	public CrypticChestplate getChestplate(){
		return chest;
	}
	
	public void setLeggings(CrypticLeggings legs){
		this.legs = legs;
	}
	
	public CrypticLeggings getLeggings(){
		return legs;
	}
	
	public void setBoots(CrypticBoots boots){
		this.boots = boots;
	}
	
	public CrypticBoots getBoots(){
		return boots;
	}
	
	public void setWeapon(CrypticWeapon weapon){
		this.weapon = weapon;
	}
	
	public CrypticWeapon getWeapon(){
		return weapon;
	}
	
	public void setSpawnType(SpawnType spawnType){
		this.spawnType = spawnType;
	}
	
	public SpawnType getSpawnType(){
		return spawnType;
	}

}
