package me.neildennis.crypticrpg.items.type;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.neildennis.crypticrpg.items.ItemUtils;
import me.neildennis.crypticrpg.items.attribs.Attribute;
import me.neildennis.crypticrpg.items.attribs.AttributeType;
import me.neildennis.crypticrpg.items.attribs.Rarity;
import me.neildennis.crypticrpg.items.attribs.Tier;

public abstract class CrypticGear extends CrypticItem{

	protected ArrayList<Attribute> attribs;
	protected Tier tier;
	protected Rarity rarity;
	
	protected String shortLore;

	public CrypticGear(CrypticItemType type, String name, List<String> lore, ArrayList<Attribute> attribs, Tier tier, Rarity rarity) {
		super(type, name, lore);
		this.attribs = attribs;
		this.tier = tier;
		this.rarity = rarity;

		generateItemStack();
	}

	public CrypticGear(CrypticItemType type, ItemStack stack){
		super(type, stack);
		
		this.stack = generateItemStack();
	}

	public ArrayList<Attribute> getAttribs(){
		return attribs;
	}

	public Attribute getAttribute(AttributeType attr){
		for (Attribute attrib : attribs)
			if (attrib.getType() == attr)
				return attrib;
		return null;
	}

	public Tier getTier(){
		return tier;
	}

	public Rarity getRarity(){
		return rarity;
	}
	
	public String getShortLore() {
		return shortLore;
	}

	protected abstract Material getMatFromTier();
	protected abstract Tier getTierFromMat(Material mat);

	protected List<String> generateBukkitLore(){
		List<String> retlore = new ArrayList<String>();

		retlore.add(rarity.getDisplay());
		
		if (attribs == null) return this.lore;
		
		for (Attribute attr : attribs){
			retlore.add(ChatColor.RED + attr.getType().getPrefix() + attr.format() + attr.getType().getPostfix());
		}

		return this.lore;
	}

	protected CrypticItem getItemFromItemStack(ItemStack is) {
		this.lore = is.getItemMeta().getLore();

		this.rarity = ItemUtils.getRarity(ChatColor.stripColor(lore.get(0)));

		int start = (AttributeType.valueOf(ChatColor.stripColor(lore.get(1).split(" ")[0])) == null) ? 2 : 1; // Figures out if item has actual lore
		
		if (start == 2) {
			shortLore = lore.get(1);
		}

		tier = getTierFromMat(is.getType());
		name = is.getItemMeta().getDisplayName();
		attribs = ItemUtils.getAttributes(lore, start);

		return this;
	}

	@Override
	public ItemStack generateItemStack() {
		Material mat = getMatFromTier();
		ItemStack is = new ItemStack(mat);

		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(tier.getColor() + name);
		meta.setLore(generateBukkitLore());
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		is.setItemMeta(meta);

		return is;
	}


}
