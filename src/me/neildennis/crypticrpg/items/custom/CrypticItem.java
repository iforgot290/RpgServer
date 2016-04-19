package me.neildennis.crypticrpg.items.custom;

import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonObject;

import me.neildennis.crypticrpg.items.metadata.ItemType;

public abstract class CrypticItem {
	
	protected ItemType type;
	protected int tier;
	
	protected ItemStack item;
	
	protected CrypticItem(){
		
	}
	
	protected CrypticItem(ItemType type){
		this(type, 0, 0);
	}
	
	protected CrypticItem(ItemType type, int tier, int amount){
		this.type = type;
		this.tier = tier;
		item = new ItemStack(ItemType.getMaterialFromType(type, tier), amount);
	}
	
	/*public CrypticItem(Material mat, String name, List<String> lore, List<Attribute> attribs, int tier, Rarity rare){
		this(mat, name, lore, attribs, tier, rare, 0);
	}
	
	public CrypticItem(Material mat, String name, List<String> lore, List<Attribute> attribs, int tier, Rarity rare, int level){
		this.item = new ItemStack(mat);
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
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(displayMeta);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		item.setItemMeta(meta);
	}*/
	
	public abstract void loadFromJson(JsonObject obj);
	public abstract JsonObject saveToJson();
	
	public ItemStack getItemStack(){
		return item;
	}
	
	public ItemType getType(){
		return type;
	}
	
	public int getTier(){
		return tier;
	}

}
