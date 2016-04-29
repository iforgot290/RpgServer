package me.neildennis.crypticrpg.permission.commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.CrypticCommand;
import me.neildennis.crypticrpg.chat.ChatManager;
import me.neildennis.crypticrpg.cloud.Cloud;
import me.neildennis.crypticrpg.cloud.data.PlayerData;
import me.neildennis.crypticrpg.permission.Rank;
import me.neildennis.crypticrpg.permission.RankManager;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;
import me.neildennis.crypticrpg.utils.Log;
import net.md_5.bungee.api.ChatColor;

public class CommandSetRank extends CrypticCommand{

	@Override
	protected void sendUsage(){
		sender.sendMessage(ChatColor.RED + "/setrank <player> <rank>");
	}

	@Override
	public void command(final CrypticPlayer pl, Command cmd, String label, String[] args) {

		if (pl.getRank().getPriority() < Rank.ADMIN.getPriority()){
			noPerms();
			return;
		}

		if (args.length == 0){
			sendUsage();
			return;
		}

		final UUID id;
		final String name;
		final CrypticPlayer toset;
		final Rank torank;

		if (args.length == 1) {
			toset = pl;
			id = pl.getId();
			name = pl.getPlayer().getName();
			torank = Rank.valueOf(args[0]);
		} else {
			@SuppressWarnings("deprecation")
			OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
			id = op.getUniqueId();
			name = op.getName();
			toset = PlayerManager.getCrypticPlayer(op);
			torank = Rank.valueOf(args[1]);
		}

		if (toset == null){
			Bukkit.getScheduler().runTaskAsynchronously(Cryptic.getPlugin(), new Runnable(){

				@Override
				public void run() {
					ResultSet res = Cloud.sendQuery("SELECT rank, server FROM player_db WHERE player_id = '"
							+ id.toString() + "'");

					try {
						if (!res.next()){
							ChatManager.sendSyncMessage(pl.getPlayer(), ChatColor.RED + "Player not found");
							return;
						}
						
						Rank fromrank = Rank.valueOf(res.getString("rank"));
						boolean cont = false;
						if (RankManager.isDemote(fromrank, torank)){
							if (fromrank.getPriority() < pl.getRank().getPriority())
								cont = true;
						} else {
							if (torank.getPriority() < pl.getRank().getPriority())
								cont = true;
						}

						if (cont){
							Cloud.sendStatementAsync("UPDATE player_db SET rank = '" + torank.name()
							+ "' WHERE player_id = '" + id.toString() + "'");

							int server = res.getInt("server");
							if (server >= 0){
								Cloud.sendCrossServer(server, "rank " + id.toString() + " " + torank.name());
							}

							ChatManager.sendSyncMessage(pl.getPlayer(), ChatColor.GREEN + "Set " + name + "'s rank to "
									+ torank.toString());
						} else {
							ChatManager.sendSyncMessage(pl.getPlayer(), ChatColor.RED + "You do not have permission to set "
									+ name + "'s rank to " + torank.toString());
						}
					} catch (SQLException e) {
						ChatManager.sendSyncMessage(pl.getPlayer(), ChatColor.RED + "Database error");
						e.printStackTrace();
					}
				}

			});
		} else {
			if (toset == pl){
				if (RankManager.isDemote(pl.getRankData().getRank(), torank)){
					pl.getRankData().setRank(torank);
					PlayerData.savePlayerRank(pl);
					pl.getPlayer().sendMessage(ChatColor.GREEN + "Set your own rank to " + torank.toString());
					return;
				}

				pl.getPlayer().sendMessage(ChatColor.RED + "You are not allowed to promote yourself");
				return;
			}
			
			Rank fromrank = toset.getRankData().getRank();
			boolean cont = false;

			if (RankManager.isDemote(fromrank, torank)){
				if (fromrank.getPriority() < pl.getRank().getPriority())
					cont = true;
			} else {
				if (torank.getPriority() < pl.getRank().getPriority())
					cont = true;
			}

			if (cont){
				toset.getRankData().setRank(torank);
				PlayerData.savePlayerRank(toset);
				pl.getPlayer().sendMessage(ChatColor.GREEN + "Set " + name + "'s rank to " + torank.toString());
			} else {
				pl.getPlayer().sendMessage(ChatColor.RED + "You do not have permission to set "
						+ name + "'s rank to " + torank.toString());
			}

		}

	}

	@Override
	public void console(Command cmd, String label, String[] args) {
		if (args.length < 2){
			sendUsage();
			return;
		}
		
		String name = args[0];
		Rank rank = Rank.valueOf(args[1]);
		
		@SuppressWarnings("deprecation")
		OfflinePlayer pl = Bukkit.getOfflinePlayer(name);
		
		if (pl.isOnline()){
			CrypticPlayer cp = PlayerManager.getCrypticPlayer(pl);
			cp.getRankData().setRank(rank);
			PlayerData.savePlayerRank(cp);
		} else {
			Cloud.sendStatementAsync("UPDATE player_db SET rank = '" + rank.name()
			+ "' WHERE player_id = '" + pl.getUniqueId().toString() + "'");
		}
		
		Log.info("Set " + pl.getName() + " to " + rank.toString());
	}

}
