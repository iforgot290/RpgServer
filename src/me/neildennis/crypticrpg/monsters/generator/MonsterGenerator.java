package me.neildennis.crypticrpg.monsters.generator;

import java.util.HashMap;
import java.util.List;

import me.neildennis.crypticrpg.items.type.CrypticGear;
import me.neildennis.crypticrpg.items.type.CrypticItem;
import me.neildennis.crypticrpg.items.type.armor.CrypticBoots;
import me.neildennis.crypticrpg.items.type.armor.CrypticChestplate;
import me.neildennis.crypticrpg.items.type.armor.CrypticHelmet;
import me.neildennis.crypticrpg.items.type.armor.CrypticLeggings;
import me.neildennis.crypticrpg.items.type.weapon.CrypticWeapon;
import me.neildennis.crypticrpg.monsters.MobType;
import me.neildennis.crypticrpg.monsters.type.CrypticMonster;

public class MonsterGenerator {
	
	private MobType type;
	private boolean elite;
	
	private int minlvl;
	private int maxlvl;
	
	private String name;
	
	private CrypticHelmet helm;
	private CrypticChestplate chest;
	private CrypticLeggings legs;
	private CrypticBoots boots;
	private CrypticWeapon weapon;
	
	private float armorDrop = 0.0F;
	private HashMap<CrypticItem, Float> drops = new HashMap<CrypticItem, Float>();
	
	public MonsterGenerator(MobType type){
		this.type = type;
	}
	
	public MonsterGenerator setType(MobType type){
		this.type = type;
		return this;
	}
	
	public MobType getType(){
		return type;
	}
	
	public MonsterGenerator setLvlRange(int min, int max){
		this.minlvl = min;
		this.maxlvl = max;
		return this;
	}
	
	public int getMaxLvl(){
		return maxlvl;
	}
	
	public int getMinLvl(){
		return minlvl;
	}
	
	public MonsterGenerator setHelmet(CrypticHelmet helm){
		this.helm = helm;
		return this;
	}
	
	public CrypticHelmet getHelmet(){
		return helm;
	}
	
	public MonsterGenerator setChestplate(CrypticChestplate chest){
		this.chest = chest;
		return this;
	}
	
	public CrypticChestplate getChestplate(){
		return chest;
	}
	
	public MonsterGenerator setLeggings(CrypticLeggings legs){
		this.legs = legs;
		return this;
	}
	
	public CrypticLeggings getLeggings(){
		return legs;
	}
	
	public MonsterGenerator setBoots(CrypticBoots boots){
		this.boots = boots;
		return this;
	}
	
	public CrypticBoots getBoots(){
		return boots;
	}
	
	public MonsterGenerator setWeapon(CrypticWeapon weapon){
		this.weapon = weapon;
		return this;
	}
	
	public CrypticWeapon getWeapon(){
		return weapon;
	}
	
	public MonsterGenerator setGear(List<CrypticGear> monsterGear){
		for (CrypticGear gear : monsterGear){
			if (gear instanceof CrypticWeapon) weapon = (CrypticWeapon) gear;
			else if (gear instanceof CrypticHelmet) helm = (CrypticHelmet) gear;
			else if (gear instanceof CrypticChestplate) chest = (CrypticChestplate) gear;
			else if (gear instanceof CrypticLeggings) legs = (CrypticLeggings) gear;
			else if (gear instanceof CrypticBoots) boots = (CrypticBoots) gear;
		}
		
		return this;
	}
	
	public MonsterGenerator addDrop(CrypticItem item, Float chance){
		drops.put(item, chance);
		return this;
	}
	
	public MonsterGenerator addAllDrops(HashMap<CrypticItem, Float> drops){
		this.drops.putAll(drops);
		return this;
	}
	
	public MonsterGenerator setDrops(HashMap<CrypticItem, Float> drops){
		this.drops = drops;
		return this;
	}
	
	public HashMap<CrypticItem, Float> getDrops(){
		return drops;
	}
	
	public MonsterGenerator setArmorDropChance(float chance){
		this.armorDrop = chance;
		return this;
	}
	
	public float getArmorDropChance(){
		return armorDrop;
	}
	
	public MonsterGenerator setName(String name){
		this.name = name;
		return this;
	}
	
	public String getName(){
		return name;
	}
	
	public MonsterGenerator setElite(boolean elite){
		this.elite = elite;
		return this;
	}
	
	public boolean isElite(){
		return elite;
	}
	
	public CrypticMonster generate(){
		return null;
	}

}
