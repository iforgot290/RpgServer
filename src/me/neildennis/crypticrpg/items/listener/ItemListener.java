package me.neildennis.crypticrpg.items.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;

import me.neildennis.crypticrpg.items.ItemManager;
import me.neildennis.crypticrpg.items.attribs.AttributeType;
import me.neildennis.crypticrpg.items.type.CrypticItem;
import me.neildennis.crypticrpg.items.type.armor.CrypticArmor;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;
import me.neildennis.crypticrpg.utils.Log;
import me.neildennis.crypticrpg.zone.ZoneManager.ZoneState;

public class ItemListener implements Listener{
	
	@EventHandler
	public void onArmorEquip(InventoryClickEvent event){
		if (event.getSlotType() != SlotType.ARMOR) return;
		if (event.getClickedInventory().getType() != InventoryType.PLAYER) return;
		if (event.getWhoClicked().getType() != EntityType.PLAYER) return;
		
		CrypticPlayer pl = PlayerManager.getCrypticPlayer((Player) event.getWhoClicked());
		
		ItemStack on = event.getCursor();
		ItemStack off = event.getCurrentItem();
		
		double oldhealth = pl.getPlayer().getMaxHealth();
		double newhealth = oldhealth;
		
		CrypticItem croff = ItemManager.getItemFromStack(off);
		CrypticItem cron = ItemManager.getItemFromStack(on);
		
		if (croff != null && croff instanceof CrypticArmor){
			CrypticArmor armor = (CrypticArmor) croff;
			newhealth -= armor.getAttribute(AttributeType.HEALTH).genValue();
		}
		
		if (cron != null && cron instanceof CrypticArmor){
			CrypticArmor armor = (CrypticArmor) cron;
			newhealth += armor.getAttribute(AttributeType.HEALTH).genValue();
		}
		
		pl.getPlayer().setMaxHealth(newhealth > 50 ? newhealth : 50);
		
		if (oldhealth < newhealth && pl.getZoneState() == ZoneState.PEACEFUL){
			pl.getPlayer().setHealth(pl.getPlayer().getHealth() + (oldhealth - newhealth));
		}
		
		pl.getHealthData().updateOverheadHP();
		
		Log.debug(event.getCursor().getType().name());
		Log.debug(event.getCurrentItem().getType().name());
	}

}
