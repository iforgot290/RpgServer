package me.neildennis.crypticrpg.items.type;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class CrypticItemProvider {
	
	protected CrypticItem item;
	
	public CrypticItemProvider(CrypticItem item) {
		this.item = item;
	}
	
	public abstract void onInventoryClick(InventoryClickEvent event);
	public abstract void onItemInteract(PlayerInteractEvent event);
	public abstract void onEntityInteract(PlayerInteractEntityEvent event);

}
