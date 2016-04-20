package me.neildennis.crypticrpg.chat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.permission.Rank;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;

public class ChatManager implements Listener{
	
	private static String globalPrefix = ChatColor.AQUA + "<G> ";
	
	public ChatManager(){
		Bukkit.getPluginManager().registerEvents(this, Cryptic.getPlugin());
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onAsyncChat(AsyncPlayerChatEvent event){
		event.setCancelled(true);
		CrypticPlayer pl = PlayerManager.getCrypticPlayer(event.getPlayer());
		showGlobal(pl, event.getMessage());
	}
	
	public static void showGlobal(CrypticPlayer pl, String message){
		String broadcast = globalPrefix + pl.getRankData().getRank().getPrefix()
				+ pl.getPlayer().getDisplayName() + ChatColor.RESET + ": ";
		
		if (pl.getRankData().getRank().getPriority() <= Rank.ADMIN.getPriority())
			broadcast += ChatColor.translateAlternateColorCodes('&', message);
		else
			broadcast += message;
		
		Bukkit.broadcastMessage(broadcast);
	}

}
