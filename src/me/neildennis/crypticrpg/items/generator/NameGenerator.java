package me.neildennis.crypticrpg.items.generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import me.neildennis.crypticrpg.items.attribs.Rarity;
import me.neildennis.crypticrpg.items.attribs.Tier;
import me.neildennis.crypticrpg.items.type.CrypticItemType;

public class NameGenerator {
	
	private static List<NameTemplate> templates;
	private static Random random;
	
	public static void load(){
		random = new Random();
		templates = new ArrayList<NameTemplate>();
		
		templates.add(new NameTemplate(CrypticItemType.SWORD, Tier.ONE, Rarity.COMMON, Arrays.asList("Sword of wood")));
	}
	
	public static String generateName(ItemGenerator gen){
		for (NameTemplate temp : templates){
			if (temp.getRarity() != gen.getRarity()) continue;
			if (temp.getTier() != gen.getTier()) continue;
			if (temp.getType() != gen.getType()) continue;
			
			return temp.getName();
		}
		
		return "Error generating name";
	}
	
	private static class NameTemplate {
		
		private CrypticItemType type;
		private Tier tier;
		private Rarity rarity;
		
		private List<String> names;
		
		public NameTemplate(CrypticItemType type, Tier tier, Rarity rarity, List<String> names){
			this.type = type;
			this.tier = tier;
			this.rarity = rarity;
			this.names = names;
		}
		
		public CrypticItemType getType(){
			return type;
		}
		
		public Tier getTier(){
			return tier;
		}
		
		public Rarity getRarity(){
			return rarity;
		}
		
		public String getName(){
			return names.get(random.nextInt(names.size()));
		}
		
	}

}
