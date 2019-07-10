package me.neildennis.crypticrpg.monsters;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import me.neildennis.crypticrpg.monsters.type.CrypticMonster;
import me.neildennis.crypticrpg.monsters.type.CrypticZombie;

public enum MobType {
	
	ZOMBIE(CrypticZombie.class, EntityType.ZOMBIE, "Zombie", Material.ZOMBIE_HEAD);
	
	private String name;
	private Class<? extends CrypticMonster> c;
	private EntityType type;
	private Material icon;
	
	MobType(Class<? extends CrypticMonster> c, EntityType type, String name, Material icon){
		this.c = c;
		this.type = type;
		this.name = name;
		this.icon = icon;
	}
	
	public Material getIcon() {
		return icon;
	}
	
	public String getDefaultName(){
		return name;
	}
	
	public EntityType getEntityType(){
		return type;
	}
	
	public Class<? extends CrypticMonster> getHandleClass(){
		return c;
	}

}
