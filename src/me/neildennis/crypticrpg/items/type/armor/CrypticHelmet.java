package me.neildennis.crypticrpg.items.type.armor;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;

import me.neildennis.crypticrpg.items.attribs.Attribute;
import me.neildennis.crypticrpg.items.attribs.Rarity;
import me.neildennis.crypticrpg.items.attribs.Tier;
import me.neildennis.crypticrpg.items.type.CrypticItemType;

public class CrypticHelmet extends CrypticArmor{

	public CrypticHelmet(String name, List<String> lore, CrypticItemType type, HashMap<Attribute, Integer> attribs, Tier tier, Rarity rarity) {
		super(name, lore, type, attribs, tier, rarity);
	}
	
	public CrypticHelmet(){
		super();
	}
	
	@Override
	protected Material getMatFromTier() {
		switch (tier){
			case ONE: return Material.LEATHER_HELMET;
			case TWO: return Material.CHAINMAIL_HELMET;
			case THREE: return Material.IRON_HELMET;
			case FOUR: return Material.DIAMOND_HELMET;
			case FIVE: return Material.GOLD_HELMET;
			default: return Material.LEATHER_HELMET;
		}
	}

	@Override
	protected Tier getTierFromMat(Material mat) {
		switch (mat){
			case LEATHER_HELMET: return Tier.ONE;
			case CHAINMAIL_HELMET: return Tier.TWO;
			case IRON_HELMET: return Tier.THREE;
			case DIAMOND_HELMET: return Tier.FOUR;
			case GOLD_HELMET: return Tier.FIVE;
			default: return Tier.ONE;
		}
	}

}
