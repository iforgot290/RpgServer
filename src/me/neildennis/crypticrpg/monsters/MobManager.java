package me.neildennis.crypticrpg.monsters;

import java.util.ArrayList;

import me.neildennis.crypticrpg.Manager;

public class MobManager extends Manager{
	
	private ArrayList<SpawnBlock> spawners;
	
	public MobManager(){
		spawners = new ArrayList<SpawnBlock>();
	}
	
	@Override
	public void registerTasks(){
		
	}
	
	public void loadMobSpawns(){
		
	}

}
