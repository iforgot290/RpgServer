package me.neildennis.crypticrpg.itemsnew.type.weapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.neildennis.crypticrpg.itemsnew.ItemUtils;
import me.neildennis.crypticrpg.itemsnew.attribs.Attribute;
import me.neildennis.crypticrpg.itemsnew.attribs.Rarity;
import me.neildennis.crypticrpg.itemsnew.attribs.Tier;
import me.neildennis.crypticrpg.itemsnew.type.CrypticItem;
import me.neildennis.crypticrpg.itemsnew.type.CrypticItemType;
import me.neildennis.crypticrpg.utils.StringUtils;

public abstract class CrypticWeapon extends CrypticItem{

	protected HashMap<Attribute, Integer> attribs;
	protected Tier tier;
	protected Rarity rarity;
	
	public CrypticWeapon(String name, List<String> lore, CrypticItemType type, HashMap<Attribute, Integer> attribs, Tier tier) {
		super(name, lore, type);
		this.attribs = attribs;
		this.tier = tier;
	}
	
	public CrypticWeapon(){
		super();
	}
	
	public HashMap<Attribute, Integer> getAttribs(){
		return attribs;
	}
	
	public int getValue(Attribute attrib){
		return attribs.get(attrib);
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
		
		for (Attribute attr : attribs.keySet()){
			retlore.add(attr.getPrefix() + attribs.get(attr) + attr.getPostfix());
		}
		
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
		ItemStack is = new ItemStack(getMatFromTier());
		
		ItemMeta meta = is.getItemMeta();
		meta.setDisplayName(tier.getColor() + name);
		meta.setLore(getBukkitDisplayLore());
		is.setItemMeta(meta);
		
		return null;
	}
	

}
