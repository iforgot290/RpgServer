package me.neildennis.crypticrpg.items.generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.neildennis.crypticrpg.items.attribs.Attribute;
import me.neildennis.crypticrpg.items.attribs.AttributeType;
import me.neildennis.crypticrpg.items.attribs.Rarity;
import me.neildennis.crypticrpg.items.attribs.Tier;
import me.neildennis.crypticrpg.items.type.CrypticGear;
import me.neildennis.crypticrpg.items.type.CrypticItemType;
import me.neildennis.crypticrpg.items.type.weapon.CrypticSword;

public class ItemGenerator {
	
	private String name;
	private List<String> lore;
	private CrypticItemType type;
	private ArrayList<Attribute> attribs;
	private Tier tier = Tier.ONE;
	private Rarity rarity = Rarity.COMMON;
	
	public ItemGenerator(CrypticItemType type){
		this.type = type;
		attribs = new ArrayList<Attribute>();
	}
	
	public ItemGenerator setType(CrypticItemType type){
		this.type = type;
		return this;
	}
	
	public CrypticItemType getType(){
		return type;
	}
	
	public ItemGenerator setTier(Tier tier){
		this.tier = tier;
		return this;
	}
	
	public Tier getTier(){
		return tier;
	}
	
	public ItemGenerator setRarity(Rarity rarity){
		this.rarity = rarity;
		return this;
	}
	
	public Rarity getRarity(){
		return rarity;
	}
	
	public ItemGenerator setAttribute(AttributeType attr, int[] values){
		attribs.add(new Attribute(attr, values));
		return this;
	}
	
	public boolean hasAttribute(AttributeType attr){
		return getAttribute(attr) != null;
	}
	
	public Attribute getAttribute(AttributeType attr){
		for (Attribute attrib : attribs)
			if (attrib.getType().equals(attr))
				return attrib;
		return null;
	}
	
	public CrypticGear generate(){
		Random r = new Random();
		
		if (name == null) name = NameGenerator.generateName(this);
		if (tier == null) tier = Tier.ONE;
		if (rarity == null) rarity = Rarity.getByPct(r.nextFloat());
		
		AttribGenerator.generate(this);
		
		for (Constructor<?> cons : type.getHandleClass().getDeclaredConstructors()){
			if (cons.getParameterTypes().length == 0) continue;
			
			try {
				return (CrypticGear) cons.newInstance(name, lore, type, attribs, tier, rarity);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				//TODO: take this out of production
				e.printStackTrace();
				return new CrypticSword("Invalid generator", null, CrypticItemType.SWORD, null, Tier.ONE, Rarity.COMMON);
			}
		}
		
		return new CrypticSword("Invalid constructor", null, CrypticItemType.SWORD, null, Tier.ONE, Rarity.COMMON);
	}

}
