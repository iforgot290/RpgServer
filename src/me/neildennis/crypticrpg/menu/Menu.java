package me.neildennis.crypticrpg.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.utils.FancyMessage;

public abstract class Menu {
	
	protected String top =
			ChatColor.RED + "+" + ChatColor.GRAY + "--------------------------------------------------" + ChatColor.RED + "+";
	
	protected CrypticPlayer pl;
	protected List<FancyMessage> text;
	protected List<FancyMessage> options;
	
	public Menu(CrypticPlayer pl){
		this.pl = pl;
		text = new ArrayList<FancyMessage>();
		options = new ArrayList<FancyMessage>();
	}
	
	public void updateMenu(){};
	public abstract void input(String str);
	public abstract void display();
	
}
