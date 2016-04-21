package me.neildennis.crypticrpg.permission;

import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.cloud.Cloud;
import me.neildennis.crypticrpg.permission.commands.CommandSetRank;

public class RankManager {
	
	public RankManager(){
		JavaPlugin main = Cryptic.getPlugin();
		main.getCommand("setrank").setExecutor(new CommandSetRank());
	}
	
	public static void setRankOffline(UUID id, Rank rank){
		String query = "INSERT INTO player_db (player_id, rank) VALUES ('" + id.toString() + "', '" + rank.name()
			+ "') ON DUPLICATE KEY UPDATE rank = '" + rank.name() + "'";
		Cloud.sendStatementAsync(query);
	}
	
	public static boolean isDemote(Rank from, Rank to){
		return from.getPriority() >= to.getPriority();
	}

}
