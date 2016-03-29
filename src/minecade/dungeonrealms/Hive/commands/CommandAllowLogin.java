package minecade.dungeonrealms.Hive.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import minecade.dungeonrealms.Hive.Hive;

public class CommandAllowLogin implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length != 1) {
			sender.sendMessage(ChatColor.RED + "Invalid Syntax: /allowlogin <name>");
			return true;
		}
		if (!sender.isOp()) {
			return true;
		}
		
		@SuppressWarnings("deprecation")
		OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
		Hive.setPlayerCanJoin(player, true);
		sender.sendMessage(ChatColor.AQUA + player.getName() + " join status set to " + ChatColor.GREEN + ChatColor.BOLD + "TRUE");
		return false;
	}

}
