package me.neildennis.crypticrpg.items.attribs;

import org.bukkit.ChatColor;

public enum Rarity {
	
	COMMON(ChatColor.GRAY + "Common"),
	UNCOMMON(ChatColor.GREEN + "Uncommon"),
	RARE(ChatColor.AQUA + "Rare"),
	UNIQUE(ChatColor.YELLOW + "Unique");
	
	private String display;
	
	Rarity(String display){
		this.display = display;
	}
	
	public String getDisplay(){
		return display;
	}

}
