package me.neildennis.crypticrpg.items.attribs;

import java.util.Random;

public class Attribute {

	private AttributeType type;
	private int high, low;
	
	public Attribute(AttributeType type){
		this(type, 0, 0);
	}
	
	public Attribute(AttributeType type, int[] values){
		this(type, values[0], values[1]);
	}
	
	public Attribute(AttributeType type, int value){
		this(type, value, value);
	}
	
	public Attribute(AttributeType type, int low, int high){
		this.type = type;
		this.low = low;
		this.high = high;
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
	
	public void setLow(int low){
		this.low = low;
	}
	
	public int getHigh(){
		return high;
	}
	
	public void setHigh(int high){
		this.high = high;
	}
	
}
