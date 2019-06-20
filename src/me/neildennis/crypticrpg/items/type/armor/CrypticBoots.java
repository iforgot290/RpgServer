package me.neildennis.crypticrpg.items.type.armor;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.neildennis.crypticrpg.items.attribs.Attribute;
import me.neildennis.crypticrpg.items.attribs.Rarity;
import me.neildennis.crypticrpg.items.attribs.Tier;
import me.neildennis.crypticrpg.items.type.CrypticItemType;

public class CrypticBoots extends CrypticArmor {

	public CrypticBoots(CrypticItemType type, String name, List<String> lore, ArrayList<Attribute> attribs, Tier tier, Rarity rarity) {
		super(type, name, lore, attribs, tier, rarity);
	}
	
	public CrypticBoots(CrypticItemType type, ItemStack stack) {
		super(type, stack);
	}
	
	@Override
	protected Material getMatFromTier() {
		switch (tier){
			case ONE: return Material.LEATHER_BOOTS;
			case TWO: return Material.CHAINMAIL_BOOTS;
			case THREE: return Material.IRON_BOOTS;
			case FOUR: return Material.DIAMOND_BOOTS;
			case FIVE: return Material.GOLDEN_BOOTS;
			default: return Material.LEATHER_BOOTS;
		}
	}

	@Override
	protected Tier getTierFromMat(Material mat) {
		switch (mat){
			case LEATHER_BOOTS: return Tier.ONE;
			case CHAINMAIL_BOOTS: return Tier.TWO;
			case IRON_BOOTS: return Tier.THREE;
			case DIAMOND_BOOTS: return Tier.FOUR;
			case GOLDEN_BOOTS: return Tier.FIVE;
			default: return Tier.ONE;
		}
	}

}
