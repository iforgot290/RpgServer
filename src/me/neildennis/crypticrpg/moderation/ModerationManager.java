package me.neildennis.crypticrpg.moderation;

import java.sql.ResultSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import me.neildennis.crypticrpg.cloud.Cloud;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;
import minecade.dungeonrealms.Main;
import net.md_5.bungee.api.ChatColor;

public class ModerationManager implements Listener{

	public ModerationManager(){
		Bukkit.getPluginManager().registerEvents(this, Main.plugin);
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event){
		ResultSet result = Cloud.sendQuery("SELECT * FROM player_db WHERE player_id = '" + event.getUniqueId().toString() + "'");
		try {
			result.next();
			if (result.getBoolean("banned")){
				String reason = result.getString("banned_reason");
				if (reason == null) reason = "The ban hammer has spoken!";
				
				event.disallow(Result.KICK_BANNED, getKickedBannedMessage(reason));
			}
		} catch (Exception e){
			e.printStackTrace();
			event.disallow(Result.KICK_OTHER, ChatColor.RED + "Error contacting the database");
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerAsyncChat(AsyncPlayerChatEvent event){
		CrypticPlayer pl = PlayerManager.getCrypticPlayer(event.getPlayer());
		if (pl.getModerationData().isMuted())
			event.setCancelled(true);
	}
	
	public static String getKickedBannedMessage(String reason){
		return "Banned: " + reason;
	}
	
	public static String getKickedMessage(String reason){
		return "Kicked: " + reason;
	}
	
	public static void kickPlayer(Player player, String reason){
		
	}

}
