package me.neildennis.crypticrpg.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.neildennis.crypticrpg.utils.FancyMessage;

public abstract class Menu {
	
	protected String top =
			ChatColor.RED + "+" + ChatColor.GRAY + "--------------------------------------------------" + ChatColor.RED + "+";
	
	protected Player pl;
	protected List<FancyMessage> text;
	protected List<FancyMessage> options;
	
	public Menu(Player pl){
		this.pl = pl;
		text = new ArrayList<FancyMessage>();
		options = new ArrayList<FancyMessage>();
	}
	
	public abstract void updateMenu();
	
	public void tickMenu(){
		for (int i = 0; i < 15; i++){
			pl.sendMessage("");
		}
		pl.sendMessage(top);
		pl.sendMessage("");
		
		for (FancyMessage msg : text){
			pl.spigot().sendMessage(msg.getMessage());
		}
		
		if (text.size() > 0) pl.sendMessage("");
		
		for (FancyMessage msg : options){
			pl.spigot().sendMessage(msg.getMessage());
		}
		
		pl.sendMessage("");
		pl.sendMessage(top);
	}
	
}
