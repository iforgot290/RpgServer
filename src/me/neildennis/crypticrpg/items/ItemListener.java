package me.neildennis.crypticrpg.items;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import me.neildennis.crypticrpg.items.custom.CrypticItem;
import me.neildennis.crypticrpg.items.custom.Interactable;

public class ItemListener implements Listener {
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		CrypticItem item = ItemManager.getCrypticItem(event.getItem());
		if (item != null){
			if (item instanceof Interactable){
				Interactable inter = (Interactable) item;
				inter.onInteract(event);
			}
		}
	}

}
