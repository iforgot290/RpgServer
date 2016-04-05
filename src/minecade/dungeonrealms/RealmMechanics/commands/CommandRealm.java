package minecade.dungeonrealms.RealmMechanics.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import minecade.dungeonrealms.RealmMechanics.RealmMechanics;

public class CommandRealm implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;

		String msg = "";
		for (String s : args) {
			if (!(s.contains("/realm"))) {
				msg += s + " ";
			}
		}

		RealmMechanics.realm_title.put(p.getUniqueId(), msg);
		p.sendMessage("");
		p.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "                       " + "* REALM TITLE SET *");
		p.sendMessage(ChatColor.GRAY + "\"" + msg + "\"");
		p.sendMessage("");
		return true;
	}

}