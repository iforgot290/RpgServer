package me.neildennis.crypticrpg.moderation;

import java.sql.SQLException;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.moderation.ModerationData.Ban;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;

public class ModerationListener implements Listener {
	
	private ModerationManager manager;
	
	public ModerationListener(ModerationManager manager) {
		this.manager = manager;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event){
		try {
			Ban ban = manager.getCurrentBan(event.getUniqueId());
			
			if (ban != null && ban.isBanned()){
				String reason = ban.getReason();
				if (reason == null || reason.equals("")) reason = "The ban hammer has spoken!";
				event.setLoginResult(Result.KICK_BANNED);
				event.setKickMessage(manager.getKickedBannedMessage(reason));
			} else {
				CrypticPlayer pl = new CrypticPlayer(event.getUniqueId());
				PlayerManager.getPlayers().add(pl);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			event.disallow(Result.KICK_OTHER, ChatColor.RED + "Error contacting the database");
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerAsyncChat(AsyncPlayerChatEvent event){
		CrypticPlayer pl = Cryptic.getCrypticPlayer(event.getPlayer());
		if (pl.getModerationData().isMuted()){
			pl.sendMessage(ChatColor.RED + "You are muted.");
			event.setCancelled(true);
		}
	}

}
