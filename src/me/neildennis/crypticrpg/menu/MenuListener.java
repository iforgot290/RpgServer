package me.neildennis.crypticrpg.menu;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.player.CrypticPlayer;

public class MenuListener implements Listener {
	
	@EventHandler
	public void onInvClick(InventoryClickEvent event) {
		if (event.getWhoClicked().getType() != EntityType.PLAYER) return;
		CrypticPlayer pl = Cryptic.getCrypticPlayer((Player) event.getWhoClicked());
		
		ItemMenu menu = pl.getCurrentItemMenu();
		
		if (menu == null) return;
	}

}
