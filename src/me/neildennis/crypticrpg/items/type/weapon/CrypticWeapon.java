package me.neildennis.crypticrpg.items.type.weapon;

import java.util.HashMap;
import java.util.List;

import me.neildennis.crypticrpg.items.attribs.Attribute;
import me.neildennis.crypticrpg.items.attribs.Rarity;
import me.neildennis.crypticrpg.items.attribs.Tier;
import me.neildennis.crypticrpg.items.type.CrypticGear;
import me.neildennis.crypticrpg.items.type.CrypticItemType;

public abstract class CrypticWeapon extends CrypticGear{
	
	public CrypticWeapon(String name, List<String> lore, CrypticItemType type, HashMap<Attribute, Integer> attribs, Tier tier, Rarity rarity) {
		super(name, lore, type, attribs, tier, rarity);
	}
	
	public CrypticWeapon(){
		super();
	}
	
}
