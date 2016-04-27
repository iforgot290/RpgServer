package me.neildennis.crypticrpg.items.generator;

import java.util.ArrayList;
import java.util.Random;

import me.neildennis.crypticrpg.items.metadata.ItemModifier;
import me.neildennis.crypticrpg.items.metadata.ItemModifier.ModifierType;
import me.neildennis.crypticrpg.items.metadata.ItemType;
import me.neildennis.crypticrpg.items.metadata.Rarity;

public class ModifierTemplate {

	private static ArrayList<ModifierTemplate> templates;

	public static void loadTemplates(){
		templates = new ArrayList<ModifierTemplate>();
		templates.add(new ModifierTemplate(ModifierType.DAMAGE, ItemType.SWORD, Rarity.UNIQUE, 0, 9, 1, 10, 20, 1000));
	}

	public static ArrayList<ModifierTemplate> getTemplates(){
		return templates;
	}

	private ModifierType type;
	private ItemType itype;
	private Rarity rarity;
	private int minlvl;
	private int maxlvl;
	private int chance;

	private int lowmin;
	private int highmin;
	private int max;

	public ModifierTemplate(ModifierType type, ItemType itype, Rarity rarity, int minlvl, int maxlvl, int lowmin, int highmin, int max, int chance){
		this.type = type;
		this.itype = itype;
		this.minlvl = minlvl;
		this.maxlvl = maxlvl;
		this.lowmin = lowmin;
		this.highmin = highmin;
		this.max = max;
		this.chance = chance;
		this.rarity = rarity;
	}

	public boolean applies(ItemType type, Rarity rarity, int lvl){
		if (this.itype != type) return false;
		if (lvl < minlvl || lvl > maxlvl) return false;
		if (this.rarity != null && this.rarity != rarity) return false;
		Random random = new Random();
		if (random.nextInt(1000) >= chance) return false;
		return true;
	}

	public ItemModifier getModifier(int lvl){
		Random random = new Random();

		int lower = lvl + lowmin + random.nextInt(highmin - lowmin);
		int upper = lower + random.nextInt(max - lower);
		ItemModifier mod = new ItemModifier(type, lower, upper);
		
		return mod;
	}

}
