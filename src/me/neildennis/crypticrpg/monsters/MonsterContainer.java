package me.neildennis.crypticrpg.monsters;

import org.bukkit.Location;

import me.neildennis.crypticrpg.moderation.MonsterSpawner;
import me.neildennis.crypticrpg.monsters.generator.MonsterGenerator;
import me.neildennis.crypticrpg.monsters.type.CrypticMonster;

public class MonsterContainer {
	
	protected MonsterGenerator gen;
	protected CrypticMonster mob;
	protected MonsterSpawner spawner;
	protected long respawnDelay;
	protected long lastDeath;
	protected SpawnType type;
	
	public MonsterContainer(MonsterGenerator gen, long respawnDelay, SpawnType type, MonsterSpawner spawner){
		this.gen = gen;
		this.spawner = spawner;
		this.respawnDelay = respawnDelay;
		this.lastDeath = 0;
	}
	
	public void setDeath(){
		mob = null;
		lastDeath = System.currentTimeMillis();
		spawner.setDead(this);
	}
	
	public boolean shouldSpawn(){
		if (System.currentTimeMillis() - lastDeath > respawnDelay)
			if (mob == null || mob.getEntity() == null || mob.getEntity().isDead()) return true;
		
		return false;
	}
	
	public void spawn(Location loc){
		try {
			mob = gen.generate();
			mob.spawn(loc, type);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public SpawnType getSpawnType(){
		return type;
	}
	
	public void setRespawnDelay(long delay){
		this.respawnDelay = delay;
	}
	
	public long getRespawnDelay(){
		return respawnDelay;
	}
	
	public void setMonsterGen(MonsterGenerator gen){
		this.gen = gen;
	}
	
	public MonsterGenerator getMonsterGen(){
		return gen;
	}
	
	public CrypticMonster getMonster(){
		return mob;
	}

}
