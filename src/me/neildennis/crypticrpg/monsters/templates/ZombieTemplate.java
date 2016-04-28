package me.neildennis.crypticrpg.monsters.templates;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Zombie;

import me.neildennis.crypticrpg.items.ItemManager;
import me.neildennis.crypticrpg.items.custom.CrypticWeapon;
import me.neildennis.crypticrpg.items.generator.ItemGenerator;
import me.neildennis.crypticrpg.items.metadata.ItemType;
import me.neildennis.crypticrpg.items.metadata.Rarity;
import me.neildennis.crypticrpg.monsters.MobType;

public class ZombieTemplate extends LivingTemplate {
	
	public ZombieTemplate(){
		super();
	}
	
	public ZombieTemplate(String name, boolean elite, int respawn){
		super(MobType.ZOMBIE, name, elite, respawn);
	}

	@Override
	public void spawnMob(Location loc, int level) {
		super.spawnMob(loc, level);
		
		ArrayList<ItemGenerator> generated = ItemManager.generateMobArmor(level);
		
		for (ItemGenerator gen : generated){
			gear.add(gen.generate());
		}
		
		Zombie zomb = (Zombie)ent;
		zomb.setBaby(false);
		zomb.setVillagerProfession(null);
		weapon = (CrypticWeapon) new ItemGenerator().setType(ItemType.SWORD).setLevel(level).setRarity(Rarity.UNIQUE).generate();
	}
	
	@Override
	public String getTierPrefix(int tier){
		switch (tier){
		case 1: return "Rotten ";
		case 2: return "T2 ";
		case 3: return "T3 ";
		case 4: return "T4 ";
		case 5: return "T5 ";
		default: return "default ";
		}
	}

}
