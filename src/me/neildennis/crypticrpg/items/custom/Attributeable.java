package me.neildennis.crypticrpg.items.custom;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import me.neildennis.crypticrpg.items.metadata.Attribute;
import me.neildennis.crypticrpg.items.metadata.ItemType;
import me.neildennis.crypticrpg.items.metadata.Attribute.AttributeType;

public abstract class Attributeable extends CrypticItem {
	
	protected List<Attribute> attribs;

	protected Attributeable(){
		super();
	}
	
	protected Attributeable(ItemType type, int tier, int amount, List<Attribute> attribs){
		super(type, tier, amount);
		
		this.attribs = attribs;
	}
	
	protected List<String> getAttributeDisplay(){
		List<String> display = new ArrayList<String>();
		
		for (Attribute attr : attribs){
			String value;
			
			if (attr.getMax() == attr.getMin()) value = String.valueOf(attr.getMax());
			else value = attr.getMin() + " - " + attr.getMax();
			
			String line = "&c" + attr.getType().getDisplayPrefix() + value + attr.getType().getDisplayPostfix();
			display.add(ChatColor.translateAlternateColorCodes('&', line));
		}
		
		return display;
	}
	
	public List<Attribute> getAttributes(){
		return attribs;
	}
	
	public Attribute getAttribute(String str){
		return getAttribute(AttributeType.getFromString(str));
	}
	
	public Attribute getAttribute(AttributeType type){
		for (Attribute attr : attribs)
			if (attr.getType() == type)
				return attr;
		return null;
	}
	
	public void setAttribute(Attribute attr){
		for (Attribute a : attribs)
			if (a.getType() == attr.getType())
				attribs.remove(a);
		
		attribs.add(attr);
	}
	
	public void removeAttribute(AttributeType type){
		for (Attribute a : attribs)
			if (a.getType() == type)
				attribs.remove(a);
	}

}
