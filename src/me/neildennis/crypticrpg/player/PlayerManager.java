package me.neildennis.crypticrpg.player;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.cloud.data.PlayerData;

public class PlayerManager implements Listener{
	
	private static ArrayList<CrypticPlayer> players;

	public PlayerManager(){
		players = new ArrayList<CrypticPlayer>();
		Bukkit.getPluginManager().registerEvents(this, Cryptic.getPlugin());
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
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event){
		if (event.getLoginResult() == Result.ALLOWED){
			CrypticPlayer pl = new CrypticPlayer(event.getUniqueId());
			players.add(pl);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		CrypticPlayer pl = getCrypticPlayer(event.getPlayer());
		pl.online(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		//TODO combat logging
		CrypticPlayer pl = getCrypticPlayer(event.getPlayer());
		pl.cancelTasks();
		PlayerData.savePlayerData(pl);
		players.remove(pl);
	}
	
}
