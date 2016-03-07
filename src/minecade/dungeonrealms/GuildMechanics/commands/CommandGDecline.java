package minecade.dungeonrealms.GuildMechanics.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import minecade.dungeonrealms.GuildMechanics.GuildMechanics;

public class CommandGDecline implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player p = (Player) sender;
		if (args.length != 0) {
			p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Invalid Syntax. " + ChatColor.RED + "/gdecline");
			return true;
		}

		if (!(GuildMechanics.guild_invite.containsKey(p.getUniqueId()))) {
			p.sendMessage(ChatColor.RED + "No pending guild invites.");
			return true;
		}

		String guild_name = GuildMechanics.guild_invite.get(p.getUniqueId());
		GuildMechanics.guild_invite.remove(p.getUniqueId());
		GuildMechanics.guild_inviter.remove(p.getUniqueId());
		GuildMechanics.guild_invite_time.remove(p.getUniqueId());
		p.sendMessage(ChatColor.RED + "Declined invitation to '" + ChatColor.BOLD + guild_name + "'" + ChatColor.RED
				+ "s guild.");
		if (Bukkit.getPlayer(guild_name) != null) {
			Player owner = Bukkit.getPlayer(guild_name);
			owner.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + p.getName() + ChatColor.RED.toString()
					+ " has DECLINED your guild invitation.");
		}
		return true;
	}

}