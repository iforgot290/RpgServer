package me.neildennis.crypticrpg.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import me.neildennis.crypticrpg.items.type.armor.CrypticArmor;
import me.neildennis.crypticrpg.items.type.weapon.CrypticWeapon;
import me.neildennis.crypticrpg.utils.Log;

public class ItemManager extends Manager{
	
	private static HashMap<AttributeType, ItemModifier> mods;
	private static ArrayList<AttributeType> weaponMods;
	
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
		possible.add(CrypticWeapon.class);
		
		ItemModifier mod = new ItemModifier(AttributeType.DAMAGE, tiermods, possible, 1.0F);
		
		mods.put(AttributeType.DAMAGE, mod);
		
		ArrayList<TierModifier> armorMods = new ArrayList<TierModifier>();
		armorMods.add(new TierModifier(ModifierType.STATIC, 0, 19, 10, 20));
		
		ArrayList<Class<? extends CrypticItem>> possibleArmor = new ArrayList<Class<? extends CrypticItem>>();
		possibleArmor.add(CrypticArmor.class);
		
		ItemModifier modArmor = new ItemModifier(AttributeType.HEALTH, armorMods, possibleArmor, 1.0F);
		
		mods.put(AttributeType.HEALTH, modArmor);
		
		sortWeaponMods();
	}
	
	private void sortWeaponMods(){
		weaponMods = new ArrayList<AttributeType>();
		for (ItemModifier mod : mods.values()){
			if (mod.isPossible(CrypticWeapon.class)){
				Log.debug(mod.getType().name() + " is weapon mod");
				weaponMods.add(mod.getType());
			}
		}
	}
	
	public static boolean isWeaponMod(AttributeType type){
		return weaponMods.contains(type);
	}
	
	public static HashMap<AttributeType, ItemModifier> getMods(){
		return mods;
	}
	
	public static CrypticItem getItemFromStack(ItemStack is){
		if (is == null) return null;
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
