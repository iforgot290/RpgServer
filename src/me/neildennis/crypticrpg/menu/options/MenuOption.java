package me.neildennis.crypticrpg.menu.options;

import org.bukkit.inventory.ItemStack;

import me.neildennis.crypticrpg.player.CrypticPlayer;

public abstract class MenuOption {
	
	private int page;
	private int slot;
	
	public abstract void activate(CrypticPlayer pl);
	public abstract ItemStack getItem();
	
	public int getPage() {
		return page;
	}
	
	public void setPage(int page) {
		this.page = page;
	}
	
	public int getSlot() {
		return slot;
	}
	
	public void setSlot(int slot) {
		this.slot = slot;
	}

}
