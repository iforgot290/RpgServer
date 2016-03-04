package minecade.dungeonrealms.PermissionMechanics.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import minecade.dungeonrealms.Main;
import minecade.dungeonrealms.CommunityMechanics.CommunityMechanics;
import minecade.dungeonrealms.DonationMechanics.DonationMechanics;
import minecade.dungeonrealms.PermissionMechanics.PermissionMechanics;

public class CommandSetRank implements CommandExecutor {
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = null;
		if(sender instanceof Player) {
			p = (Player) sender;
			if(!(p.isOp())) { return true; }
		}
		if(args.length != 2) {
			if(p != null) {
				p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Incorrect Syntax");
				p.sendMessage(ChatColor.RED + "/setrank <PLAYER> <RANK>");
				p.sendMessage(ChatColor.RED + "Ranks: " + ChatColor.GRAY + "Default | SUB | SUB+ | SUB++ | PMOD | GM | YT |");
			}
			return true;
		}
		
		final String pname = args[0];
		final String rank = args[1];
		final Player psender = p;
		
		if(!(rank.equalsIgnoreCase("default")) && !(rank.equalsIgnoreCase("wd")) && !(rank.equalsIgnoreCase("sub")) && !(rank.equalsIgnoreCase("sub+")) && !(rank.equalsIgnoreCase("sub++")) && !(rank.equalsIgnoreCase("PMod")) && !(rank.equalsIgnoreCase("GM")) && !(rank.equalsIgnoreCase("yt"))) {
			if(p != null) {
				p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Invalid Rank '" + rank + "'");
				p.sendMessage(ChatColor.RED + "/setrank <PLAYER> <RANK>");
				p.sendMessage(ChatColor.RED + "Ranks: " + ChatColor.GRAY + "Default | SUB | SUB+ | SUB++ | PMOD | GM | WD | YT |");
			}
			return true;
		}
		
		new BukkitRunnable(){
			
			public void run(){
				@SuppressWarnings("deprecation")
				OfflinePlayer player = Bukkit.getOfflinePlayer(pname);
				PermissionMechanics.setRank(player, rank, true);
				CommunityMechanics.sendPacketCrossServer("[rank_map]" + player.getUniqueId().toString() + ":" + rank.toLowerCase(), -1, true);
				
				if (rank.equalsIgnoreCase("sub") || rank.equalsIgnoreCase("sub+")){
					DonationMechanics.addSubscriberDays(player, 30, true);
				} else if (rank.equalsIgnoreCase("sub++")){
					DonationMechanics.addSubscriberDays(player, 9999, true);
				}
				
				if(psender != null) {
					psender.sendMessage(ChatColor.GREEN + "You have set the user " + player.getName() + " to the rank of " + rank + " on all Dungeon Realm servers.");
				}
			}
			
		}.runTaskAsynchronously(Main.plugin);
		
		return true;
	}
	
}