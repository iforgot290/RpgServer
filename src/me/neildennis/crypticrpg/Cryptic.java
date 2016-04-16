package me.neildennis.crypticrpg;

import me.neildennis.crypticrpg.cloud.Cloud;
import me.neildennis.crypticrpg.items.commands.TestCommand;
import me.neildennis.crypticrpg.player.PlayerManager;
import minecade.dungeonrealms.Main;

public class Cryptic {
	
	private PlayerManager playerManager;
	private Cloud cloud;
	
	public void onEnable(){
		playerManager = new PlayerManager();
		cloud = new Cloud();
		Main.plugin.getCommand("itemtest").setExecutor(new TestCommand());
	}
	
	public PlayerManager getPlayerManager(){
		return playerManager;
	}
	
	public Cloud getCloud(){
		return cloud;
	}

}
