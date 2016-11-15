package me.neildennis.crypticrpg.monsters.type;

import org.bukkit.Location;
import org.bukkit.entity.Zombie;

import me.neildennis.crypticrpg.monsters.MobType;
import me.neildennis.crypticrpg.monsters.SpawnType;

public class CrypticZombie extends LivingMonster{
	
	private Zombie zombie;
	
	public CrypticZombie(){
		this.type = MobType.ZOMBIE;
	}
	
	@Override
	public Zombie spawn(Location loc, SpawnType spawnType){
		zombie = (Zombie) super.spawn(loc, spawnType);
		zombie.setBaby(false);
		
		return zombie;
	}
	
	@Override
	public Zombie getEntity(){
		return zombie;
	}

}
