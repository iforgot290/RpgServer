package me.neildennis.crypticrpg.itemsnew.type.weapon;

import java.util.HashMap;
import java.util.List;

import me.neildennis.crypticrpg.itemsnew.attribs.Attribute;
import me.neildennis.crypticrpg.itemsnew.attribs.Rarity;
import me.neildennis.crypticrpg.itemsnew.attribs.Tier;
import me.neildennis.crypticrpg.itemsnew.type.CrypticItem;
import me.neildennis.crypticrpg.itemsnew.type.CrypticItemType;

public abstract class CrypticWeapon extends CrypticItem{

	private HashMap<Attribute, Integer> attribs;
	private Tier tier;
	private Rarity rarity;
	
	public CrypticWeapon(String name, List<String> lore, CrypticItemType type, HashMap<Attribute, Integer> attribs, Tier tier) {
		super(name, lore, type);
		this.attribs = attribs;
		this.tier = tier;
	}
	
	public HashMap<Attribute, Integer> getAttribs(){
		return attribs;
	}
	
	public Tier getTier(){
		return tier;
	}
	
	public Rarity getRarity(){
		return rarity;
	}
	
	@Override
	public List<String> getBukkitDisplayLore(){
		return null;
	}
	

}
