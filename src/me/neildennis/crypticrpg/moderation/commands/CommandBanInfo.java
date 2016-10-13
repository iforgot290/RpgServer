package me.neildennis.crypticrpg.moderation.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.CrypticCommand;
import me.neildennis.crypticrpg.chat.ChatManager;
import me.neildennis.crypticrpg.moderation.ModerationData;
import me.neildennis.crypticrpg.moderation.ModerationData.Ban;
import me.neildennis.crypticrpg.permission.Rank;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;

public class CommandBanInfo extends CrypticCommand {

	@Override
	protected boolean sendUsage() {
		sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <user>");
		return true;
	}

	@Override
	public boolean command(CrypticPlayer pl) {
		if (pl.getRank().getPriority() < Rank.ADMIN.getPriority()) return false;
		return console();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean console() {
		
		if (args.length == 0) return sendUsage();
		
		OfflinePlayer pl = Bukkit.getOfflinePlayer(args[0]);
		final UUID id = pl.getUniqueId();
		final String name = pl.getName();
		
		Bukkit.getScheduler().runTaskAsynchronously(Cryptic.getPlugin(), new Runnable(){
			
			@Override
			public void run(){
				try {
					ModerationData data = getData(id);
					ChatManager.sendSyncMessage(sender, info(data, name));
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		});
		
		return true;
	}
	
	private ModerationData getData(UUID id) throws SQLException{
		CrypticPlayer pl = PlayerManager.getCrypticPlayer(id);
		
		if (pl == null) return new ModerationData(id);
		else return pl.getModerationData();
	}
	
	private String[] info(ModerationData data, String name){
		ArrayList<String> info = new ArrayList<String>();
		Ban ban = data.getCurrentBan();
		
		info.add(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Player info: " + ChatColor.GREEN + name);
		info.add(ChatColor.YELLOW + "Banned: " + (ban == null ? ChatColor.GREEN + "false" : ChatColor.RED + "true"));
		info.add(ChatColor.YELLOW + "Bans on record: " + ChatColor.GRAY + data.getBans().size());
		info.add("");
		
		if (ban != null){
			info.add(ChatColor.YELLOW + "Enforcer: " + ChatColor.GRAY + ban.getBannerName());
			info.add(ChatColor.YELLOW + "Reason: " + ChatColor.GRAY + ban.getReason());
			Date date = new Date(ban.getBanTime());
			info.add(ChatColor.YELLOW + "Time: " + ChatColor.GRAY + date.toString());
			
			if (ban.isTempBan()){
				Date unban = new Date(ban.getUnbanTime());
				info.add(ChatColor.YELLOW + "Unban time: " + ChatColor.GRAY + unban.toString());
			}
		}
		
		return info.toArray(new String[info.size()]);
	}

}
