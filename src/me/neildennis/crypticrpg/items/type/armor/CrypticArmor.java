package me.neildennis.crypticrpg.items.type.armor;

import java.util.ArrayList;
import java.util.List;

import me.neildennis.crypticrpg.items.attribs.Attribute;
import me.neildennis.crypticrpg.items.attribs.Rarity;
import me.neildennis.crypticrpg.items.attribs.Tier;
import me.neildennis.crypticrpg.items.type.CrypticGear;
import me.neildennis.crypticrpg.items.type.CrypticItemType;

public abstract class CrypticArmor extends CrypticGear{
	
	public CrypticArmor(String name, List<String> lore, CrypticItemType type, ArrayList<Attribute> attribs, Tier tier, Rarity rarity) {
		super(name, lore, type, attribs, tier, rarity);
	}
	
	public CrypticArmor(){
		super();
	}

}
