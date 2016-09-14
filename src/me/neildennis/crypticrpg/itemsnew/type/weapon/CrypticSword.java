package me.neildennis.crypticrpg.itemsnew.type.weapon;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;

import me.neildennis.crypticrpg.itemsnew.attribs.Attribute;
import me.neildennis.crypticrpg.itemsnew.attribs.Tier;
import me.neildennis.crypticrpg.itemsnew.type.CrypticItemType;

public class CrypticSword extends CrypticWeapon{
	
	public CrypticSword(String name, List<String> lore, CrypticItemType type, HashMap<Attribute, Integer> attribs, Tier tier) {
		super(name, lore, type, attribs, tier);
	}
	
	public CrypticSword(){
		super();
	}

	@Override
	protected Material getMatFromTier() {
		switch (tier){
			case ONE: return Material.WOOD_SWORD;
			case TWO: return Material.STONE_SWORD;
			case THREE: return Material.IRON_SWORD;
			case FOUR: return Material.DIAMOND_SWORD;
			case FIVE: return Material.GOLD_SWORD;
			default: return Material.WOOD_SWORD;
		}
	}

	@Override
	protected Tier getTierFromMat(Material mat) {
		switch (mat){
			case WOOD_SWORD: return Tier.ONE;
			case STONE_SWORD: return Tier.TWO;
			case IRON_SWORD: return Tier.THREE;
			case DIAMOND_SWORD: return Tier.FOUR;
			case GOLD_SWORD: return Tier.FIVE;
			default: return Tier.ONE;
		}
	}

}
