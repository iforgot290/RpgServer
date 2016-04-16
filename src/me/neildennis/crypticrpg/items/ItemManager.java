package me.neildennis.crypticrpg.items;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.neildennis.crypticrpg.items.metadata.Attribute;

public class ItemManager {
	
	private String itempath = "plugins/CrypticRPG/Items/";
	
	private HashMap<String, CustomLoot> loot;
	
	public ItemManager(){
		loot = new HashMap<String, CustomLoot>();
		
		File folder = new File(itempath);
		folder.mkdirs();
		
		if (folder.isDirectory()){
			for (File file : folder.listFiles()){
				if (!file.getName().endsWith(".template")) continue;
				loot.put(file.getName().replaceAll(".template", ""), new CustomLoot(file));
			}
		}
	}
	
	public static ChatColor getTierColor(int tier){
		switch (tier){
		
		case 1: return ChatColor.WHITE;
		case 2: return ChatColor.GREEN;
		case 3: return ChatColor.BLUE;
		case 4: return ChatColor.LIGHT_PURPLE;
		case 5: return ChatColor.YELLOW;
		
		default: return ChatColor.WHITE;
		
		}
	}
	
	public HashMap<String, CustomLoot> getAllCustomLoot(){
		return loot;
	}
	
	public CustomLoot getCustomLoot(String str){
		return loot.get(loot);
	}
	
	public CrypticItem getCrypticItem(ItemStack stack){
		if (stack.getAmount() > 1) return null;
		
		String name;
		List<String> lore;
		List<Attribute> attrs;
		
		
		ItemMeta meta = stack.getItemMeta();
		String displayName = meta.getDisplayName();
		
		if (ChatColor.stripColor(displayName).startsWith("[+")){
			displayName = displayName.split("] ")[1];
		}
		
		return null;
	}

}
