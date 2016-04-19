package me.neildennis.crypticrpg.items;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.neildennis.crypticrpg.items.misc.TeleportBook;

public class ItemListener implements Listener {
	
	@EventHandler(ignoreCancelled = true)
	public void onPlayerTPBookUse(PlayerInteractEvent event){
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		
		TeleportBook book = ItemManager.getTeleportBook(event.getItem());
		if (book != null){
			event.setCancelled(true);
		}
	}

}
