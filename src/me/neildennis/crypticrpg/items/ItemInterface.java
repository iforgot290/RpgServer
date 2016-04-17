package me.neildennis.crypticrpg.items;

import org.bukkit.inventory.ItemStack;

import me.neildennis.crypticrpg.items.metadata.ItemType;

public interface ItemInterface {
	
	public String getSaveString();
	public ItemType getItemType();
	public ItemStack getBukkitItem();

}
