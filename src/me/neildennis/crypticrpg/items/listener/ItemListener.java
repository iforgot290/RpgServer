package me.neildennis.crypticrpg.items.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.items.ItemManager;
import me.neildennis.crypticrpg.items.type.CrypticItem;
import me.neildennis.crypticrpg.items.type.armor.CrypticArmor;
import me.neildennis.crypticrpg.items.type.armor.CrypticBoots;
import me.neildennis.crypticrpg.items.type.armor.CrypticChestplate;
import me.neildennis.crypticrpg.items.type.armor.CrypticHelmet;
import me.neildennis.crypticrpg.items.type.armor.CrypticLeggings;
import me.neildennis.crypticrpg.player.CrypticPlayer;

public class ItemListener implements Listener{

	@EventHandler
	public void onArmorEquip(InventoryClickEvent event){
		if (event.getClickedInventory() == null) return;
		if (event.getClickedInventory().getType() != InventoryType.PLAYER) return;
		if (event.getWhoClicked().getType() != EntityType.PLAYER) return;

		CrypticPlayer pl = Cryptic.getCrypticPlayer((Player) event.getWhoClicked());
		PlayerInventory pinv = (PlayerInventory) event.getClickedInventory();
		pl.getStats().setNeedsUpdate();

		CrypticItem croff = ItemManager.getItemFromStack(event.getCurrentItem());
		CrypticItem cron = ItemManager.getItemFromStack(event.getCursor());
		boolean cont = false;
		boolean reverse = false;

		if (event.getSlotType() != SlotType.ARMOR){
			if (croff instanceof CrypticArmor){
				if (event.isShiftClick()){
					if (croff instanceof CrypticHelmet){
						if (pinv.getHelmet() == null) cont = true;
					} else if (croff instanceof CrypticChestplate){
						if (pinv.getChestplate() == null) cont = true;
					} else if (croff instanceof CrypticLeggings){
						if (pinv.getLeggings() == null) cont = true;
					} else if (croff instanceof CrypticBoots){
						if (pinv.getBoots() == null) cont = true;
					}

					reverse = true;
				}
			}
		} else {
			if (event.isShiftClick()){
				cron = null;
				if (hasSlot(pinv.getStorageContents()))
					cont = true;
			} else {
				cont = true;
			}
		}

		if (cont){
			if (croff != null && croff instanceof CrypticArmor){
				if (!reverse)
					pl.getStats().removeArmor((CrypticArmor) croff);
				else
					pl.getStats().addArmor((CrypticArmor) croff);
			}

			if (cron != null && cron instanceof CrypticArmor){
				if (!reverse)
					pl.getStats().addArmor((CrypticArmor) cron);
				else
					pl.getStats().removeArmor((CrypticArmor) cron);
			}

			pl.getHealthData().updateHealth();
		}

		//Log.debug(event.getSlot() + " - slot");
		//Log.debug(event.getRawSlot() + " - raw slot");
	}

	@EventHandler
	public void onInvMove(InventoryMoveItemEvent event){

	}
	
	private boolean hasSlot(ItemStack[] slots){
		for (ItemStack is : slots)
			if (is == null) return true;
		
		return false;
	}

}
