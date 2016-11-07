package me.neildennis.crypticrpg.items.generator.modifiers;

import java.util.Random;

import me.neildennis.crypticrpg.items.attribs.Rarity;
import me.neildennis.crypticrpg.items.generator.modifiers.ItemModifier.ModifierType;
import me.neildennis.crypticrpg.utils.Log;

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
	
}