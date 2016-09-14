package me.neildennis.crypticrpg.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

public class StringUtils {

	private StringUtils(){}
	
	public static List<String> stripColor(List<String> list){
		List<String> toret = new ArrayList<String>();
		
		for (String str : list){
			toret.add(ChatColor.stripColor(str));
		}
		
		return toret;
	}
	
}
