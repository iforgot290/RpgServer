package me.neildennis.crypticrpg.utils;

import org.bukkit.Bukkit;

import me.neildennis.crypticrpg.Cryptic;

public class Log {

	private Log(){}

	public static void info(Object obj) {
		System.out.println(obj);
	}

	public static void debug(Object obj) {
		if (Cryptic.isDebug()) {
			if (Cryptic.rpgEnabled()) Bukkit.broadcastMessage(obj.toString());
			else System.out.println(obj);
		}
	}

	public static void warning(Object obj) {
		Bukkit.getLogger().warning(obj.toString());
	}

	public static void error(Object obj) {
		Bukkit.getLogger().severe(obj.toString());
	}

}
