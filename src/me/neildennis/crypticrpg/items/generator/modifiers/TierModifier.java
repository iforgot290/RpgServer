package me.neildennis.crypticrpg.items.generator.modifiers;

import java.util.Random;

import me.neildennis.crypticrpg.items.attribs.Rarity;
import me.neildennis.crypticrpg.utils.Log;

public class TierModifier {
	
	private int minlvl, maxlvl;
	private int low, high, middle;
	private ModifierType type;
	
	public TierModifier(ModifierType type, int minlvl, int maxlvl, int low, int high){
		this.type = type;
		this.minlvl = minlvl;
		this.maxlvl = maxlvl;
		this.low = low;
		this.high = high;
		this.middle = high;
	}
	
	public TierModifier(ModifierType type, int minlvl, int maxlvl, int low, int high, int middle){
		this(type, minlvl, maxlvl, low, high);
		this.middle = middle;
	}
	
	public boolean isInRange(int level){
		return level >= minlvl && level <= maxlvl;
	}
	
	public int[] generateValue(Rarity rarity){
		int[] values = new int[2];
		Random r = new Random();
		
		int first = (middle - low > 0 ? r.nextInt(middle - low) + low : low);
		int second = first;
		
		//generates a value that is inbetween the two values provided
		//can use the rarity value
		if (type == ModifierType.STATIC){
			float rareValue;
			
			if (rarity == null) rareValue = r.nextFloat();
			else rareValue = rarity.getRandomPct();
			
			first = (int) ((high - low) * rareValue) + low;
			second = first;
		}
		
		//generates 2 values using middle as the mid point
		//wont use the rarity value
		else if (type == ModifierType.TRIPLE){
			second = r.nextInt(high - middle) + middle;
		}
		
		//generates a value range based on a rarity factor
		else if (type == ModifierType.RANGE){
			float rareValue;
			
			if (rarity == null) rareValue = r.nextFloat();
			else rareValue = rarity.getRandomPct();
		
			int range = high - low;
			int x = (int) (range * rareValue);
			int dps = low + x;
			
			Log.debug("Rare value: " + rareValue);
			Log.debug("Range: " + range);
			Log.debug("X: " + x);
			Log.debug("DPS: " + dps);
			
			float spreadRarity = r.nextFloat();
			int spread = (int) (middle * spreadRarity);
			first = dps - spread;
			second = dps + spread;
		}
		
		values[0] = first;
		values[1] = second;
		
		return values;
	}
	
	public enum ModifierType{
		STATIC, TRIPLE, RANGE;
	}
	
}