package me.neildennis.crypticrpg.menu.options;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.neildennis.crypticrpg.menu.ItemMenu;
import me.neildennis.crypticrpg.player.CrypticPlayer;

public class PageOption extends MenuOption {

	private ItemMenu menu;
	private int currentPage;
	private boolean forward;
	
	private ItemStack item;
	
	public PageOption(ItemMenu menu, int currentPage, boolean forward) {
		this.menu = menu;
		this.currentPage = currentPage;
		this.forward = forward;
		
		this.item = new ItemStack(Material.ARROW);
		setup();
	}
	
	@Override
	public void activate(CrypticPlayer pl) {
		
	}
	
	@Override
	public ItemStack getItem() {
		return item;
	}
	
	private void setup() {
		item = new ItemStack(Material.ARROW);
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RESET + (forward ? "Next Page" : "Previous Page"));
		item.setItemMeta(meta);
	}
	
	public ItemMenu getItemMenu() {
		return menu;
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	
	public boolean isForward() {
		return forward;
	}

}
