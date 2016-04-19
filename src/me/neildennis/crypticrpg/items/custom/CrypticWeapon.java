package me.neildennis.crypticrpg.items.custom;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.JsonObject;

import me.neildennis.crypticrpg.items.ItemManager;
import me.neildennis.crypticrpg.items.metadata.Attribute;
import me.neildennis.crypticrpg.items.metadata.ItemType;
import me.neildennis.crypticrpg.items.metadata.Rarity;

public abstract class CrypticWeapon extends Attributeable {
	
	protected String name;
	protected List<String> lore;
	protected int level;
	protected Rarity rarity;

	protected CrypticWeapon(){
		super();
	}
	
	protected CrypticWeapon(ItemType type, String name, List<String> lore, List<Attribute> attribs, int tier, Rarity rarity, int level){
		super(type, tier, 1, attribs);
		
		this.name = name;
		this.lore = lore;
		this.level = level;
		this.rarity = rarity;
		
		List<String> displayMeta = new ArrayList<String>();
		
		for (String str : lore){
			displayMeta.add(ChatColor.translateAlternateColorCodes('&', str));
		}
		
		for (String str : this.getAttributeDisplay())
			displayMeta.add(str);
		
		displayMeta.add(rarity.getColor() + rarity.getDisplay());
		
		String displayName = ItemManager.getTierColor(tier) + name;
		
		if (level > 0){
			displayName = ChatColor.RED + "[+" + level + "] " + displayName;
		}
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(displayMeta);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		
		item.setItemMeta(meta);
	}
	
	@Override
	public void loadFromJson(JsonObject obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JsonObject saveToJson() {
		// TODO Auto-generated method stub
		return null;
	}

}
