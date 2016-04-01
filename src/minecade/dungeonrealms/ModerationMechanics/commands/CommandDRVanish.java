package minecade.dungeonrealms.ModerationMechanics.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import minecade.dungeonrealms.ModerationMechanics.ModerationMechanics;

public class CommandDRVanish implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = null;
		if (sender instanceof Player) {
			p = (Player) sender;
		}

		if (!(p.isOp())) {
			return true;
		}

		if (ModerationMechanics.isPlayerVanished(p)) {
			ModerationMechanics.unvanishPlayer(p);
			for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
				if (pl.getName().equalsIgnoreCase(p.getName())) {
					continue;
				}
				pl.showPlayer(p);
			}
			p.sendMessage(ChatColor.RED + "You are now " + ChatColor.BOLD + "visible.");
		} else {
			ModerationMechanics.vanishPlayer(p);
			p.sendMessage(ChatColor.GREEN + "You are now " + ChatColor.BOLD + "invisible.");
		}

		return true;
	}

}
