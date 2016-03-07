package minecade.dungeonrealms.GuildMechanics.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import minecade.dungeonrealms.CommunityMechanics.CommunityMechanics;
import minecade.dungeonrealms.GuildMechanics.GuildMechanics;
import minecade.dungeonrealms.ModerationMechanics.ModerationMechanics;
import minecade.dungeonrealms.managers.PlayerManager;

public class CommandGInvite implements CommandExecutor {
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player p = (Player) sender;
		if(!(GuildMechanics.inGuild(p))) {
			p.sendMessage(ChatColor.RED + "You must be in a " + ChatColor.BOLD + "GUILD" + ChatColor.RED + " to use " + ChatColor.BOLD + "/ginvite.");
			return true;
		}
		
		int g_rank = GuildMechanics.getRankNum(p.getName());
		if(g_rank < 2) { // 2 = officer, 3 = co-owner, 4 = owner -> 1 is just a member.
			p.sendMessage(ChatColor.RED + "You must be at least a guild " + ChatColor.BOLD + "OFFICER" + ChatColor.RED + " to use " + ChatColor.BOLD + "/ginvite");
			return true;
		}
		
		if(args.length != 1) {
			p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Invalid Syntax. " + ChatColor.RED + "/ginvite <player>");
			if(GuildMechanics.isGuildLeader(p.getName()) || GuildMechanics.isGuildCoOwner(p.getName())) {
				p.sendMessage(ChatColor.GRAY + "You can also " + ChatColor.UNDERLINE + "LEFT CLICK" + ChatColor.GRAY + " players with your " + ChatColor.ITALIC + "Guild Emblem" + ChatColor.GRAY + " to invite them.");
			}
			return true;
		}
		
		String p_name = args[0];
		
		if(p_name.equalsIgnoreCase(p.getName())) {
			p.sendMessage(ChatColor.RED + "You cannot invite yourself to your own guild.");
			return true;
		}
		
		if(Bukkit.getPlayer(p_name) == null) {
			p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + p_name + ChatColor.RED + " is OFFLINE");
			return true;
		}
		
		Player to_invite = Bukkit.getPlayer(p_name);
		
		if(PlayerManager.getPlayerModel(to_invite).getToggleList() != null && PlayerManager.getPlayerModel(to_invite).getToggleList().contains("guild")){
			if(!CommunityMechanics.isPlayerOnBuddyList(to_invite, p)) {
				// They're not buddies and this player doesn't want non-bud invites.
				p.sendMessage(ChatColor.RED + p_name + " has Non-BUD guild invites " + ChatColor.BOLD + "DISABLED");
				return true;
			}
		}
		
		if(CommunityMechanics.isPlayerOnIgnoreList(to_invite, p) || ModerationMechanics.isPlayerVanished(to_invite)) {
			p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + p_name + ChatColor.RED + " is OFFLINE");
			return true;
		}
		
		GuildMechanics.inviteToGuild(to_invite, p);
		return true;
	}
	
}