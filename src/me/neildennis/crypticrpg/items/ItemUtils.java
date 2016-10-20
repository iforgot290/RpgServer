package me.neildennis.crypticrpg.items;

import java.util.HashMap;
import java.util.List;

import me.neildennis.crypticrpg.items.attribs.AttributeType;
import me.neildennis.crypticrpg.items.attribs.Rarity;
import net.md_5.bungee.api.ChatColor;

public class ItemUtils {

	public static Rarity getRarity(String str){
		for (Rarity rare : Rarity.values())
			if (str.equalsIgnoreCase(ChatColor.stripColor(rare.getDisplay())))
				return rare;
		return null;
	}

	public static HashMap<AttributeType, Integer> getAttributes(List<String> lore){
		HashMap<AttributeType, Integer> attribs = new HashMap<AttributeType, Integer>();

		for (String str : lore){
			AttributeType attr = getAttrFromString(str);
			if (attr == null) continue;

			String value = str.replaceAll(attr.getPrefix(), "").replaceAll(attr.getPostfix(), "");
			attribs.put(attr, Integer.valueOf(value));
		}

		return attribs;
	}

	public static AttributeType getAttrFromString(String str){
		for (AttributeType attr : AttributeType.values())
			if (str.startsWith(attr.getPrefix()))
				return attr;
		return null;
	}

}
