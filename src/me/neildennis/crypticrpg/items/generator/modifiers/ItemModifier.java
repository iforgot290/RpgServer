package me.neildennis.crypticrpg.items.generator.modifiers;

import java.util.ArrayList;
import java.util.HashMap;
import me.neildennis.crypticrpg.items.attribs.AttributeType;
import me.neildennis.crypticrpg.items.attribs.Rarity;
import me.neildennis.crypticrpg.items.attribs.Tier;
import me.neildennis.crypticrpg.items.type.CrypticItem;

public class ItemModifier implements Comparable<ItemModifier>{
	
	protected AttributeType type;
	protected HashMap<Tier, TierModifier> tiermods;
	protected ArrayList<Class<? extends CrypticItem>> possible;
	protected ArrayList<Class<? extends CrypticItem>> exclude;
	protected float chance;
	
	/**
	 * Constructs an item modifier with tier types
	 * @param type type of attribute
	 * @param tiermods mods for the different tiers
	 * @param possible possible items to apply to
	 * @param chance chance of applying to item
	 */
	public ItemModifier(AttributeType type, HashMap<Tier, TierModifier> tiermods, ArrayList<Class<? extends CrypticItem>> possible, float chance){
		this(type, tiermods, possible, new ArrayList<Class<? extends CrypticItem>>(), chance);
	}
	
	public ItemModifier(AttributeType type, HashMap<Tier, TierModifier> tiermods, ArrayList<Class<? extends CrypticItem>> possible, ArrayList<Class<? extends CrypticItem>> exclude, float chance){
		this.type = type;
		this.tiermods = tiermods;
		this.possible = possible;
		this.exclude = exclude;
		this.chance = chance;
	}
	
	public AttributeType getType(){
		return type;
	}
	
	public float getChance(){
		return chance;
	}
	
	public boolean isPossible(Class<? extends CrypticItem> cl){
		return possible.contains(cl);
	}
	
	public boolean isExcluded(Class<? extends CrypticItem> cl){
		return exclude.contains(cl);
	}
	
	public int[] getValues(Tier tier, Rarity rare){
		if (type == AttributeType.DAMAGE || type == AttributeType.HEALTH)
			return tiermods.get(tier).generateValue(rare);
		else return tiermods.get(tier).generateValue(null);
	}
	
	@Override
	public int compareTo(ItemModifier o) {
		if (o.getChance() == chance) return 0;
		if (o.getChance() == 1.0F) return -1;
		if (chance == 1.0F) return 1;
		return o.getChance() < chance ? 1 : -1;
	}

}
