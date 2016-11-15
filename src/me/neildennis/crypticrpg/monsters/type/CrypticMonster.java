package me.neildennis.crypticrpg.monsters.type;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import me.neildennis.crypticrpg.items.attribs.Tier;
import me.neildennis.crypticrpg.monsters.MobType;

public abstract class CrypticMonster {
	
	protected Entity entity;
	
	protected MobType type;
	protected boolean elite;
	protected int level;
	protected Tier tier;
	protected String name;
	
	public CrypticMonster(MobType type, String name, int level, boolean elite){
		this.type = type;
		this.name = name;
		this.level = level;
		this.tier = Tier.fromLevel(level);
		this.elite = elite;
	}
	
	public Entity spawn(Location loc){
		entity = loc.getWorld().spawnEntity(loc, type.getEntityType());
		
		entity.setCustomName(getNameplate());
		entity.setCustomNameVisible(true);
		
		return entity;
	}
	
	public String getNameplate(){
		return ChatColor.GREEN + "[" + level + "] " + tier.getColor() + name;
	}
	
	public Entity getEntity(){
		return entity;
	}
	
	public boolean isElite(){
		return elite;
	}
	
	public int getLevel(){
		return level;
	}
	
	public Tier getTier(){
		return tier;
	}
	
	public String getName(){
		return name;
	}

}
