package me.neildennis.crypticrpg.menu;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.Manager;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;

public class MenuManager extends Manager implements Listener {
	
	@Override
	public void onEnable() {
		Cryptic.registerEvents(this);
		Cryptic.registerEvents(new MenuListener());
		Cryptic.registerCommand("menu", new MenuCommand());
		registerTasks();
	}
	
	@Override
	public void onDisable() {
		
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onChat(AsyncPlayerChatEvent event){
		CrypticPlayer player = Cryptic.getCrypticPlayer(event.getPlayer());
		if (player.getCurrentMenu() != null){
			event.setCancelled(true);
			player.getCurrentMenu().input(event.getMessage());
		}
	}

	@Override
	public void registerTasks() {
		tasks.add(Bukkit.getScheduler().runTaskTimer(Cryptic.getPlugin(), new Runnable(){

			@Override
			public void run(){
				for (CrypticPlayer pl : PlayerManager.getPlayers()){
					if (pl.getCurrentMenu() != null){
						pl.getCurrentMenu().updateMenu();
					}
				}
			}

		}, 20L, 5L));
	}

}
