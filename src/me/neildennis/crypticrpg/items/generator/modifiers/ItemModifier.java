package me.neildennis.crypticrpg.items.generator.modifiers;

import java.util.HashMap;

import me.neildennis.crypticrpg.items.attribs.Tier;

public class ItemModifier implements Comparable<ItemModifier>{
	
	protected HashMap<Tier, TierModifier> tiermods;
	protected float chance;
	protected float priority;
	
	/**
	 * Constructs a modifier for an item attribute
	 * @param tiermods The different modifier values for the tiers
	 * @param chance Chance to apply the modifier
	 * @param priority Priority the modifier should be shown in
	 */
	public ItemModifier(HashMap<Tier, TierModifier> tiermods, float chance, int priority){
		this.tiermods = tiermods;
		this.chance = chance;
		this.priority = priority;
	}
	
	@Override
	public int compareTo(ItemModifier o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public class TierModifier {
		
	}

}
