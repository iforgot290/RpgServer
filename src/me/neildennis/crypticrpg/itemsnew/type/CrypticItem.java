package me.neildennis.crypticrpg.itemsnew.type;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public abstract class CrypticItem {
	
	private String name;
	private List<String> lore;
	private CrypticItemType type;
	
	public CrypticItem(String name, List<String> lore, CrypticItemType type){
		this.name = name;
		this.lore = lore;
		this.type = type;
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
	public abstract CrypticItem getItemFromItemStack(ItemStack is);
	public abstract ItemStack generateItemStack();

}
