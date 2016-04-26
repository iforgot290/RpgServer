package me.neildennis.crypticrpg.items.metadata;

import java.util.Random;

public class ItemModifier {
	
	private ModifierType type;
	private int max;
	private int min;
	private Rarity rarity;
	
	public ItemModifier(ModifierType type, int value){
		this(type, value, value);
	}
	
	public ItemModifier(ModifierType type, int max, int min){
		this.type = type;
		this.max = max;
		this.min = min;
	}
	
	public ModifierType getType(){
		return type;
	}
	
	public int getValue(){
		if (max == min) return max;
		
		Random rand = new Random();
		int range = max - min;
		int val = rand.nextInt(range);
		
		return min + val;
	}
	
	public int getMax(){
		return max;
	}
	
	public int getMin(){
		return min;
	}
	
	public void setRarity(Rarity rarity){
		this.rarity = rarity;
	}
	
	public Rarity getRarity(){
		return rarity;
	}
	
	public enum ModifierType {
		DAMAGE("DMG: ", "");
		
		private String pre;
		private String post;
		
		ModifierType(String pre, String post){
			this.pre = pre;
			this.post = post;
		}
		
		public String getDisplayPrefix(){
			return pre;
		}
		
		public String getDisplayPostfix(){
			return post;
		}
		
		public static ModifierType getFromString(String str){
			switch (str.toLowerCase()){
			
			case "damage": return DAMAGE;
			
			default: return null;
			
			}
		}
	}

}
