package minecade.dungeonrealms.GuildMechanics.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import minecade.dungeonrealms.GuildMechanics.GuildMechanics;

public class CommandG implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player p = (Player) sender;
		if (!(GuildMechanics.inGuild(p.getUniqueId()))) {
			p.sendMessage(ChatColor.RED + "You must be in a " + ChatColor.BOLD + "GUILD" + ChatColor.RED + " to use "
					+ ChatColor.BOLD + "GUILD CHAT.");
			p.sendMessage(ChatColor.GRAY + "Use /gl (or tab) to speak in global.");
			return true;
		}

		if (args.length == 0) {
			if (!(GuildMechanics.guild_only.contains(p.getUniqueId()))) {
				GuildMechanics.guild_only.add(p.getUniqueId());
				p.sendMessage(ChatColor.DARK_AQUA + "Messages will now be default sent to <" + ChatColor.BOLD
						+ GuildMechanics.guild_handle_map.get(GuildMechanics.getGuild(p.getUniqueId()))
						+ ChatColor.DARK_AQUA + ">. Type " + ChatColor.UNDERLINE + "/l <msg>" + ChatColor.DARK_AQUA
						+ " to speak in local.");
				p.sendMessage(ChatColor.GRAY + "To change back to default local, type " + ChatColor.BOLD + "/g"
						+ ChatColor.GRAY + " again.");
			} else if (GuildMechanics.guild_only.contains(p.getUniqueId())) {
				GuildMechanics.guild_only.remove(p.getUniqueId());
				p.sendMessage(ChatColor.GRAY + "Messages will now be default sent to local chat.");
			}
			return true;
		}

		String msg = "";

		for (String s : args) {
			msg += s + " ";
		}

		if (msg.endsWith(" ")) {
			msg = msg.substring(0, (msg.length() - 1));
		}

		GuildMechanics.sendMessageToGuild(p, msg);
		// TODO: Cross-server
		return true;
	}

}