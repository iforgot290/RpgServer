package me.neildennis.crypticrpg.items.generator.modifiers;

import java.util.HashMap;
import java.util.Random;

import me.neildennis.crypticrpg.items.attribs.Rarity;
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
		
		private int low, high, middle;
		private ModifierType type;
		
		public TierModifier(ModifierType type, int low, int high){
			this.type = type;
			this.low = low;
			this.high = high;
			this.middle = high;
		}
		
		public TierModifier(ModifierType type, int low, int high, int middle){
			this(type, low, high);
			this.middle = middle;
		}
		
		public int[] generateValue(Rarity rarity){
			int[] values = new int[2];
			Random r = new Random();
			
			int first = (middle - low > 0 ? r.nextInt(middle - low) + low : low);
			int second = first;
			
			if (type == ModifierType.DOUBLE){
				second = r.nextInt(high - first) + first;
			} else if (type == ModifierType.TRIPLE){
				second = r.nextInt(high - middle) + middle;
			} else if (type == ModifierType.RANGE){
				float rareValue = 0.0F;
				
				if (rarity == null) rareValue = r.nextFloat();
				else rareValue = rarity.getRandomPct();
			
				int range = high - low;
				int x = (int) (range * rareValue);
				int dps = low + x;
				
				float spreadRarity = r.nextFloat();
				first = (int) (middle * spreadRarity);
				
				second = 2 * dps - first;
			}
			
			values[0] = first;
			values[1] = second;
			
			return values;
		}
		
	}
	
	public enum ModifierType{
		STATIC, DOUBLE, TRIPLE, RANGE;
	}

}
