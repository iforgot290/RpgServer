package me.neildennis.crypticrpg.items.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;

import me.neildennis.crypticrpg.utils.Log;

public class ItemListener implements Listener{
	
	@EventHandler
	public void onArmorEquip(InventoryClickEvent event){
		if (event.getSlotType() != SlotType.ARMOR) return;
		if (event.getClickedInventory().getType() != InventoryType.PLAYER) return;
		
		ItemStack on = event.getCursor();
		ItemStack off = event.getCurrentItem();
		
		
		Log.debug(event.getCursor().getType().name());
		Log.debug(event.getCurrentItem().getType().name());
	}

}
