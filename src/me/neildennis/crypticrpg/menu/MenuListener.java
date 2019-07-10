package me.neildennis.crypticrpg.menu;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.menu.options.MenuOption;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.utils.Log;

public class MenuListener implements Listener {
	
	@EventHandler
	public void onInvClick(InventoryClickEvent event) {
		if (event.getWhoClicked().getType() != EntityType.PLAYER) return;
		CrypticPlayer pl = Cryptic.getCrypticPlayer((Player) event.getWhoClicked());
		
		ItemMenu menu = pl.getCurrentItemMenu();
		
		if (menu == null) return;
		
		if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) event.setCancelled(true);
		
		if (event.getClickedInventory().getType() != InventoryType.CHEST) return;
		
		event.setCancelled(true);
		int slot = event.getSlot();
		
		for (MenuOption option : menu.getOptions()) {
			if (option.getPage() == menu.getCurrentPage() && option.getSlot() == slot)
				option.activate(pl);
		}
	}
	
	@EventHandler
	public void onInvClose(InventoryCloseEvent event) {
		CrypticPlayer pl = Cryptic.getCrypticPlayer((Player) event.getPlayer());
		pl.setItemMenu(null);
	}
	
	@EventHandler
	public void onInvDrag(InventoryDragEvent event) {
		if (event.getWhoClicked().getType() != EntityType.PLAYER) return;
		CrypticPlayer pl = Cryptic.getCrypticPlayer((Player) event.getWhoClicked());
		
		ItemMenu menu = pl.getCurrentItemMenu();
		
		if (menu == null) return;
		if (event.getInventory().getType() != InventoryType.CHEST) return;
		
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onInvMove(InventoryMoveItemEvent event) {
		Log.debug("Move item");
	}

}
