package me.neildennis.crypticrpg.items;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;

import com.google.gson.JsonArray;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.items.custom.CrypticItem;
import me.neildennis.crypticrpg.items.generator.ItemGenerator;
import me.neildennis.crypticrpg.items.generator.ModifierTemplate;
import me.neildennis.crypticrpg.items.metadata.ItemType;
import me.neildennis.crypticrpg.items.metadata.Rarity;
import me.neildennis.crypticrpg.items.templates.CustomLoot;

public class ItemManager {

	private String filepath = "plugins/CrypticRPG/";

	private HashMap<String, CustomLoot> loot;

	public ItemManager(){
		loadLootTemplates();
		ModifierTemplate.loadTemplates();
		
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

	public HashMap<String, CustomLoot> getAllCustomLoot(){
		return loot;
	}

	public CustomLoot getCustomLoot(String str){
		return loot.get(loot);
	}
	
	public static ArrayList<ItemGenerator> generateMobArmor(int level){
		ArrayList<ItemGenerator> gear = new ArrayList<ItemGenerator>();
		Random random = new Random();
		
		gear.add(new ItemGenerator().setLevel(level).setRarity(Rarity.COMMON).setType(ItemType.HELMET));
		gear.add(new ItemGenerator().setLevel(level).setRarity(Rarity.COMMON).setType(ItemType.CHESTPLATE));
		gear.add(new ItemGenerator().setLevel(level).setRarity(Rarity.COMMON).setType(ItemType.LEGGINGS));
		gear.add(new ItemGenerator().setLevel(level).setRarity(Rarity.COMMON).setType(ItemType.BOOTS));
		
		
		return gear;
	}
	
	public static ArrayList<CrypticItem> loadItemsFromJson(JsonArray json){
		return null;
	}

}
