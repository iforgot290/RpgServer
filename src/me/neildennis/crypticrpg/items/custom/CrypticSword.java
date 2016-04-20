package me.neildennis.crypticrpg.items.custom;

import java.util.List;

import me.neildennis.crypticrpg.items.metadata.Attribute;
import me.neildennis.crypticrpg.items.metadata.ItemType;
import me.neildennis.crypticrpg.items.metadata.Rarity;

public class CrypticSword extends CrypticWeapon{
	
	public CrypticSword(String name, List<String> lore, List<Attribute> attribs, int tier, Rarity rarity, int level){
		super(ItemType.SWORD, name, lore, attribs, tier, rarity, level);
	}

}
