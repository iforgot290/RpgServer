package me.neildennis.crypticrpg.utils;

import org.bukkit.Location;

import me.neildennis.crypticrpg.Cryptic;

public class Utils {
	
	private Utils(){}
	
	public static Location getLocFromString(String str){
		String[] args = str.split(":");
		Location loc = new Location(Cryptic.getMainWorld(), Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
		return loc;
	}

}
