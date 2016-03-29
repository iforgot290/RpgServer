package minecade.dungeonrealms.Hive.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import minecade.dungeonrealms.Main;
import minecade.dungeonrealms.Hive.Hive;

public class CommandWhois implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player pl = null;
		if (sender instanceof Player) {
			pl = (Player) sender;
			if (!pl.isOp()) {
				return true;
			}
		}

		if (pl != null) {
			if (args.length != 1) {
				pl.sendMessage("Syntax. /whois <player>");
				return true;
			}
			
			@SuppressWarnings("deprecation")
			OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
			
			int server_num = Hive.getPlayerServer(player, false);
			if (server_num == -1) {
				pl.sendMessage(ChatColor.RED + player.getName() + ", currently offline.");
				return true;
			}

			String server_prefix = Hive.getServerPrefixFromNum(server_num);

			if (Bukkit.getPlayer(args[0]) == null) {
				pl.sendMessage(
						ChatColor.YELLOW + player.getName() + ", currently on server " + ChatColor.UNDERLINE + server_prefix);
			} else {
				pl.sendMessage(ChatColor.YELLOW + player.getName() + ", currently on " + ChatColor.UNDERLINE + "YOUR"
						+ ChatColor.YELLOW + " server.");
			}
		} else if (pl == null) {
			if (args.length != 1) {
				Main.log.info("Syntax. /whois <player>");
				return true;
			}
			
			@SuppressWarnings("deprecation")
			OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
			
			int server_num = Hive.getPlayerServer(player, false);
			if (server_num == -1) {
				Main.log.info(ChatColor.RED + player.getName() + ", currently offline.");
				return true;
			}

			String server_prefix = Hive.getServerPrefixFromNum(server_num);

			if (Bukkit.getPlayer(args[0]) == null) {
				Main.log.info(
						ChatColor.YELLOW + player.getName() + ", currently on server " + ChatColor.UNDERLINE + server_prefix);
			} else {
				Main.log.info(ChatColor.YELLOW + player.getName() + ", currently on " + ChatColor.UNDERLINE + "YOUR"
						+ ChatColor.YELLOW + " server.");
			}
			// Log to console.
		}
		return true;
	}

}