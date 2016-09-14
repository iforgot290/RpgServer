package me.neildennis.crypticrpg.itemsnew.type;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

public abstract class CrypticItem {
	
	protected String name;
	protected List<String> lore;
	protected CrypticItemType type;
	
	public CrypticItem(String name, List<String> lore, CrypticItemType type){
		this.name = name;
		this.lore = lore;
		this.type = type;
	}
	
	public CrypticItem(){
		
	}
	
	public String getName(){
		return name;
	}
	
	public List<String> getLore(){
		return lore;
	}
	
	public CrypticItemType getType(){
		return type;
	}
	
	public abstract List<String> getBukkitDisplayLore();
	public abstract ItemStack generateItemStack();
	
	public CrypticItem getItemFromItemStack(ItemStack is){
		name = ChatColor.stripColor(is.getItemMeta().getDisplayName());
		type = CrypticItemType.fromMaterial(is.getType());
		return this;
	}

}
