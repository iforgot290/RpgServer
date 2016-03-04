package minecade.dungeonrealms.PermissionMechanics.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
		
		String p_name = args[0];
		String rank = args[1];
		
		if(!(rank.equalsIgnoreCase("default")) && !(rank.equalsIgnoreCase("wd")) && !(rank.equalsIgnoreCase("sub")) && !(rank.equalsIgnoreCase("sub+")) && !(rank.equalsIgnoreCase("sub++")) && !(rank.equalsIgnoreCase("PMod")) && !(rank.equalsIgnoreCase("GM")) && !(rank.equalsIgnoreCase("yt"))) {
			if(p != null) {
				p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Invalid Rank '" + rank + "'");
				p.sendMessage(ChatColor.RED + "/setrank <PLAYER> <RANK>");
				p.sendMessage(ChatColor.RED + "Ranks: " + ChatColor.GRAY + "Default | SUB | SUB+ | SUB++ | PMOD | GM | WD | YT |");
			}
			return true;
		}
		
		PermissionMechanics.setRank(p_name, rank, true);
		
		if(p != null) {
			p.sendMessage(ChatColor.GREEN + "You have set the user " + p_name + " to the rank of " + rank + " on all Dungeon Realm servers.");
		}
		return true;
	}
	
}