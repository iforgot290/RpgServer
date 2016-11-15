package me.neildennis.crypticrpg.monsters;

import org.bukkit.entity.EntityType;

import me.neildennis.crypticrpg.monsters.type.CrypticMonster;
import me.neildennis.crypticrpg.monsters.type.CrypticZombie;

public enum MobType {
	
	ZOMBIE(CrypticZombie.class, EntityType.ZOMBIE, "Zombie");
	
	private String name;
	private Class<? extends CrypticMonster> c;
	private EntityType type;
	
	MobType(Class<? extends CrypticMonster> c, EntityType type, String name){
		this.c = c;
		this.type = type;
		this.name = name;
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
