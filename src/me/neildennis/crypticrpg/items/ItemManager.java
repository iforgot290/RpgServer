package me.neildennis.crypticrpg.items;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

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
import me.neildennis.crypticrpg.items.type.weapon.CrypticWeapon;
import me.neildennis.crypticrpg.utils.Log;

public class ItemManager extends Manager{
	
	private static HashMap<AttributeType, ItemModifier> mods;
	private static ArrayList<AttributeType> weaponMods;
	
	@Override
	public void onEnable() {
		NameGenerator.load();
		Cryptic.registerEvents(new ItemListener());
		loadMods();
	}

	@Override
	public void onDisable() {
		
	}
	
	@Override
	public void registerTasks() {
		
	}
	
	// loads mods from file, will make it load from mysql or something later
	public void loadMods(){
		mods = new HashMap<AttributeType, ItemModifier>();
		
		try {
			for (JsonElement ele : loadJsonFile(new File(Cryptic.getPlugin().getDataFolder() + "/itemmods.json"))){
				try {
					parseJsonObject(ele.getAsJsonObject());
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		sortWeaponMods();
	}
	
	private void sortWeaponMods(){
		weaponMods = new ArrayList<AttributeType>();
		for (ItemModifier mod : mods.values()){
			if (mod.isPossible(CrypticWeapon.class)){
				weaponMods.add(mod.getType());
			}
		}
	}
	
	public static boolean isWeaponMod(AttributeType type){
		return weaponMods.contains(type);
	}
	
	private JsonArray loadJsonFile(File file) throws JsonIOException, JsonSyntaxException, FileNotFoundException{
		JsonParser parse = new JsonParser();
		JsonObject root = parse.parse(new FileReader(file)).getAsJsonObject();
		return root.getAsJsonArray("itemmods");
	}
	
	@SuppressWarnings("unchecked")
	private void parseJsonObject(JsonObject itemmod) throws ClassNotFoundException{
		// load tier mods
    	JsonArray jsontiermods = itemmod.getAsJsonArray("tiermods");
    	ArrayList<TierModifier> tiermods = new ArrayList<TierModifier>();
    	for(JsonElement modele : jsontiermods){
    		JsonObject mod = modele.getAsJsonObject();
    		ModifierType type = ModifierType.valueOf(mod.get("modtype").getAsString());
    		tiermods.add(new TierModifier(type, mod.get("minlvl").getAsInt(), mod.get("maxlvl").getAsInt(),
    				mod.get("low").getAsInt(), mod.get("high").getAsInt(), mod.get("mid").getAsInt()));
    	}

    	// load possible items
    	JsonArray jsonpossible = itemmod.getAsJsonArray("possible");
    	ArrayList<Class<? extends CrypticItem>> possible = new ArrayList<Class<? extends CrypticItem>>();
    	for(JsonElement pos : jsonpossible){
    		possible.add((Class<? extends CrypticItem>) Class.forName(pos.getAsString()));
    	}

    	// load exclude items
    	JsonArray jsonexclude = itemmod.getAsJsonArray("exclude");
    	ArrayList<Class<? extends CrypticItem>> exclude = new ArrayList<Class<? extends CrypticItem>>();
    	for(JsonElement exc : jsonexclude){
    		exclude.add((Class<? extends CrypticItem>) Class.forName(exc.getAsString()));
    	}

    	// create the item mod and map it
    	AttributeType type = AttributeType.valueOf(itemmod.get("attributetype").getAsString());
    	ItemModifier mod = new ItemModifier(type, tiermods, possible, exclude, itemmod.get("chance").getAsFloat());
    	mods.put(type, mod);
	}
	
	public static HashMap<AttributeType, ItemModifier> getMods(){
		return mods;
	}
	
	public static CrypticItem getItemFromStack(ItemStack is){
		if (is == null) return null;
		try {
			CrypticItemType type = CrypticItemType.fromMaterial(is.getType());
			if (type == null) return null;
			CrypticItem item = type.getHandleClass().getDeclaredConstructor(CrypticItemType.class, ItemStack.class).newInstance(type, is);
			return item;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
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
