package me.neildennis.crypticrpg.items.type.armor;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import me.neildennis.crypticrpg.items.attribs.Attribute;
import me.neildennis.crypticrpg.items.attribs.Rarity;
import me.neildennis.crypticrpg.items.attribs.Tier;
import me.neildennis.crypticrpg.items.type.CrypticGear;
import me.neildennis.crypticrpg.items.type.CrypticItemType;

public abstract class CrypticArmor extends CrypticGear {
	
	public CrypticArmor(CrypticItemType type, String name, List<String> lore, ArrayList<Attribute> attribs, Tier tier, Rarity rarity) {
		super(type, name, lore, attribs, tier, rarity);
	}
	
	public CrypticArmor(CrypticItemType type, ItemStack stack) {
		super(type, stack);
	}

}
