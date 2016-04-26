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
		templates.add(new ModifierTemplate(ModifierType.DAMAGE, ItemType.SWORD, 0, 9, 1, 10, 20, 1000));
	}

	public static ArrayList<ModifierTemplate> getTemplates(){
		return templates;
	}

	private ModifierType type;
	private ItemType itype;
	private int minlvl;
	private int maxlvl;
	private int chance;

	private int lowmin;
	private int highmin;
	private int max;

	public ModifierTemplate(ModifierType type, ItemType itype, int minlvl, int maxlvl, int lowmin, int highmin, int max, int chance){

	}

	public boolean applies(ItemType type, int lvl){
		if (this.itype != type) return false;
		if (lvl < minlvl || lvl > maxlvl) return false;

		Random random = new Random();
		if (random.nextInt(1000) >= chance) return false;

		return true;
	}

	public ItemModifier getModifier(){
		Random random = new Random();

		int lower = lowmin + random.nextInt(highmin - lowmin);
		int upper = lower + random.nextInt(max - lower);
		ItemModifier mod = new ItemModifier(type, lower, upper);

		if (type == ModifierType.DAMAGE){
			
			int lc = ((lower - lowmin) * 100) / highmin - lowmin;
			int hc = ((upper - lowmin) * 100) / max - lowmin;
			
			Rarity rarity = Rarity.UNIQUE;
			int c = (lc + hc) / 2;
			
			if (c <= 60) rarity = Rarity.COMMON;
			else if (c <= 85) rarity = Rarity.UNCOMMON;
			else if (c <= 98) rarity = Rarity.RARE;
			
			mod.setRarity(rarity);
		}
		
		return mod;
	}

}
