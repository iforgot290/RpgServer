package me.neildennis.crypticrpg.itemsnew.attribs;

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

}
