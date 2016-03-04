package minecade.dungeonrealms.DonationMechanics.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import minecade.dungeonrealms.KarmaMechanics.KarmaMechanics;
import minecade.dungeonrealms.PermissionMechanics.PermissionMechanics;

public class CommandBank implements CommandExecutor {



	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		String rank = PermissionMechanics.getRank(p);

		if (!rank.equalsIgnoreCase("SUB++") || !PermissionMechanics.isGM(p)){
			p.sendMessage(ChatColor.RED + "This feature is just for Sub++");
			return true;

		}

		if (KarmaMechanics.getAlignment(p).contains("good")) {
			p.openInventory(p.getEnderChest());
			return true;
		} else {
			p.sendMessage(ChatColor.RED + "You cannot do this while chaotic or neutral!");
			return true;
		}
		
	}
}