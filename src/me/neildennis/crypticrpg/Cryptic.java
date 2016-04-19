package me.neildennis.crypticrpg;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import me.neildennis.crypticrpg.cloud.Cloud;
import me.neildennis.crypticrpg.items.ItemManager;
import me.neildennis.crypticrpg.items.commands.TestCommand;
import me.neildennis.crypticrpg.player.PlayerManager;
import minecade.dungeonrealms.Main;

public class Cryptic {
	
	private PlayerManager playerManager;
	private Cloud cloud;
	private ItemManager itemManager;
	
	private static World mainworld;
	
	public void onEnable(){
		playerManager = new PlayerManager();
		cloud = new Cloud();
		itemManager = new ItemManager();
		
		mainworld = Bukkit.getWorld("Dungeonrealms");
		
		Main.plugin.getCommand("itemtest").setExecutor(new TestCommand());
	}
	
	public PlayerManager getPlayerManager(){
		return playerManager;
	}
	
	public Cloud getCloud(){
		return cloud;
	}
	
	public ItemManager getItemManager(){
		return itemManager;
	}

	public static World getMainWorld() {
		return mainworld;
	}
	
	public static JavaPlugin getPlugin(){
		return Main.plugin;
	}

}
