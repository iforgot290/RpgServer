package me.neildennis.crypticrpg.menu;

import org.bukkit.Bukkit;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.Manager;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;
import me.neildennis.crypticrpg.utils.Log;

public class MenuManager extends Manager{

	public MenuManager(){
		
		registerTasks();
	}
	
	@Override
	public void registerTasks() {
		tasks.add(Bukkit.getScheduler().runTaskTimer(Cryptic.getPlugin(), new Runnable(){
			
			@Override
			public void run(){
				for (CrypticPlayer pl : PlayerManager.getPlayers()){
					if (pl.getCurrentMenu() != null){
						pl.getCurrentMenu().updateMenu();
						pl.getCurrentMenu().tickMenu();
					}
				}
			}
			
		}, 20L, 5L));
	}

}
