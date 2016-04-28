package me.neildennis.crypticrpg.items.custom;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import me.neildennis.crypticrpg.items.metadata.ItemModifier;
import me.neildennis.crypticrpg.items.metadata.ItemModifier.ModifierType;
import me.neildennis.crypticrpg.utils.Log;
import me.neildennis.crypticrpg.items.metadata.ItemType;

public abstract class Attributeable extends CrypticItem {
	
	protected List<ItemModifier> attribs;

	protected Attributeable(){
		super();
	}
	
	protected Attributeable(ItemType type, int amount, List<ItemModifier> attribs){
		super(type, amount);
		
		this.attribs = attribs;
	}
	
	protected List<String> getAttributeDisplay(){
		List<String> display = new ArrayList<String>();
		
		for (ItemModifier attr : attribs){
			String value;
			
			if (attr.getMax() == attr.getMin()) value = String.valueOf(attr.getMax());
			else value = attr.getMin() + " - " + attr.getMax();
			
			String line = "&c" + attr.getType().getDisplayPrefix() + value + attr.getType().getDisplayPostfix();
			display.add(ChatColor.translateAlternateColorCodes('&', line));
		}
		
		return display;
	}
	
	public List<ItemModifier> getAttributes(){
		return attribs;
	}
	
	public ItemModifier getAttribute(String str){
		return getAttribute(ModifierType.getFromString(str));
	}
	
	public ItemModifier getAttribute(ModifierType type){
		for (ItemModifier attr : attribs)
			if (attr.getType() == type)
				return attr;
		Log.debug("Type not found");
		return null;
	}
	
	public void setAttribute(ItemModifier attr){
		for (ItemModifier a : attribs)
			if (a.getType() == attr.getType())
				attribs.remove(a);
		
		attribs.add(attr);
	}
	
	public void removeAttribute(ModifierType type){
		for (ItemModifier a : attribs)
			if (a.getType() == type)
				attribs.remove(a);
	}

}
