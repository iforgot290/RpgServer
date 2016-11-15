package me.neildennis.crypticrpg.monsters.generator;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

import me.neildennis.crypticrpg.items.attribs.Rarity;
import me.neildennis.crypticrpg.items.attribs.Tier;
import me.neildennis.crypticrpg.items.generator.ItemGenerator;
import me.neildennis.crypticrpg.items.type.CrypticGear;
import me.neildennis.crypticrpg.items.type.CrypticItem;
import me.neildennis.crypticrpg.items.type.CrypticItemType;
import me.neildennis.crypticrpg.items.type.armor.CrypticBoots;
import me.neildennis.crypticrpg.items.type.armor.CrypticChestplate;
import me.neildennis.crypticrpg.items.type.armor.CrypticHelmet;
import me.neildennis.crypticrpg.items.type.armor.CrypticLeggings;
import me.neildennis.crypticrpg.items.type.weapon.CrypticWeapon;
import me.neildennis.crypticrpg.monsters.MobType;
import me.neildennis.crypticrpg.monsters.type.CrypticMonster;

public class MonsterGenerator {

	private MobType type;
	private boolean elite = false;

	private int minlvl = 0;
	private int maxlvl = 0;

	private String name;

	private CrypticHelmet helm;
	private CrypticChestplate chest;
	private CrypticLeggings legs;
	private CrypticBoots boots;
	private CrypticWeapon weapon;
	
	private CrypticItemType weaponType;

	private Rarity gearRarity;
	
	private float armorDrop = 0.0F;
	private HashMap<CrypticItem, Float> drops = new HashMap<CrypticItem, Float>();
	
	private Random r;

	public MonsterGenerator(MobType type){
		this.type = type;
		this.name = type.getDefaultName();
		this.r = new Random();
	}

	public CrypticMonster generate() throws InstantiationException, IllegalAccessException{

		CrypticMonster mob = type.getHandleClass().newInstance();

		mob.setElite(elite);
		mob.setLevel(r.nextInt(maxlvl - minlvl) + minlvl);
		mob.setName(name);
		mob.setWillDrop(getArmorDropChance(mob.getTier()) > r.nextFloat());
		
		int gencounter = mob.getTier() == Tier.ONE ? r.nextInt(4) + 1 : 4;
		
		CrypticItemType weptype;
		if (weaponType == null) weptype = getRandomWeaponType(new Random().nextInt(1));
		else weptype = weaponType;
		
		Rarity rare;
		if (gearRarity == null) rare = Rarity.getByPct(new Random().nextFloat());
		else rare = gearRarity;
		
		CrypticHelmet helm;
		CrypticChestplate chest;
		CrypticLeggings legs;
		CrypticBoots boots;
		CrypticWeapon weapon;
		
		if (this.helm == null)
			helm = (CrypticHelmet) new ItemGenerator(CrypticItemType.HELMET).setLevel(mob.getLevel()).setRarity(rare).generate();
		else helm = this.helm;
		
		if (this.chest == null)
			chest = (CrypticChestplate) new ItemGenerator(CrypticItemType.CHESTPLATE).setLevel(mob.getLevel()).setRarity(rare).generate();
		else chest = this.chest;
		
		if (this.legs == null)
			legs = (CrypticLeggings) new ItemGenerator(CrypticItemType.LEGGINGS).setLevel(mob.getLevel()).setRarity(rare).generate();
		else legs = this.legs;
		
		if (this.boots == null)
			boots = (CrypticBoots) new ItemGenerator(CrypticItemType.BOOTS).setLevel(mob.getLevel()).setRarity(rare).generate();
		else boots = this.boots;
		
		if (this.weapon == null)
			weapon = (CrypticWeapon) new ItemGenerator(weptype).setLevel(mob.getLevel()).setRarity(rare).generate();
		else weapon = this.weapon;
		
		if (helm != null)
			mob.setHelmet(helm);
		if (chest != null)
			mob.setChestplate(chest);
		if (legs != null)
			mob.setLeggings(legs);
		if (boots != null)
			mob.setBoots(boots);
		if (weapon != null)
			mob.setWeapon(weapon);

		return mob;
	}
	
	private CrypticItemType getRandomWeaponType(int i){
		if (i == 0) return CrypticItemType.SWORD;
		
		return CrypticItemType.SWORD;
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
		return maxlvl == 0 ? 10 : maxlvl;
	}

	public int getMinLvl(){
		return minlvl == 0 ? 1 : minlvl;
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

	public float getArmorDropChance(Tier tier){
		if (tier == null) return armorDrop;
		return armorDrop == 0.0F ? tier.getDefaultDropPct() : armorDrop;
	}

	public MonsterGenerator setName(String name){
		if (name == null) this.name = type.getDefaultName();
		else this.name = name;
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
	
	public MonsterGenerator setGearRarity(Rarity rarity){
		this.gearRarity = rarity;
		return this;
	}
	
	public Rarity getGearRarity(){
		return gearRarity;
	}
	
	public MonsterGenerator setWeaponType(CrypticItemType type){
		this.weaponType = type;
		return this;
	}
	
	public CrypticItemType getWeaponType(){
		return weaponType;
	}

}
