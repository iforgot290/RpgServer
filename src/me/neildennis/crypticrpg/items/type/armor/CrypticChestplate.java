package me.neildennis.crypticrpg.items.type.armor;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.neildennis.crypticrpg.items.attribs.Attribute;
import me.neildennis.crypticrpg.items.attribs.Rarity;
import me.neildennis.crypticrpg.items.attribs.Tier;
import me.neildennis.crypticrpg.items.type.CrypticItemType;

public class CrypticChestplate extends CrypticArmor {

	public CrypticChestplate(CrypticItemType type, String name, List<String> lore, ArrayList<Attribute> attribs, Tier tier, Rarity rarity) {
		super(type, name, lore, attribs, tier, rarity);
	}
	
	public CrypticChestplate(CrypticItemType type, ItemStack stack) {
		super(type, stack);
	}
	
	@Override
	protected Material getMatFromTier() {
		switch (tier){
			case ONE: return Material.LEATHER_CHESTPLATE;
			case TWO: return Material.CHAINMAIL_CHESTPLATE;
			case THREE: return Material.IRON_CHESTPLATE;
			case FOUR: return Material.DIAMOND_CHESTPLATE;
			case FIVE: return Material.GOLDEN_CHESTPLATE;
			default: return Material.LEATHER_CHESTPLATE;
		}
	}

	@Override
	protected Tier getTierFromMat(Material mat) {
		switch (mat){
			case LEATHER_CHESTPLATE: return Tier.ONE;
			case CHAINMAIL_CHESTPLATE: return Tier.TWO;
			case IRON_CHESTPLATE: return Tier.THREE;
			case DIAMOND_CHESTPLATE: return Tier.FOUR;
			case GOLDEN_CHESTPLATE: return Tier.FIVE;
			default: return Tier.ONE;
		}
	}

}
