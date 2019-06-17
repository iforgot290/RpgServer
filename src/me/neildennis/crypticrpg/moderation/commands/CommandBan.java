package me.neildennis.crypticrpg.moderation.commands;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.CrypticCommand;
import me.neildennis.crypticrpg.chat.ChatManager;
import me.neildennis.crypticrpg.moderation.ModerationManager;
import me.neildennis.crypticrpg.permission.Rank;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.utils.Log;
import net.md_5.bungee.api.ChatColor;

public class CommandBan extends CrypticCommand {

	private ModerationManager manager;
	
	public CommandBan(ModerationManager manager) {
		this.manager = manager;
	}
	
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
		
		OfflinePlayer toBan = Bukkit.getOfflinePlayer(args[0]);
		String reason = "The ban hammer has spoken!";
		
		if (args.length > 1){
			reason = "";
			for (int i = 1; i < args.length; i++){
				reason += args[i] + " ";
			}
		}
		
		if (toBan.isOnline()){
			CrypticPlayer cptoban = Cryptic.getCrypticPlayer(toBan.getPlayer());
			
			if (cptoban.getRank().getPriority() >= pl.getRank().getPriority()){
				pl.sendMessage(ChatColor.RED + "You cannot ban this person");
				return true;
			}
			
			cptoban.getPlayer().kickPlayer(manager.getKickedBannedMessage(reason));
		}
		
		final OfflinePlayer finalToBan = toBan;
		final String finalReason = reason;
		final Player enforcer = pl.getPlayer();
		
		Bukkit.getScheduler().runTaskAsynchronously(Cryptic.getPlugin(), new Runnable(){

			@Override
			public void run() {
				try {
					manager.banPlayer(finalToBan, finalReason, enforcer);
					ChatManager.sendSyncMessage(enforcer, ChatColor.GREEN + finalToBan.getName() + " was banned");
				} catch (SQLException e) {
					ChatManager.sendSyncMessage(enforcer, ChatColor.RED + "Error putting ban in database");
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
		
		if (player.isOnline()) player.getPlayer().kickPlayer(manager.getKickedBannedMessage(reason));
		
		final OfflinePlayer toBan = player;
		final String finalReason = reason;
		final OfflinePlayer enforcer = Bukkit.getOfflinePlayer("CONSOLE");
		
		Bukkit.getScheduler().runTaskAsynchronously(Cryptic.getPlugin(), new Runnable(){

			@Override
			public void run() {
				try {
					manager.banPlayer(toBan, finalReason, enforcer);
					Log.info(toBan.getName() + " was banned");
				} catch (SQLException e) {
					Log.info("Error banning player: " + toBan.getName());
					e.printStackTrace();
				}
			}
			
		});
		
		return true;
	}

}
