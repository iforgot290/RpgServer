package me.neildennis.crypticrpg.items.generator;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import me.neildennis.crypticrpg.items.metadata.ItemModifier;
import me.neildennis.crypticrpg.items.metadata.ItemType;
import me.neildennis.crypticrpg.items.metadata.Rarity;

public class ModifierGenerator {
	
	private ItemType type;
	private int lvl;
	private Rarity rarity;
	
	private List<ItemModifier> mods;
	
	public ModifierGenerator(ItemType type){
		this.type = type;
		mods = new ArrayList<ItemModifier>();
		lvl = 1;
		rarity = Rarity.COMMON;
	}
	
	public List<ItemModifier> generate(){
		for (ModifierTemplate temp : ModifierTemplate.getTemplates()){
			Bukkit.broadcastMessage("Looping through a template");
			if (temp.applies(type, rarity, lvl)){
				Bukkit.broadcastMessage("Should add template");
				mods.add(temp.getModifier(lvl));
			}
		}
		return mods;
	}
	
	public ModifierGenerator setType(ItemType type){
		this.type = type;
		return this;
	}
	
	public ModifierGenerator setLevel(int lvl){
		this.lvl = lvl;
		return this;
	}
	
	public ModifierGenerator setRarity(Rarity rarity){
		this.rarity = rarity;
		return this;
	}

}
