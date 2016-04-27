package me.neildennis.crypticrpg.items;

import org.bukkit.inventory.ItemStack;

import me.neildennis.crypticrpg.items.custom.CrypticItem;

public class ItemTracker {
	
	private CrypticItem citem;
	private ItemStack stack;
	
	public ItemTracker(ItemStack stack, CrypticItem citem){
		this.citem = citem;
		this.stack = stack;
	}
	
	public CrypticItem getCrypticItem(){
		return citem;
	}
	
	public ItemStack getItemStack(){
		return stack;
	}

}
