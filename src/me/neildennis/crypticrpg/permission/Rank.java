package me.neildennis.crypticrpg.permission;

import org.bukkit.ChatColor;

public enum Rank {
	
	NORMAL(0, ""),
	ADMIN(1, "&4Admin&f"),
	OWNER(2, "&b&lGM&f");
	
	private int priority;
	private String prefix;
	
	Rank(int priority, String prefix){
		this.priority = priority;
		this.prefix = prefix;
	}
	
	public int getPriority(){
		return priority;
	}
	
	public String getPrefix(){
		if (prefix.equalsIgnoreCase("")) return "";
		return ChatColor.translateAlternateColorCodes('&', prefix) + " ";
	}

}
