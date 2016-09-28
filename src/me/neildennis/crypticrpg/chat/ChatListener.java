package me.neildennis.crypticrpg.chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;
import me.neildennis.crypticrpg.utils.Log;

public class ChatListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onAsyncChat(AsyncPlayerChatEvent event){
		if (!event.isCancelled()){
			event.setCancelled(true);
			CrypticPlayer pl = PlayerManager.getCrypticPlayer(event.getPlayer());
			ChatManager.showGlobal(pl, event.getMessage());
		}
	}

}
