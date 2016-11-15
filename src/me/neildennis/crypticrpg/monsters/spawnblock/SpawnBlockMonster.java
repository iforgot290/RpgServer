package me.neildennis.crypticrpg.monsters.spawnblock;

import org.bukkit.Location;

import me.neildennis.crypticrpg.monsters.SpawnType;
import me.neildennis.crypticrpg.monsters.generator.MonsterGenerator;
import me.neildennis.crypticrpg.monsters.type.CrypticMonster;
import me.neildennis.crypticrpg.utils.Log;

public class SpawnBlockMonster {
	
	private MonsterGenerator gen;
	private CrypticMonster mob;
	private long respawnDelay;
	private long lastDeath;
	
	public SpawnBlockMonster(MonsterGenerator gen, long respawnDelay){
		this.gen = gen;
		this.respawnDelay = respawnDelay;
		this.lastDeath = 0;
	}
	
	public boolean shouldSpawn(){
		if (System.currentTimeMillis() - lastDeath > respawnDelay)
			if (mob == null || mob.getEntity() == null || mob.getEntity().isDead()) return true;
		
		return false;
	}
	
	public void spawn(Location loc){
		try {
			mob = gen.generate();
			mob.spawn(loc, SpawnType.SPAWNBLOCK);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public void setLastDeath(){
		mob = null;
		lastDeath = System.currentTimeMillis();
	}
	
	public void setRespawnDelay(long delay){
		this.respawnDelay = delay;
	}
	
	public long getRespawnDelay(){
		return respawnDelay;
	}
	
	public MonsterGenerator getMonsterGen(){
		return gen;
	}
	
	public CrypticMonster getMonster(){
		return mob;
	}

}
