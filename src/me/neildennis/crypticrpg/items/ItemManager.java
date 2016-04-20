package me.neildennis.crypticrpg.items;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.items.templates.CustomLoot;

public class ItemManager {

	private String filepath = "plugins/CrypticRPG/";

	private HashMap<String, CustomLoot> loot;

	public ItemManager(){
		loadLootTemplates();
		
		Bukkit.getPluginManager().registerEvents(new ItemListener(), Cryptic.getPlugin());
	}

	private void loadLootTemplates(){
		loot = new HashMap<String, CustomLoot>();

		File folder = new File(filepath + "loot/");
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

}
