package me.neildennis.crypticrpg.items.attribs;

import org.bukkit.ChatColor;

public enum Tier {
	
	ONE(ChatColor.WHITE),
	TWO(ChatColor.GREEN),
	THREE(ChatColor.AQUA),
	FOUR(ChatColor.LIGHT_PURPLE),
	FIVE(ChatColor.GOLD);
	
	private ChatColor color;
	
	Tier(ChatColor color){
		this.color = color;
	}
	
	public ChatColor getColor(){
		return color;
	}
	
	public static Tier fromLevel(int lvl){
		if (lvl < 20) return Tier.ONE;
		else if (lvl < 40) return Tier.TWO;
		else if (lvl < 60) return Tier.THREE;
		else if (lvl < 80) return Tier.FOUR;
		else return Tier.FIVE;
	}

}
