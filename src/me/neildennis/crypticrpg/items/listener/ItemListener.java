package me.neildennis.crypticrpg.items.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import me.neildennis.crypticrpg.items.ItemManager;
import me.neildennis.crypticrpg.items.type.CrypticItem;
import me.neildennis.crypticrpg.items.type.armor.CrypticArmor;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;

public class ItemListener implements Listener{
	
	@EventHandler
	public void onArmorEquip(InventoryClickEvent event){
		if (event.getClickedInventory().getType() != InventoryType.PLAYER) return;
		if (event.getWhoClicked().getType() != EntityType.PLAYER) return;
		
		CrypticPlayer pl = PlayerManager.getCrypticPlayer((Player) event.getWhoClicked());
		pl.getStats().setNeedsUpdate();
		
		if (event.getSlotType() != SlotType.ARMOR) return;
		
		CrypticItem croff = ItemManager.getItemFromStack(event.getCurrentItem());
		CrypticItem cron = ItemManager.getItemFromStack(event.getCursor());
		
		if (croff != null && croff instanceof CrypticArmor){
			pl.getStats().removeArmor((CrypticArmor) croff);
		}
		
		if (cron != null && cron instanceof CrypticArmor){
			pl.getStats().addArmor((CrypticArmor) cron);
		}
		
		pl.getHealthData().updateHealth();
	}

}
