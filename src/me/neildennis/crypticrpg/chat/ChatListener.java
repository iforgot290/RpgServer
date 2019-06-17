package me.neildennis.crypticrpg.chat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.player.CrypticPlayer;

public class ChatListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onAsyncChat(AsyncPlayerChatEvent event){
		if (!event.isCancelled()){
			event.setCancelled(true);
			CrypticPlayer pl = Cryptic.getCrypticPlayer(event.getPlayer());
			ChatManager.showGlobal(pl, event.getMessage());
		}
	}

}
