package me.neildennis.crypticrpg.utils;

import org.bukkit.Bukkit;

public class Log {
	
	private Log(){}
	
	public static void info(Object obj){
		System.out.println(obj);
	}
	
	public static void debug(Object obj){
		Bukkit.broadcastMessage(obj.toString());
	}

}
