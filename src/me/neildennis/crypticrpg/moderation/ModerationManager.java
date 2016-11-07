package me.neildennis.crypticrpg.moderation;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.moderation.ModerationData.Ban;
import me.neildennis.crypticrpg.moderation.commands.CommandBan;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;

public class ModerationManager implements Listener{

	public ModerationManager(){
		Bukkit.getPluginManager().registerEvents(this, Cryptic.getPlugin());
		
		Cryptic.registerCommand("ban", new CommandBan());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event){
		try {
			ModerationData data = new ModerationData(event.getUniqueId());
			Ban ban = data.getCurrentBan();
			
			if (ban != null && ban.isBanned()){
				String reason = ban.getReason();
				if (reason == null || reason.equals("")) reason = "The ban hammer has spoken!";
				event.disallow(Result.KICK_BANNED, getKickedBannedMessage(reason));
			} else {
				CrypticPlayer pl = new CrypticPlayer(event.getUniqueId(), data);
				PlayerManager.getPlayers().add(pl);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			event.disallow(Result.KICK_OTHER, ChatColor.RED + "Error contacting the database");
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerAsyncChat(AsyncPlayerChatEvent event){
		CrypticPlayer pl = PlayerManager.getCrypticPlayer(event.getPlayer());
		if (pl.getModerationData().isMuted()){
			pl.sendMessage(ChatColor.RED + "You are muted.");
			event.setCancelled(true);
		}
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
