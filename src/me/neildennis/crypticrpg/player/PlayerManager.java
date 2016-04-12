package me.neildennis.crypticrpg.player;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import minecade.dungeonrealms.Main;

public class PlayerManager implements Listener{
	
	private static ArrayList<CrypticPlayer> players;

	public PlayerManager(){
		players = new ArrayList<CrypticPlayer>();
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}
	
	public static ArrayList<CrypticPlayer> getPlayers(){
		return players;
	}
	
	public static CrypticPlayer getCrypticPlayer(OfflinePlayer pl){
		return getCrypticPlayer(pl.getUniqueId());
	}
	
	public static CrypticPlayer getCrypticPlayer(UUID id){
		for (CrypticPlayer cp : players)
			if (cp.getOfflinePlayer().getUniqueId().equals(id))
				return cp;
		return null;
	}
	
	@EventHandler
	public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event){
		players.add(new CrypticPlayer(event.getUniqueId()));
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		//TODO combat logging
		
	}
	
}
