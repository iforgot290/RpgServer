package me.neildennis.crypticrpg.monsters;

import org.bukkit.entity.EntityType;

import me.neildennis.crypticrpg.monsters.templates.SpawnTemplate;
import me.neildennis.crypticrpg.monsters.templates.ZombieTemplate;

public enum MobType {
	
	ZOMBIE(ZombieTemplate.class, EntityType.ZOMBIE, "Zombie");
	
	private String name;
	private Class<? extends SpawnTemplate> c;
	private EntityType type;
	
	MobType(Class<? extends SpawnTemplate> c, EntityType type, String name){
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
	
	public Class<? extends SpawnTemplate> getHandleClass(){
		return c;
	}

}
