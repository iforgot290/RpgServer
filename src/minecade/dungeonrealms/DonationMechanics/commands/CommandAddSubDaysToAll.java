package minecade.dungeonrealms.DonationMechanics.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import minecade.dungeonrealms.DonationMechanics.DonationMechanics;
import minecade.dungeonrealms.PermissionMechanics.PermissionMechanics;

public class CommandAddSubDaysToAll implements CommandExecutor {
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		Player ps = null;
		if(sender instanceof Player) {
			ps = (Player) sender;
			if(!PermissionMechanics.isGM(ps)) { return true; }
		}
		
		if (args.length != 1) {
			sender.sendMessage(ChatColor.RED + "Incorrect Syntax.  Usage: /addsubdaystoall <days>");
			return true;
		}
		
		try {
			DonationMechanics.addSubscriberDaysToAll(Integer.valueOf(args[0]));
		}
		catch (NumberFormatException ex) {
			ps.sendMessage(ChatColor.RED + "Error: argument must be a number.");
			return true;
		}
		
		sender.sendMessage(ChatColor.GREEN + args[0] + " days have been added to all subs and sub+'s.");
		return true;
	}
	
}