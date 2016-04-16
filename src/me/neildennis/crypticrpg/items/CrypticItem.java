package me.neildennis.crypticrpg.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.neildennis.crypticrpg.items.metadata.Attribute;
import me.neildennis.crypticrpg.items.metadata.Attribute.AttributeType;
import me.neildennis.crypticrpg.items.metadata.Rarity;

public class CrypticItem extends ItemStack{
	
	private String name;
	private List<String> lore;
	private List<Attribute> attribs;
	private int tier;
	private Rarity rare;
	
	public CrypticItem(Material mat, String name, List<String> lore, List<Attribute> attribs, int tier, Rarity rare){
		this(mat, name, lore, attribs, tier, rare, 0);
	}
	
	public CrypticItem(Material mat, String name, List<String> lore, List<Attribute> attribs, int tier, Rarity rare, int level){
		super(mat);
		
		this.name = name;
		this.lore = lore;
		this.attribs = attribs;
		
		List<String> displayMeta = new ArrayList<String>();
		
		for (String str : lore){
			displayMeta.add(ChatColor.translateAlternateColorCodes('&', str));
		}
		
		for (Attribute attr : attribs){
			String value;
			
			if (attr.getMax() == attr.getMin()) value = String.valueOf(attr.getMax());
			else value = attr.getMin() + " - " + attr.getMax();
			
			String line = "&c" + attr.getType().getDisplayPrefix() + value + attr.getType().getDisplayPostfix();
			displayMeta.add(ChatColor.translateAlternateColorCodes('&', line));
		}
		
		displayMeta.add(rare.getColor() + rare.getDisplay());
		
		String displayName = ItemManager.getTierColor(tier) + name;
		
		if (level > 0){
			displayName = ChatColor.RED + "[+" + level + "] " + displayName;
		}
		
		ItemMeta meta = this.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(displayMeta);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		this.setItemMeta(meta);
	}
	
	public List<Attribute> getAttribs(){
		return attribs;
	}
	
	public Attribute getAttribute(String str){
		return getAttribute(AttributeType.getFromString(str));
	}
	
	public Attribute getAttribute(AttributeType type){
		for (Attribute attr : attribs)
			if (attr.getType() == type)
				return attr;
		return null;
	}
	
	public String getName(){
		return name;
	}
	
	public List<String> getBaseLore(){
		return lore;
	}
	
	public int getTier(){
		return tier;
	}
	
	public Rarity getRarity(){
		return rare;
	}

}
