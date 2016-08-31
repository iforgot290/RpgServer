package me.neildennis.crypticrpg.itemsnew.attribs;

import org.bukkit.ChatColor;

public enum Rarity {
	
	COMMON(ChatColor.GRAY + "COMMON"),
	UNCOMMON(ChatColor.GREEN + "UNCOMMON"),
	RARE(ChatColor.AQUA + "RARE"),
	UNIQUE(ChatColor.YELLOW + "UNIQUE");
	
	private String display;
	
	Rarity(String display){
		this.display = display;
	}
	
	public String getDisplay(){
		return display;
	}

}
