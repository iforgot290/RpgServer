package me.neildennis.crypticrpg.items.custom;

import java.util.List;

import me.neildennis.crypticrpg.items.metadata.ItemModifier;
import me.neildennis.crypticrpg.items.metadata.ItemType;
import me.neildennis.crypticrpg.items.metadata.Rarity;

public abstract class CrypticWeapon extends CrypticGear {

	protected CrypticWeapon(){
		super();
	}

	protected CrypticWeapon(ItemType type, String name, List<String> lore, List<ItemModifier> attribs, int tier, Rarity rarity, int level){
		super(type, name, lore, attribs, tier, rarity, level);
	}

}
