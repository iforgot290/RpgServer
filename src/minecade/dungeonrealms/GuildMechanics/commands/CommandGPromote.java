package minecade.dungeonrealms.GuildMechanics.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import minecade.dungeonrealms.GuildMechanics.GuildMechanics;

public class CommandGPromote implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player p = (Player) sender;
		if (args.length != 1) {
			p.sendMessage(
					ChatColor.RED + "" + ChatColor.BOLD + "Invalid Syntax. " + ChatColor.RED + "/gpromote <player>");
			return true;
		}

		if (!(GuildMechanics.inGuild(p))) {
			p.sendMessage(ChatColor.RED + "You must be in a " + ChatColor.BOLD + "GUILD" + ChatColor.RED + " to use "
					+ ChatColor.BOLD + "/gpromote.");
			return true;
		}

		if (!(GuildMechanics.isGuildLeader(p) || GuildMechanics.isGuildCoOwner(p))) {
			p.sendMessage(ChatColor.RED + "You must be the " + ChatColor.BOLD + "GUILD OWNER" + ChatColor.RED + " or "
					+ ChatColor.BOLD + "GUILD CO-OWNER" + ChatColor.RED + " to use " + ChatColor.BOLD + "/gpromote.");
			return true;
		}

		@SuppressWarnings("deprecation")
		OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);

		if (GuildMechanics.isGuildLeader(op.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "You can't promote the owner of a guild.");
			return true;
		}

		if (op.getName().equalsIgnoreCase(p.getName())) {
			p.sendMessage(ChatColor.RED + "You cannot promote yourself in your own guild.");
			return true;
		}

		if (!(GuildMechanics.areGuildies(p.getUniqueId(), op.getUniqueId()))) {
			p.sendMessage(ChatColor.RED + "" + ChatColor.UNDERLINE + op.getName() + ChatColor.RED
					+ " is not in YOUR guild.");
			return true;
		}

		if (!GuildMechanics.isGuildOfficer(op.getUniqueId()) && !GuildMechanics.isGuildCoOwner(op.getUniqueId())) {
			if (GuildMechanics.getRankNum(p) >= 3) {
				GuildMechanics.promoteToOfficer(op.getName(), op.getUniqueId(), p);
				return true;
			}
			p.sendMessage(ChatColor.RED + "You can't promote the guild owner.");
			return true;
		}

		if (GuildMechanics.isGuildCoOwner(p) && GuildMechanics.isGuildOfficer(op.getUniqueId())) {
			p.sendMessage(ChatColor.RED + "You aren't allowed to promote others to the rank of " + ChatColor.BOLD
					+ "CO-OWNER");
			return true;
		}

		if (GuildMechanics.getTotalCoOwnersCount(GuildMechanics.getGuild(p)) == 2
				&& GuildMechanics.isGuildLeader(p)) {
			p.sendMessage(ChatColor.RED + "You've already set 2 guild co-owners, demote one of them to set another!");
			return true;
		}

		if (GuildMechanics.isGuildLeader(p) && GuildMechanics.isGuildOfficer(op.getUniqueId())) {
			GuildMechanics.promoteToCoOwner(op.getName(), op.getUniqueId(), p);
		}
		return true;
	}
}