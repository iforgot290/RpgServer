package me.neildennis.crypticrpg.items.generator.modifiers;

import java.util.HashMap;

import me.neildennis.crypticrpg.items.attribs.Tier;

public class ItemModifier implements Comparable<ItemModifier>{
	
	protected HashMap<Tier, TierModifier> tiermods;
	protected float chance;
	protected int priority;
	
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
	
	public void setPriority(int priority){
		this.priority = priority;
	}
	
	public int getPriority(){
		return priority;
	}
	
	public float getChance(){
		return chance;
	}
	
	@Override
	public int compareTo(ItemModifier o) {
		if (o.getPriority() == priority) return 0;
		return o.getPriority() < priority ? -1 : 1;
	}
	
	public class TierModifier {
		
		private int low, high, lowHigh;
		private ModifierType type;
		
		public TierModifier(ModifierType type, int low, int high){
			this.type = type;
			this.low = low;
			this.high = high;
		}
		
		public TierModifier(ModifierType type, int low, int high, int lowHigh){
			this(type, low, high);
			this.lowHigh = lowHigh;
		}
		
		public int generateValue(){
			
			
			if (type == ModifierType.STATIC){
				
			}
			
			return 0;
		}
		
	}
	
	public enum ModifierType{
		STATIC, DOUBLE, TRIPLE;
	}

}
