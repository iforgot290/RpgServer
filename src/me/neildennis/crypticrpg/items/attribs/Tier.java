package me.neildennis.crypticrpg.items.attribs;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum Tier {
	
	ONE(ChatColor.WHITE, 0.75F, Material.COAL_ORE),
	TWO(ChatColor.GREEN, 0.6F, Material.EMERALD_ORE),
	THREE(ChatColor.AQUA, 0.5F, Material.IRON_ORE),
	FOUR(ChatColor.LIGHT_PURPLE, 0.4F, Material.DIAMOND_ORE),
	FIVE(ChatColor.GOLD, 0.3F, Material.GOLD_ORE);
	
	private ChatColor color;
	private float chance;
	private Material oretype;
	
	Tier(ChatColor color, float chance, Material oretype){
		this.color = color;
		this.chance = chance;
		this.oretype = oretype;
	}
	
	public ChatColor getColor(){
		return color;
	}
	
	public float getDefaultDropPct(){
		return chance;
	}
	
	public static Tier fromLevel(int lvl){
		if (lvl < 20) return Tier.ONE;
		else if (lvl < 40) return Tier.TWO;
		else if (lvl < 60) return Tier.THREE;
		else if (lvl < 80) return Tier.FOUR;
		else return Tier.FIVE;
	}
	
	public static Tier fromInt(int i){
		if (i == 2) return Tier.TWO;
		else if (i == 3) return Tier.THREE;
		else if (i == 4) return Tier.FOUR;
		else if (i == 5) return Tier.FIVE;
		else return Tier.ONE;
	}
	
	public static int randomLevel(Tier tier){
		Random r = new Random();
		int lvl = r.nextInt(20);
		if (tier == Tier.ONE) return r.nextInt(19) + 1;
		else if (tier == Tier.TWO) return lvl + 20;
		else if (tier == Tier.THREE) return lvl + 40;
		else if (tier == Tier.FOUR) return lvl + 60;
		else if (tier == Tier.FIVE) return lvl + 80;
		else return lvl;
	}
	
	public Material getOreType(){
		return oretype;
	}

}
