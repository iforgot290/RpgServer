package me.neildennis.crypticrpg.items.misc;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.neildennis.crypticrpg.items.ItemInterface;
import me.neildennis.crypticrpg.items.metadata.ItemType;

public class TeleportBook implements ItemInterface{

	private String name;
	private List<String> lore;
	private boolean chaotic;
	private Location loc;
	
	private ItemStack item;
	
	public TeleportBook(String name, List<String> lore, boolean chaotic, Location loc){
		item = new ItemStack(Material.BOOK);
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + ChatColor.BOLD.toString() + name);
		meta.getLore().clear();
		
		for (String str : lore){
			meta.getLore().add(ChatColor.WHITE + str);
		}
		
		if (chaotic){
			meta.getLore().add(ChatColor.RED + ChatColor.BOLD.toString() + "CHAOTIC");
		}
		
		item.setItemMeta(meta);
	}
	
	public TeleportBook(ItemStack stack, List<String> lore, boolean chaotic, Location loc){
		this.item = stack;
	}
	
	@Override
	public String getSaveString() {
		return null;
	}

	@Override
	public ItemType getItemType() {
		return ItemType.TELEPORT_BOOK;
	}

	@Override
	public ItemStack getBukkitItem() {
		return item;
	}
	
	public static TeleportBook getTeleportBook(ItemStack stack){
		if (stack.getType() != Material.BOOK) return null;
		
		return null;
	}

}
