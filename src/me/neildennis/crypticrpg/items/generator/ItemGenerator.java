package me.neildennis.crypticrpg.items.generator;

import java.util.ArrayList;
import java.util.List;

import me.neildennis.crypticrpg.items.custom.CrypticGear;
import me.neildennis.crypticrpg.items.custom.CrypticItem;
import me.neildennis.crypticrpg.items.metadata.ItemModifier;
import me.neildennis.crypticrpg.items.metadata.ItemType;
import me.neildennis.crypticrpg.items.metadata.Rarity;

public class ItemGenerator {
	
	private ItemType type;
	private String name;
	private List<String> lore;
	private Rarity rarity;
	private List<ItemModifier> mods;
	private int lvl;
	
	public ItemGenerator(){
		this(ItemType.SWORD, 1);
	}
	
	public ItemGenerator(ItemType type, int lvl){
		lore = new ArrayList<String>();
		rarity = Rarity.COMMON;
		name = "Default";
		this.lvl = lvl;
		this.type = type;
	}
	
	public CrypticGear generate(){
		mods = new ModifierGenerator(type).setLevel(lvl).setRarity(rarity).generate();
		
		try {
			CrypticItem item = type.getHandleClass().newInstance();
			if (item instanceof CrypticGear){
				CrypticGear gear = (CrypticGear)item;
				gear.generate(this);
				return gear;
			}
			return null;
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ItemGenerator setType(ItemType type){
		this.type = type;
		return this;
	}
	
	public ItemGenerator setName(String name){
		this.name = name;
		return this;
	}
	
	public ItemGenerator setLore(List<String> lore){
		this.lore = lore;
		return this;
	}
	
	public ItemGenerator setLevel(int lvl){
		this.lvl = lvl;
		return this;
	}
	
	public ItemGenerator setRarity(Rarity rarity){
		this.rarity = rarity;
		return this;
	}
	
	public ItemType getType(){
		return type;
	}
	
	public String getName(){
		return name;
	}
	
	public List<String> getLore(){
		return lore;
	}
	
	public Rarity getRarity(){
		return rarity;
	}
	
	public int getLevel(){
		return lvl;
	}
	
	public List<ItemModifier> getModifiers(){
		return mods;
	}

}
