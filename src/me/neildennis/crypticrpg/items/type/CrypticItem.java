package me.neildennis.crypticrpg.items.type;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.neildennis.crypticrpg.utils.Log;

public abstract class CrypticItem {

	protected CrypticItemType type;
	protected String name;
	protected List<String> lore;

	protected ItemStack stack; // This will be null until a child class sets it
	protected CrypticItemProvider provider;

	public CrypticItem(CrypticItemType type, String name, List<String> lore) {
		this.type = type;
		this.name = name;
		this.lore = lore;
		
		instantiateProvider();
	}

	public CrypticItem(CrypticItemType type, ItemStack stack) {
		this.type = type;
		this.stack = stack;

		ItemMeta meta = stack.getItemMeta();
		name = meta.getDisplayName();
		lore = meta.getLore();
		
		instantiateProvider();
	}
	
	private void instantiateProvider() {
		try {
			this.provider = this.type.getProviderClass().getDeclaredConstructor(CrypticItem.class).newInstance(this);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			Log.error("Unable to instantiate provider for " + this.getClass().getName());
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}

	public List<String> getLore() {
		return lore;
	}

	public CrypticItemType getType() {
		return type;
	}
	
	public void setProvider(CrypticItemProvider provider) {
		this.provider = provider;
	}
	
	public CrypticItemProvider getProvider() {
		return provider;
	}

	public ItemStack getBukkitItem() {
		if (stack != null) return stack;
		else return (stack = generateItemStack());
	}
	
	public abstract ItemStack generateItemStack();
	
	

}
