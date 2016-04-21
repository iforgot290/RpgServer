package me.neildennis.crypticrpg.monsters;

import me.neildennis.crypticrpg.monsters.templates.SpawnTemplate;
import me.neildennis.crypticrpg.monsters.templates.ZombieTemplate;

public enum MobType {
	
	ZOMBIE(ZombieTemplate.class, "Zombie");
	
	private String name;
	private Class<? extends SpawnTemplate> c;
	
	MobType(Class<? extends SpawnTemplate> c, String name){
		this.c = c;
		this.name = name;
	}
	
	public String getDefaultName(){
		return name;
	}
	
	public Class<? extends SpawnTemplate> getHandleClass(){
		return c;
	}

}
