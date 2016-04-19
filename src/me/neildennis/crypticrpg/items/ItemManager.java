package me.neildennis.crypticrpg.items;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.items.custom.CrypticItem;
import me.neildennis.crypticrpg.items.custom.TeleportBook;
import me.neildennis.crypticrpg.items.templates.CustomLoot;

public class ItemManager {

	private String filepath = "plugins/CrypticRPG/";

	private HashMap<String, CustomLoot> loot;
	private static List<TeleportBook> tpbooks;
	
	private static HashMap<ItemStack, CrypticItem> items;

	public ItemManager(){
		loadLootTemplates();
		loadTeleportBooks();
		
		items = new HashMap<ItemStack, CrypticItem>();
		
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
	
	private void loadTeleportBooks(){
		tpbooks = new ArrayList<TeleportBook>();
		
		File folder = new File(filepath + "tpbooks/");
		folder.mkdirs();
		
		if (folder.isDirectory()){
			for (File file : folder.listFiles()){
				if (!file.getName().endsWith(".tpbook")) continue;
				
			}
		}
	}
	
	public static CrypticItem getCrypticItem(ItemStack stack){
		return items.get(stack);
	}
	
	public static void trackItem(CrypticItem item){
		items.put(item.getItemStack(), item);
	}
	
	public static void untrackItem(CrypticItem item){
		items.remove(item.getItemStack());
	}
	
	public static void untrackItem(ItemStack stack){
		items.remove(stack);
	}
	
	public static TeleportBook getTeleportBook(ItemStack stack){
		if (stack.getType() != Material.BOOK) return null;
		
		ItemMeta meta = stack.getItemMeta();
		if (meta == null) return null;
		
		return getTeleportBook(ChatColor.stripColor(meta.getDisplayName()));
	}
	
	public static TeleportBook getTeleportBook(String name){
		for (TeleportBook book : tpbooks)
			if (book.getName().equalsIgnoreCase(name))
				return book;
		return null;
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
