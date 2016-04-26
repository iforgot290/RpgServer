package me.neildennis.crypticrpg.items.generator;

import java.util.ArrayList;
import java.util.List;

import me.neildennis.crypticrpg.items.metadata.ItemModifier;
import me.neildennis.crypticrpg.items.metadata.ItemType;
import me.neildennis.crypticrpg.items.metadata.Rarity;

public class ModifierGenerator {
	
	private ItemType type;
	private int lvl;
	
	private List<ItemModifier> mods;
	
	private Rarity rarity;
	
	public ModifierGenerator(){
		mods = new ArrayList<ItemModifier>();
		rarity = Rarity.COMMON;
	}
	
	public List<ItemModifier> generate(){
		for (ModifierTemplate temp : ModifierTemplate.getTemplates())
			if (temp.applies(type, lvl))
				mods.add(temp.getModifier());
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
	
	public Rarity getRarity(){
		return rarity;
	}

}
