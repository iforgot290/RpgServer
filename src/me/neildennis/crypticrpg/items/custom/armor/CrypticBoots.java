package me.neildennis.crypticrpg.items.custom.armor;

import java.util.List;

import me.neildennis.crypticrpg.items.custom.CrypticGear;
import me.neildennis.crypticrpg.items.metadata.ItemModifier;
import me.neildennis.crypticrpg.items.metadata.ItemType;
import me.neildennis.crypticrpg.items.metadata.Rarity;

public class CrypticBoots extends CrypticGear{

	public CrypticBoots(){
		super();
	}
	
	public CrypticBoots(String name, List<String> lore, List<ItemModifier> attribs, Rarity rarity, int level){
		super(ItemType.LEGGINGS, name, lore, attribs, rarity, level);
	}
	
}
