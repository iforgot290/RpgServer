package me.neildennis.crypticrpg.items;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.neildennis.crypticrpg.items.custom.CrypticItem;
import me.neildennis.crypticrpg.items.custom.Interactable;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;
import me.neildennis.crypticrpg.utils.Log;

public class ItemListener implements Listener {
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		CrypticPlayer pl = PlayerManager.getCrypticPlayer(event.getPlayer());
		CrypticItem item = pl.getItemData().getCrypticItem(event.getItem());
		if (item != null){
			if (item instanceof Interactable){
				Interactable inter = (Interactable) item;
				inter.onInteract(event);
			}
		}
	}
	
	@EventHandler
	public void trackInventoryEvent(InventoryClickEvent event){
		
	}

}
