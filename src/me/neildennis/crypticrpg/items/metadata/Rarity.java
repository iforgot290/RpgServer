package me.neildennis.crypticrpg.items.metadata;

import org.bukkit.ChatColor;

public enum Rarity {
	COMMON(ChatColor.GRAY, "Common"),
	UNCOMMON(ChatColor.GREEN, "Uncommon"),
	RARE(ChatColor.AQUA, "Rare"),
	UNIQUE(ChatColor.YELLOW, "Unique");
	
	private ChatColor color;
	private String display;
	
	Rarity(ChatColor color, String display){
		this.color = color;
		this.display = display;
	}
	
	public ChatColor getColor(){
		return color;
	}
	
	public String getDisplay(){
		return display;
	}
	
	public static Rarity getFromChance(int c){
		if (c < 60) return COMMON;
		else if (c < 85) return UNCOMMON;
		else if (c < 98) return RARE;
		else return UNIQUE;
	}

}
