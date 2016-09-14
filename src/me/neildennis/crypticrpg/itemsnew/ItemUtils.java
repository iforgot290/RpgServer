package me.neildennis.crypticrpg.itemsnew;

import java.util.HashMap;
import java.util.List;

import me.neildennis.crypticrpg.itemsnew.attribs.Attribute;
import me.neildennis.crypticrpg.itemsnew.attribs.Rarity;
import net.md_5.bungee.api.ChatColor;

public class ItemUtils {

	public static Rarity getRarity(String str){
		for (Rarity rare : Rarity.values())
			if (str.equalsIgnoreCase(ChatColor.stripColor(rare.getDisplay())))
				return rare;
		return null;
	}

	public static HashMap<Attribute, Integer> getAttributes(List<String> lore){
		HashMap<Attribute, Integer> attribs = new HashMap<Attribute, Integer>();

		for (String str : lore){
			Attribute attr = getAttrFromString(str);
			if (attr == null) continue;

			String value = str.replaceAll(attr.getPrefix(), "").replaceAll(attr.getPostfix(), "");
			attribs.put(attr, Integer.valueOf(value));
		}

		return attribs;
	}

	public static Attribute getAttrFromString(String str){
		for (Attribute attr : Attribute.values())
			if (str.startsWith(attr.getPrefix()))
				return attr;
		return null;
	}

}
