package me.neildennis.crypticrpg.items.metadata;

import java.util.Random;

public class Attribute {
	
	private AttributeType type;
	private int max;
	private int min;
	
	public Attribute(AttributeType type, int value){
		this(type, value, value);
	}
	
	public Attribute(AttributeType type, int max, int min){
		this.type = type;
		this.max = max;
		this.min = min;
	}
	
	public AttributeType getType(){
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
	
	public enum AttributeType {
		DAMAGE("DMG: ", "");
		
		private String pre;
		private String post;
		
		AttributeType(String pre, String post){
			this.pre = pre;
			this.post = post;
		}
		
		public String getDisplayPrefix(){
			return pre;
		}
		
		public String getDisplayPostfix(){
			return post;
		}
		
		public static AttributeType getFromString(String str){
			switch (str.toLowerCase()){
			
			case "damage": return DAMAGE;
			
			default: return null;
			
			}
		}
	}

}
