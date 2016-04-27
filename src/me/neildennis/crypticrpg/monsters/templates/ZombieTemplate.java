package me.neildennis.crypticrpg.monsters.templates;

import java.util.ArrayList;

import org.bukkit.Location;

import me.neildennis.crypticrpg.items.ItemManager;
import me.neildennis.crypticrpg.items.generator.ItemGenerator;
import me.neildennis.crypticrpg.items.metadata.ItemType;
import me.neildennis.crypticrpg.items.metadata.Rarity;

public class ZombieTemplate extends SpawnTemplate {
	
	public ZombieTemplate(){
		super();
	}

	@Override
	public void spawnMob(Location loc, int level) {
		super.spawnMob(loc, level);
		
		ArrayList<ItemGenerator> generated = ItemManager.generateMobArmor(level);
		
		for (ItemGenerator gen : generated){
			gear.add(gen.generate());
		}
		
		gear.add(new ItemGenerator().setType(ItemType.SWORD).setLevel(level).setRarity(Rarity.UNCOMMON).generate());
	}
	
	@Override
	public String getTierPrefix(int tier){
		switch (tier){
		case 1: return "T1";
		case 2: return "T2";
		case 3: return "T3";
		case 4: return "T4";
		case 5: return "T5";
		default: return "default";
		}
	}

}
