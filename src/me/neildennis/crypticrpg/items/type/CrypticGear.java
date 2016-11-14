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
import me.neildennis.crypticrpg.utils.StringUtils;

public abstract class CrypticGear extends CrypticItem{

	protected ArrayList<Attribute> attribs;
	protected Tier tier;
	protected Rarity rarity;
	
	public CrypticGear(String name, List<String> lore, CrypticItemType type, ArrayList<Attribute> attribs, Tier tier, Rarity rarity) {
		super(name, lore, type);
		this.attribs = attribs;
		this.tier = tier;
		this.rarity = rarity;
	}
	
	public CrypticGear(){
		super();
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
	
	protected abstract Material getMatFromTier();
	protected abstract Tier getTierFromMat(Material mat);
	
	@Override
	public List<String> getBukkitDisplayLore(){
		List<String> retlore = new ArrayList<String>();
		
		for (Attribute attr : attribs){
			retlore.add(ChatColor.RED + attr.getType().getPrefix() + attr.format() + attr.getType().getPostfix());
		}
		
		retlore.add("");
		retlore.add(rarity.getDisplay());
		
		if (lore != null && lore.size() > 0){
			for (String str : lore) retlore.add(str);
		}
		
		return retlore;
	}
	
	@Override
	public CrypticItem getItemFromItemStack(ItemStack is) {
		super.getItemFromItemStack(is);
		List<String> lore = StringUtils.stripColor(is.getItemMeta().getLore());
		int skip = 0;
		
		for (String str : lore){
			skip++;
			Rarity rare = ItemUtils.getRarity(str);
			
			if (rare != null){
				rarity = rare;
				break;
			}
		}
		
		List<String> strlore = new ArrayList<String>();
		
		for (int i = skip; i < lore.size(); i++){
			String toadd = lore.get(i);
			
			if (toadd.startsWith("Durability")) break;
			if (toadd.equals("") || toadd.equals(" ")) continue;
			
			strlore.add(toadd);
		}
		
		this.lore = strlore;
		tier = getTierFromMat(is.getType());
		name = ChatColor.stripColor(is.getItemMeta().getDisplayName());
		attribs = ItemUtils.getAttributes(lore);
		
		return this;
	}

	@Override
	public ItemStack generateItemStack() {
		Material mat = getMatFromTier();
		ItemStack is = new ItemStack(mat);
		
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(tier.getColor() + name);
		meta.setLore(getBukkitDisplayLore());
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		is.setItemMeta(meta);
		
		return is;
	}
	

}
