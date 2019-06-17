package me.neildennis.crypticrpg.moderation.commands;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.CrypticCommand;
import me.neildennis.crypticrpg.chat.ChatManager;
import me.neildennis.crypticrpg.cloud.data.PlayerData;
import me.neildennis.crypticrpg.moderation.ModerationManager;
import me.neildennis.crypticrpg.permission.Rank;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;
import me.neildennis.crypticrpg.utils.Log;
import net.md_5.bungee.api.ChatColor;

public class CommandBan extends CrypticCommand{

	@Override
	protected boolean sendUsage() {
		sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <player> <reason>");
		return true;
	}

	/*
	 * Usage /ban <player> <reason>
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean command(CrypticPlayer pl) {
		
		if (pl.getRank().getPriority() < Rank.ADMIN.getPriority()) return false;
		if (args.length == 0) return sendUsage();
		
		OfflinePlayer toban = Bukkit.getOfflinePlayer(args[0]);
		String reason = "The ban hammer has spoken!";
		
		if (args.length > 1){
			reason = "";
			for (int i = 1; i < args.length; i++){
				reason += args[i] + " ";
			}
		}
		
		if (toban.isOnline()){
			CrypticPlayer cptoban = Cryptic.getCrypticPlayer(toBan.getPlayer());
			
			if (cptoban.getRank().getPriority() >= pl.getRank().getPriority()){
				pl.sendMessage(ChatColor.RED + "You cannot ban this person");
				return true;
			}
			
			toban.getPlayer().kickPlayer(ModerationManager.getKickedBannedMessage(reason));
		}
		
		final UUID tobanid = toban.getUniqueId();
		final String tobanName = toban.getName();
		final UUID banner = pl.getId();
		final String bannerName = pl.getPlayer().getName();
		final String finalReason = reason;
		final Player returnto = pl.getPlayer();
		final Rank rank = pl.getRank();
		
		Bukkit.getScheduler().runTaskAsynchronously(Cryptic.getPlugin(), new Runnable(){

			@Override
			public void run() {
				try {
					Rank tobanrank = PlayerData.getRank(tobanid);
					
					if (tobanrank.getPriority() >= rank.getPriority()){
						ChatManager.sendSyncMessage(returnto, ChatColor.RED + "You cannot ban this person");
						return;
					}
					
					PlayerData.banPlayer(tobanid, tobanName, banner, bannerName, finalReason);
					ChatManager.sendSyncMessage(returnto, ChatColor.GREEN + tobanName + " was banned");
				} catch (SQLException e) {
					ChatManager.sendSyncMessage(returnto, ChatColor.RED + "Error putting ban in database");
					e.printStackTrace();
				}
			}
			
		});
		
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean console() {
		if (args.length == 0) return sendUsage();
		
		OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
		String reason = "The ban hammer has spoken!";
		
		if (args.length > 1){
			reason = "";
			for (int i = 1; i < args.length; i++){
				reason += args[i] + " ";
			}
		}
		
		if (player.isOnline()) player.getPlayer().kickPlayer(ModerationManager.getKickedBannedMessage(reason));
		
		final UUID tobanid = player.getUniqueId();
		final String name = player.getName();
		final String finalReason = reason;
		
		Bukkit.getScheduler().runTaskAsynchronously(Cryptic.getPlugin(), new Runnable(){

			@Override
			public void run() {
				try {
					PlayerData.banPlayer(tobanid, name, null, "CONSOLE", finalReason);
					Log.info(name + " was banned");
				} catch (SQLException e) {
					Log.info("Error banning player!");
					e.printStackTrace();
				}
			}
			
		});
		
		return true;
	}

}
