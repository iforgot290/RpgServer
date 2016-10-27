package me.neildennis.crypticrpg.items.generator.modifiers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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
	 * Constructs a modifier for an item attribute
	 * @param tiermods The different modifier values for the tiers
	 * @param chance Chance to apply the modifier
	 * @param priority Priority the modifier should be shown in
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
		return tiermods.get(tier).generateValue(rare);
	}
	
	@Override
	public int compareTo(ItemModifier o) {
		if (o.getChance() == chance) return 0;
		if (o.getChance() == 1.0F) return -1;
		if (chance == 1.0F) return 1;
		return o.getChance() < chance ? 1 : -1;
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
