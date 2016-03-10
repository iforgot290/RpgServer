package minecade.dungeonrealms.GuildMechanics.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import minecade.dungeonrealms.GuildMechanics.GuildMechanics;

public class CommandGMotd implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player p = (Player) sender;
		if (!(GuildMechanics.inGuild(p))) {
			p.sendMessage(ChatColor.RED + "You must be in a " + ChatColor.BOLD + "GUILD" + ChatColor.RED + " to view "
					+ ChatColor.BOLD + "/gmotd.");
			return true;
		}

		if (args.length == 0) { // Just show the message.
			// TODO: Show message.
			if ((!(GuildMechanics.guild_motd.containsKey(GuildMechanics.getGuild(p))))
					|| GuildMechanics.guild_motd.get(GuildMechanics.getGuild(p)) == null) {
				// NO MOTD.
				p.sendMessage(
						ChatColor.RED + "No " + ChatColor.BOLD + "GUILD MOTD" + ChatColor.RED + " currently set.");
				p.sendMessage(ChatColor.GRAY + "Use /gmotd <motd> to set your guild's message of the day.");
				return true;
			}
			String g_name = GuildMechanics.getGuild(p);
			String motd = GuildMechanics.guild_motd.get(g_name);
			p.sendMessage(ChatColor.DARK_AQUA + "<" + ChatColor.BOLD + GuildMechanics.guild_handle_map.get(g_name)
					+ ChatColor.DARK_AQUA + "> " + ChatColor.BOLD + "MOTD: " + ChatColor.DARK_AQUA + motd);
			return true;
		}

		if (args.length >= 1) {
			// Setting the MOTD.

			if (!(GuildMechanics.isGuildLeader(p) || GuildMechanics.isGuildCoOwner(p))) {
				p.sendMessage(ChatColor.RED + "You must be the " + ChatColor.BOLD + "GUILD OWNER" + ChatColor.RED
						+ " to use " + ChatColor.BOLD + "/gmotd <motd>.");
				return true;
			}

			String new_motd = "";

			for (String s : args) {
				new_motd = new_motd + s + " ";
			}

			if (new_motd.contains("$")) {
				p.sendMessage(ChatColor.RED + "MOTD contains illegal character '$'.");
				return true;
			}

			new_motd = new_motd.substring(0, new_motd.length() - 1);
			GuildMechanics.setGuildMOTD(GuildMechanics.getGuild(p), new_motd);
		}
		return true;
	}

}