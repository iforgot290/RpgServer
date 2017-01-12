package me.neildennis.crypticrpg.utils;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.IntegerFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;

public class CustomFlags {
	
	public static final IntegerFlag MINING = new IntegerFlag("mining-spots");
	public static final IntegerFlag ORE_RESPAWN = new IntegerFlag("ore-respawn");
	
	public static void loadFlags(){
		FlagRegistry registry = WorldGuardPlugin.inst().getFlagRegistry();
		registry.register(MINING);
		registry.register(ORE_RESPAWN);
	}

}
