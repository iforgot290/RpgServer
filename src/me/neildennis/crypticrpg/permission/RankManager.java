package me.neildennis.crypticrpg.permission;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.permission.commands.CommandSetRank;

public class RankManager {
	
	public RankManager(){
		Cryptic.registerCommand("setrank", new CommandSetRank());
	}
	
	public static boolean isDemote(Rank from, Rank to){
		return from.getPriority() >= to.getPriority();
	}

}
