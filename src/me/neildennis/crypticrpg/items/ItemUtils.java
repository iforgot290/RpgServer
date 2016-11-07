package me.neildennis.crypticrpg.items;

import java.util.ArrayList;
import java.util.List;

import me.neildennis.crypticrpg.items.attribs.Attribute;
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

	public static ArrayList<Attribute> getAttributes(List<String> lore){
		ArrayList<Attribute> attribs = new ArrayList<Attribute>();

		for (String str : lore){
			AttributeType attr = getAttrFromString(str);
			if (attr == null) continue;

			String value = str.replaceAll(attr.getPrefix(), "").replaceAll(attr.getPostfix(), "");
			
			if (value.contains(" - ")){
				String[] strvalues = value.split(" - ");
				int[] values = {Integer.valueOf(strvalues[0]), Integer.valueOf(strvalues[1])};
				attribs.add(new Attribute(attr, values));
			} else {
				attribs.add(new Attribute(attr, Integer.valueOf(value)));
			}
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
