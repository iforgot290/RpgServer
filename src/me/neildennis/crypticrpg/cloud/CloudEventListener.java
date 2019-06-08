package me.neildennis.crypticrpg.cloud;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class CloudEventListener implements Listener {
	
	@EventHandler
	public void onServerPing(ServerListPingEvent event) {
		event.setMaxPlayers(1);
		event.setMotd("DEV0 - Cryptic RPG Development Server");
	}

}
