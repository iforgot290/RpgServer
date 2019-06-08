package me.neildennis.crypticrpg.player;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.Manager;
import me.neildennis.crypticrpg.cloud.data.PlayerData;

public class PlayerManager extends Manager implements Listener{
	
	private static ArrayList<CrypticPlayer> players;

	@Override
	public void onEnable() {
		players = new ArrayList<CrypticPlayer>();
		Cryptic.registerEvents(this);
		Cryptic.registerEvents(new DebugListener());
	}
	
	@Override
	public void onDisable() {
		
	}
	
	@Override
	public void registerTasks() {
		
	}
	
	public static ArrayList<CrypticPlayer> getPlayers(){
		return players;
	}
	
	public static ArrayList<CrypticPlayer> search(PlayerSearch criteria){
		ArrayList<CrypticPlayer> toreturn = new ArrayList<CrypticPlayer>();
		for (CrypticPlayer pl : players)
			if (criteria.meetsCriteria(pl)) toreturn.add(pl);
		return toreturn;
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
	
	public interface PlayerSearch{
		public boolean meetsCriteria(CrypticPlayer pl);
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
