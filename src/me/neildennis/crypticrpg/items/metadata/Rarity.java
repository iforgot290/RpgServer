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
	
	public static Rarity getFromString(String str){
		switch (str.toLowerCase()){
		
		case "common": return COMMON;
		case "uncommon": return UNCOMMON;
		case "rare": return RARE;
		case "unique": return UNIQUE;
		
		default: return COMMON;
		
		}
	}
	
	public static Rarity rarityFromDamage(int mindmg, int maxdmg){
		return COMMON;
	}
}
