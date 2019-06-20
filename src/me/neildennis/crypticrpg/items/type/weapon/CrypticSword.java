package me.neildennis.crypticrpg.items.type.weapon;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.neildennis.crypticrpg.items.attribs.Attribute;
import me.neildennis.crypticrpg.items.attribs.Rarity;
import me.neildennis.crypticrpg.items.attribs.Tier;
import me.neildennis.crypticrpg.items.type.CrypticItemType;

public class CrypticSword extends CrypticWeapon {
	
	public CrypticSword(CrypticItemType type, String name, List<String> lore, ArrayList<Attribute> attribs, Tier tier, Rarity rarity) {
		super(type, name, lore, attribs, tier, rarity);
	}
	
	public CrypticSword(CrypticItemType type, ItemStack stack) {
		super(type, stack);
	}

	@Override
	protected Material getMatFromTier() {
		switch (tier){
			case ONE: return Material.WOODEN_SWORD;
			case TWO: return Material.STONE_SWORD;
			case THREE: return Material.IRON_SWORD;
			case FOUR: return Material.DIAMOND_SWORD;
			case FIVE: return Material.GOLDEN_SWORD;
			default: return Material.WOODEN_SWORD;
		}
	}

	@Override
	protected Tier getTierFromMat(Material mat) {
		switch (mat){
			case WOODEN_SWORD: return Tier.ONE;
			case STONE_SWORD: return Tier.TWO;
			case IRON_SWORD: return Tier.THREE;
			case DIAMOND_SWORD: return Tier.FOUR;
			case GOLDEN_SWORD: return Tier.FIVE;
			default: return Tier.ONE;
		}
	}

}
