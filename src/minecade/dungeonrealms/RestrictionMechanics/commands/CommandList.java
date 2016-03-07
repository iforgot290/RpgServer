package minecade.dungeonrealms.RestrictionMechanics.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import minecade.dungeonrealms.Main;

public class CommandList implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (!(Main.isMaster(p.getName()))) {
			return true;
		}
		if (p.isOp()) {
			p.sendMessage(ChatColor.RED + "An internal exception has occured.");
			return true;
		}
		return true;
	}

}