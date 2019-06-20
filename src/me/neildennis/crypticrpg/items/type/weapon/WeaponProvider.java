package me.neildennis.crypticrpg.items.type.weapon;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.neildennis.crypticrpg.items.type.CrypticItem;
import me.neildennis.crypticrpg.items.type.CrypticItemProvider;

public class WeaponProvider extends CrypticItemProvider {

	public WeaponProvider(CrypticItem item) {
		super(item);
	}

	@Override
	public void onInventoryClick(InventoryClickEvent event) {
		
	}

	@Override
	public void onItemInteract(PlayerInteractEvent event) {
		
	}
	
	@Override
	public void onEntityInteract(PlayerInteractEntityEvent event) {
		
	}

}
