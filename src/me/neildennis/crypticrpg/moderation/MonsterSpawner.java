package me.neildennis.crypticrpg.moderation;

import java.util.ArrayList;

import org.bukkit.Location;

import me.neildennis.crypticrpg.monsters.MonsterContainer;

public abstract class MonsterSpawner {
	
	protected ArrayList<MonsterContainer> alive = new ArrayList<MonsterContainer>();
	protected ArrayList<MonsterContainer> dead = new ArrayList<MonsterContainer>();
	protected ArrayList<MonsterContainer> spawned = new ArrayList<MonsterContainer>();
	
	protected boolean enabled = true;
	
	public abstract Location generateLocation();
	public abstract void tickSpawns();
	
	public void setDead(MonsterContainer container){
		alive.remove(container);
		dead.add(container);
	}
	
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
	
	public boolean isEnabled(){
		return enabled;
	}
	
	public void addMob(MonsterContainer container){
		dead.add(container);
	}
	
	public ArrayList<MonsterContainer> getAliveSpawns(){
		return alive;
	}
	
	public ArrayList<MonsterContainer> getDeadSpawns(){
		return dead;
	}
	
	public ArrayList<MonsterContainer> getAllSpawns(){
		ArrayList<MonsterContainer> all = new ArrayList<MonsterContainer>();
		all.addAll(alive);
		all.addAll(dead);
		
		return all;
	}
	
	

}
