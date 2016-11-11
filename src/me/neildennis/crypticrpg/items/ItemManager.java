package me.neildennis.crypticrpg.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.io.FileReader;
import org.json.JSONTokener;
import org.json.JSONObject;
import org.json.JSONArray;

import org.bukkit.inventory.ItemStack;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.Manager;
import me.neildennis.crypticrpg.items.attribs.AttributeType;
import me.neildennis.crypticrpg.items.generator.ItemGenerator;
import me.neildennis.crypticrpg.items.generator.NameGenerator;
import me.neildennis.crypticrpg.items.generator.modifiers.ItemModifier;
import me.neildennis.crypticrpg.items.generator.modifiers.TierModifier;
import me.neildennis.crypticrpg.items.generator.modifiers.TierModifier.ModifierType;
import me.neildennis.crypticrpg.items.listener.ItemListener;
import me.neildennis.crypticrpg.items.type.CrypticItem;
import me.neildennis.crypticrpg.items.type.CrypticItemType;
import me.neildennis.crypticrpg.items.type.armor.CrypticBoots;
import me.neildennis.crypticrpg.items.type.armor.CrypticChestplate;
import me.neildennis.crypticrpg.items.type.armor.CrypticHelmet;
import me.neildennis.crypticrpg.items.type.armor.CrypticLeggings;
import me.neildennis.crypticrpg.items.type.weapon.CrypticSword;
import me.neildennis.crypticrpg.utils.Log;

public class ItemManager extends Manager{
	
	private static HashMap<AttributeType, ItemModifier> mods;
	
	public ItemManager(){
		NameGenerator.load();
		Cryptic.getPlugin().getServer().getPluginManager().registerEvents(new ItemListener(), Cryptic.getPlugin());
		loadMods();
	}
	
	@Override
	public void registerTasks() {
		
	}
	
	public void loadMods(){
		mods = new HashMap<AttributeType, ItemModifier>();
		
		ArrayList<TierModifier> tiermods = new ArrayList<TierModifier>();
		tiermods.add(new TierModifier(ModifierType.RANGE, 0, 19, 15, 30, 3));
		
		ArrayList<Class<? extends CrypticItem>> possible = new ArrayList<Class<? extends CrypticItem>>();
		possible.add(CrypticSword.class);
		
		ItemModifier mod = new ItemModifier(AttributeType.DAMAGE, tiermods, possible, 1.0F);
		
		mods.put(AttributeType.DAMAGE, mod);
		
		ArrayList<TierModifier> armorMods = new ArrayList<TierModifier>();
		armorMods.add(new TierModifier(ModifierType.STATIC, 0, 19, 10, 20));
		
		ArrayList<Class<? extends CrypticItem>> possibleArmor = new ArrayList<Class<? extends CrypticItem>>();
		possibleArmor.add(CrypticHelmet.class);
		possibleArmor.add(CrypticChestplate.class);
		possibleArmor.add(CrypticLeggings.class);
		possibleArmor.add(CrypticBoots.class);
		
		ItemModifier modArmor = new ItemModifier(AttributeType.HEALTH, armorMods, possibleArmor, 1.0F);
		
		mods.put(AttributeType.HEALTH, modArmor);
	}
	
	/** Load the mods map from a json file using json.org library */
	public void loadFromJSON(String fileName) throws Exception {
	    JSONTokener tokener = new JSONTokener(new FileReader(fileName));
	    JSONObject root = new JSONObject(tokener);
	    JSONArray itemmods = root.get("itemmods");
	    for(Object obj : itemmods) {
	    	JSONObject itemmod = (JSONObject) itemmod;

	    	// load tier mods
	    	JSONArray jsontiermods = itemmod.get("tiermods");
	    	ArrayList<TierModifier> tiermods = new ArrayList<TierModifier>();
	    	for(JSONObject mod : jsontiermods){
	    		ModifierType type = ModifierType.valueOf(mod.getString("modtype"));
	    		tiermods.add(new TierModifier(type, mod.getInt("minlvl"), mod.getInt("maxlvl"), mod.getInt("low"), mod.getInt("high"), mod.getInt("mid")));
	    	}

	    	// load possible items
	    	JSONArray jsonpossible = itemmod.get("possible");
	    	ArrayList<Class<? extends CrypticItem>> possible = new ArrayList<Class<? extends CrypticItem>>();
	    	for(Object pos : jsonpossible){
	    		possible.add(Class.forName((String) pos));
	    	}

	    	// load exclude items
	    	JSONArray jsonexclude = itemmod.get("exclude");
	    	ArrayList<Class<? extends CrypticItem>> exclude = new ArrayList<Class<? extends CrypticItem>>();
	    	for(Object exc : jsonexclude){
	    		exclude.add(Class.forName((String) exc));
	    	}

	    	// create the item mod and map it
	    	AttributeType type = AttributeType.valueOf(itemmod.getString("attributetype"));
	    	ItemModifier mod = new ItemModifier(type, tiermods, possible, exclude, (float) itemmod.getDouble("chance"));
	    	mods.put(type, mod);
	    }
	}
	
	public static HashMap<AttributeType, ItemModifier> getMods(){
		return mods;
	}
	
	public static CrypticItem getItemFromStack(ItemStack is){
		try {
			CrypticItemType type = CrypticItemType.fromMaterial(is.getType());
			if (type == null) return null;
			CrypticItem item = type.getHandleClass().newInstance();
			return item.getItemFromItemStack(is);
		} catch (InstantiationException | IllegalAccessException e) {
			Log.debug("Object wasn't made");
			return null;
		}
	}

	public static List<ItemGenerator> generateMobArmor(int level) {
		List<ItemGenerator> armor = new ArrayList<ItemGenerator>();
		
		armor.add(new ItemGenerator(CrypticItemType.HELMET).setLevel(level));
		armor.add(new ItemGenerator(CrypticItemType.CHESTPLATE).setLevel(level));
		armor.add(new ItemGenerator(CrypticItemType.LEGGINGS).setLevel(level));
		armor.add(new ItemGenerator(CrypticItemType.BOOTS).setLevel(level));
		
		return armor;
	}
	
	

}
