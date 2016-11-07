package me.neildennis.crypticrpg.items.attribs;

import java.util.Random;

public class Attribute {

	private AttributeType type;
	private int high, low;
	
	public Attribute(AttributeType type, int[] values){
		this.type = type;
		this.high = values[1];
		this.low = values[0];
	}
	
	public Attribute(AttributeType type, int low, int high){
		this.type = type;
		this.high = high;
		this.low = low;
	}
	
	public Attribute(AttributeType type, int value){
		this.type = type;
		this.high = value;
		this.low = value;
	}
	
	public String format(){
		if (high == low) return String.valueOf(low);
		
		return low + " - " + high;
	}
	
	public int genValue(){
		Random r = new Random();
		return high - low > 0 ? r.nextInt(high - low) + low : low;
	}
	
	public AttributeType getType(){
		return type;
	}
	
	public int getLow(){
		return low;
	}
	
	public int getHigh(){
		return high;
	}
	
}
