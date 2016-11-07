package me.neildennis.crypticrpg.items.type.armor;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import me.neildennis.crypticrpg.items.attribs.Attribute;
import me.neildennis.crypticrpg.items.attribs.Rarity;
import me.neildennis.crypticrpg.items.attribs.Tier;
import me.neildennis.crypticrpg.items.type.CrypticItemType;

public class CrypticLeggings extends CrypticArmor{

	public CrypticLeggings(String name, List<String> lore, CrypticItemType type, ArrayList<Attribute> attribs, Tier tier, Rarity rarity) {
		super(name, lore, type, attribs, tier, rarity);
	}
	
	public CrypticLeggings(){
		super();
	}
	
	@Override
	protected Material getMatFromTier() {
		switch (tier){
			case ONE: return Material.LEATHER_LEGGINGS;
			case TWO: return Material.CHAINMAIL_LEGGINGS;
			case THREE: return Material.IRON_LEGGINGS;
			case FOUR: return Material.DIAMOND_LEGGINGS;
			case FIVE: return Material.GOLD_LEGGINGS;
			default: return Material.LEATHER_LEGGINGS;
		}
	}

	@Override
	protected Tier getTierFromMat(Material mat) {
		switch (mat){
			case LEATHER_LEGGINGS: return Tier.ONE;
			case CHAINMAIL_LEGGINGS: return Tier.TWO;
			case IRON_LEGGINGS: return Tier.THREE;
			case DIAMOND_LEGGINGS: return Tier.FOUR;
			case GOLD_LEGGINGS: return Tier.FIVE;
			default: return Tier.ONE;
		}
	}

}
