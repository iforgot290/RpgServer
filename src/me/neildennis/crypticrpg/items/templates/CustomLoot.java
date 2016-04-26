package me.neildennis.crypticrpg.items.templates;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import me.neildennis.crypticrpg.items.custom.CrypticItem;
import me.neildennis.crypticrpg.items.metadata.ItemModifier;
import me.neildennis.crypticrpg.items.metadata.ItemModifier.ModifierType;
import me.neildennis.crypticrpg.items.metadata.Rarity;

public class CustomLoot {
	
	private String name;
	private Material mat;
	private int tier;
	private List<String> lore;
	private List<CustomAttribute> attribs;
	private Rarity rarity;
	
	@SuppressWarnings("deprecation")
	public CustomLoot(File file){
		if (!file.exists()) return;
		
		lore = new ArrayList<String>();
		attribs = new ArrayList<CustomAttribute>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";
			
			while ((line = reader.readLine()) != null){
				if (line.startsWith("item_name=")){
					String name = line.substring(line.indexOf("=") + 1, line.length());
					this.name = ChatColor.translateAlternateColorCodes('&', name);
				}
				
				else if (line.startsWith("item_id=")){
					int id = Integer.parseInt(line.substring(line.indexOf("=") + 1));
					mat = Material.getMaterial(id);
				}
				
				else if (line.startsWith("tier=")){
					tier = Integer.parseInt(line.replaceAll("tier=", ""));
				}
				
				else if (line.startsWith("rarity=")){
					rarity = Rarity.getFromString(line.replaceAll("rarity=", ""));
				}
				
				else if (line.startsWith("lore=")){
					lore.add(line.replaceAll("lore=", ""));
				}
				
				else {
					String[] args = line.split(" ");
					if (args.length != 2) continue;
					
					ModifierType type = ModifierType.getFromString(args[0]);
					CustomAttribute attr;
					
					if (args[1].contains("-")){
						String[] vals = args[1].split("-");
						String[] lower = vals[0].split("~");
						String[] upper = vals[1].split("~");
						
						attr = new CustomAttribute(type, parse(lower[0]), parse(lower[1]), parse(upper[0]), parse(upper[1]));
					}
					
					else {
						String[] vals = args[1].split("~");
						
						attr = new CustomAttribute(type, parse(vals[0]), parse(vals[0]), parse(vals[1]), parse(vals[1]));
					}
					
					attribs.add(attr);
				}
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int parse(String str){
		return Integer.parseInt(str);
	}
	
	public CrypticItem generateItem(){
		List<ItemModifier> attrs = new ArrayList<ItemModifier>();
		Random random = new Random();
		
		for (CustomAttribute cust : attribs){
			int lowerRange = cust.uppermin - cust.lowermin;
			int lower = cust.lowermin + random.nextInt(lowerRange);
			
			int upperRange = cust.uppermax - cust.lowermax;
			int upper = cust.lowermax + random.nextInt(upperRange);
			
			attrs.add(new ItemModifier(cust.type, upper, lower));
		}
		
		//return new CrypticItem(mat, name, lore, attrs, tier, rarity);
		return null;
	}
	
	private class CustomAttribute {
		
		public ModifierType type;
		public int lowermin, uppermin, lowermax, uppermax;
		
		private CustomAttribute(ModifierType type, int lowermin, int uppermin, int lowermax, int uppermax){
			this.type = type;
			this.lowermin = lowermin;
			this.uppermin = uppermin;
			this.lowermax = lowermax;
			this.uppermax = uppermax;
		}
		
	}

}
