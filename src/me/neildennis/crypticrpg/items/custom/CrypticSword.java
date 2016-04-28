package me.neildennis.crypticrpg.items.custom;

import java.util.List;

import me.neildennis.crypticrpg.items.metadata.ItemModifier;
import me.neildennis.crypticrpg.items.metadata.ItemType;
import me.neildennis.crypticrpg.items.metadata.Rarity;

public class CrypticSword extends CrypticWeapon{
	
	public CrypticSword(){
		super();
	}
	
	public CrypticSword(String name, List<String> lore, List<ItemModifier> attribs, Rarity rarity, int level){
		super(ItemType.SWORD, name, lore, attribs, rarity, level);
	}

}
