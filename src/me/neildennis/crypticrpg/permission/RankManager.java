package me.neildennis.crypticrpg.permission;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.Manager;
import me.neildennis.crypticrpg.permission.commands.CommandSetRank;

public class RankManager extends Manager {
	
	@Override
	public void onEnable() {
		Cryptic.registerCommand("setrank", new CommandSetRank());
	}
	
	@Override
	public void onDisable() {
		
	}
	
	@Override
	public void registerTasks() {
		
	}
	
	public static boolean isDemote(Rank from, Rank to){
		return from.getPriority() >= to.getPriority();
	}

}
