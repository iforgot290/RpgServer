package me.neildennis.crypticrpg.items.attribs;

import java.util.Random;

import org.bukkit.ChatColor;

public enum Rarity {
	
	COMMON(ChatColor.GRAY + "Common", 0.0F, 0.60F),
	UNCOMMON(ChatColor.GREEN + "Uncommon", 0.61F, 0.75F),
	RARE(ChatColor.AQUA + "Rare", 0.76F, 0.90F),
	UNIQUE(ChatColor.YELLOW + "Unique", 0.91F, 1.0F);
	
	private String display;
	private float lower, upper;
	
	Rarity(String display, float lower, float upper){
		this.display = display;
		this.lower = lower;
		this.upper = upper;
	}
	
	public String getDisplay(){
		return display;
	}
	
	public float getRandomPct(){
		Random r = new Random();
		float range = upper - lower;
		float f = r.nextFloat();
		float gen = f * range;
		
		return lower + gen;
	}
	
	public static Rarity getByPct(float pct){
		if (pct < Rarity.COMMON.upper)
			return Rarity.COMMON;
		else if (pct < Rarity.UNCOMMON.upper)
			return Rarity.UNCOMMON;
		else if (pct < Rarity.RARE.upper)
			return Rarity.RARE;
		else
			return Rarity.UNIQUE;
	}

}
