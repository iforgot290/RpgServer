package me.neildennis.crypticrpg;

import me.neildennis.crypticrpg.player.PlayerManager;

public class Cryptic {
	
	private PlayerManager playerManager;
	
	public void onEnable(){
		playerManager = new PlayerManager();
	}
	
	public PlayerManager getPlayerManager(){
		return playerManager;
	}

}
