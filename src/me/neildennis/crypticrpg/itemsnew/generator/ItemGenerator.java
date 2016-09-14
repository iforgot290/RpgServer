package me.neildennis.crypticrpg.itemsnew.generator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

import me.neildennis.crypticrpg.itemsnew.attribs.Attribute;
import me.neildennis.crypticrpg.itemsnew.attribs.Rarity;
import me.neildennis.crypticrpg.itemsnew.attribs.Tier;
import me.neildennis.crypticrpg.itemsnew.type.CrypticItemType;
import me.neildennis.crypticrpg.itemsnew.type.weapon.CrypticSword;
import me.neildennis.crypticrpg.itemsnew.type.weapon.CrypticWeapon;

public class ItemGenerator {
	
	private String name;
	private List<String> lore;
	private CrypticItemType type;
	private HashMap<Attribute, Integer> attribs = new HashMap<Attribute, Integer>();
	private Tier tier = Tier.ONE;
	private Rarity rarity = Rarity.COMMON;
	
	public ItemGenerator(CrypticItemType type){
		this.type = type;
	}
	
	public static CrypticWeapon generateWeapon(CrypticItemType type, Tier tier){
		for (Constructor<?> cons : type.getHandleClass().getDeclaredConstructors()){
			if (cons.getParameterTypes().length == 0){
				
			}
		}
		return null;
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
	
	public ItemGenerator setAttribute(Attribute attr, int value){
		attribs.put(attr, value);
		return this;
	}
	
	public boolean hasAttribute(Attribute attr){
		return attribs.containsKey(attr);
	}
	
	public int getAttribute(Attribute attr){
		return attribs.get(attr);
	}
	
	public CrypticWeapon generate(){
		if (name == null) name = NameGenerator.generateName(this);
		
		for (Constructor<?> cons : type.getHandleClass().getDeclaredConstructors()){
			if (cons.getParameterTypes().length == 0) continue;
			
			try {
				return (CrypticWeapon) cons.newInstance(name, lore, type, attribs, tier, rarity);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				//TODO: take this out of production
				e.printStackTrace();
				return new CrypticSword("Invalid generator", null, CrypticItemType.ASDF, null, Tier.ONE, Rarity.COMMON);
			}
		}
		
		return new CrypticSword("Invalid constructor", null, CrypticItemType.ASDF, null, Tier.ONE, Rarity.COMMON);
	}

}
