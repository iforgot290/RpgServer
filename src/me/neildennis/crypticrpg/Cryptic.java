package me.neildennis.crypticrpg;

import me.neildennis.crypticrpg.cloud.Cloud;
import me.neildennis.crypticrpg.player.PlayerManager;

public class Cryptic {
	
	private PlayerManager playerManager;
	private Cloud cloud;
	
	public void onEnable(){
		playerManager = new PlayerManager();
		cloud = new Cloud();
	}
	
	public PlayerManager getPlayerManager(){
		return playerManager;
	}
	
	public Cloud getCloud(){
		return cloud;
	}

}
