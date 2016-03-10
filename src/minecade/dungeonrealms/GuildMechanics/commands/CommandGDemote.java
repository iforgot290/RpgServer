package minecade.dungeonrealms.GuildMechanics.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import minecade.dungeonrealms.GuildMechanics.GuildMechanics;

public class CommandGDemote implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player p = (Player) sender;
		if (args.length != 1) {
			p.sendMessage(
					ChatColor.RED + "" + ChatColor.BOLD + "Invalid Syntax. " + ChatColor.RED + "/gdemote <player>");
			return true;
		}

		if (!(GuildMechanics.inGuild(p))) {
			p.sendMessage(ChatColor.RED + "You must be in a " + ChatColor.BOLD + "GUILD" + ChatColor.RED + " to use "
					+ ChatColor.BOLD + "/gdemote.");
			return true;
		}

		if (!(GuildMechanics.isGuildLeader(p) || GuildMechanics.isGuildCoOwner(p))) {
			p.sendMessage(ChatColor.RED + "You must be the " + ChatColor.BOLD + "GUILD OWNER" + ChatColor.RED + " or "
					+ ChatColor.BOLD + "CO-OWNER" + ChatColor.RED + " to use " + ChatColor.BOLD + "/gdemote.");
			return true;
		}

		@SuppressWarnings("deprecation")
		OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);

		if (op.getName().equalsIgnoreCase(p.getName())) {
			p.sendMessage(ChatColor.RED + "You cannot demote yourself in your own guild.");
			return true;
		}

		if (!(GuildMechanics.areGuildies(p.getUniqueId(), op.getUniqueId()))) {
			p.sendMessage(
					ChatColor.RED + "" + ChatColor.BOLD + op.getName() + ChatColor.RED + " is not in YOUR guild.");
			return true;
		}

		if (!GuildMechanics.isGuildOfficer(op.getUniqueId()) && !GuildMechanics.isGuildCoOwner(op.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + op.getName() + ChatColor.RED + " is not yet a "
					+ ChatColor.UNDERLINE + "guild officer");
			p.sendMessage(ChatColor.GRAY + "Use " + ChatColor.RED + "/gpromote " + op.getName() + ChatColor.GRAY
					+ " to make them a guild officer.");
			return true;
		} else if (GuildMechanics.isGuildCoOwner(op.getUniqueId())) {
			if (GuildMechanics.isGuildLeader(p)) {
				GuildMechanics.demoteCoOwner(op.getName(), op.getUniqueId(), p);
				return true;
			}
			p.sendMessage(ChatColor.RED + "Only the " + ChatColor.BOLD + "GUILD OWNER" + ChatColor.RED
					+ " is able to demote co-owners.");
			return true;
		}
		GuildMechanics.demoteOfficer(op.getName(), op.getUniqueId(), p);
		return true;
	}

}