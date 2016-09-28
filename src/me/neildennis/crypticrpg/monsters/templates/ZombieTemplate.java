package me.neildennis.crypticrpg.monsters.templates;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Zombie;

import me.neildennis.crypticrpg.items.ItemManager;
import me.neildennis.crypticrpg.items.attribs.Rarity;
import me.neildennis.crypticrpg.items.generator.ItemGenerator;
import me.neildennis.crypticrpg.items.type.CrypticGear;
import me.neildennis.crypticrpg.items.type.CrypticItemType;
import me.neildennis.crypticrpg.items.type.armor.CrypticArmor;
import me.neildennis.crypticrpg.monsters.MobType;

public class ZombieTemplate extends LivingTemplate {
	
	public ZombieTemplate(){
		super();
		type = MobType.ZOMBIE;
	}
	
	public ZombieTemplate(String name, boolean elite, int respawn){
		super(MobType.ZOMBIE, name, elite, respawn);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void spawnMob(Location loc, int level) {
		super.spawnMob(loc, level);
		
		List<ItemGenerator> generated = ItemManager.generateMobArmor(level);
		
		for (ItemGenerator gen : generated){
			gear.add((CrypticArmor) gen.generate());
		}
		
		Zombie zomb = (Zombie)ent;
		zomb.setBaby(false);
		zomb.setVillager(false);
		weapon = (CrypticGear) new ItemGenerator(CrypticItemType.SWORD).setRarity(Rarity.UNIQUE).generate();
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
