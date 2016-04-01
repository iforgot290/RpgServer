package minecade.dungeonrealms.ModerationMechanics.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import minecade.dungeonrealms.ChatMechanics.ChatMechanics;
import minecade.dungeonrealms.CommunityMechanics.CommunityMechanics;
import minecade.dungeonrealms.ModerationMechanics.ModerationMechanics;
import minecade.dungeonrealms.PermissionMechanics.PermissionMechanics;

public class CommandUnmute implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = null;
		if (sender instanceof Player) {
			p = (Player) sender;
		}

		if (p != null) {
			String rank = PermissionMechanics.getRank(p);
			if (rank == null) {
				return true;
			}

			if (!rank.equalsIgnoreCase("gm") && !p.isOp()) {
				return true;
			}
		}

		if (args.length != 1) {
			if (p != null) {
				p.sendMessage(
						ChatColor.RED + "" + ChatColor.BOLD + "Invalid Syntax. " + ChatColor.RED + "/unmute <PLAYER>");
			}
			return true;
		}

		@SuppressWarnings("deprecation")
		OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);

		ChatMechanics.mute_list.remove(op.getUniqueId());
		ChatMechanics.setMuteStateSQL(op.getUniqueId());
		if (p != null) {
			p.sendMessage(ChatColor.AQUA + "You have " + ChatColor.BOLD + "UNMUTED " + ChatColor.AQUA + op.getName());
		} else if (p == null) {
			ModerationMechanics.log.info("[ModerationMechanics] Unmuted player " + op.getName() + ".");
		}

		if (op.isOnline()) {
			Player p_2unmute = op.getPlayer();
			p_2unmute.sendMessage("");
			p_2unmute.sendMessage(ChatColor.GREEN + "Your " + ChatColor.BOLD + "GLOBAL MUTE" + ChatColor.GREEN
					+ " has been removed.");
			p_2unmute.sendMessage("");
		} else if (ModerationMechanics.isPlayerOnline(op)) {
			int server_num = ModerationMechanics.getPlayerServer(op);
			CommunityMechanics.sendPacketCrossServer("@unmute@" + op.getUniqueId(), server_num, false);
			// ConnectProtocol.sendResultCrossServer(CommunityMechanics.server_list.get(server_num),
			// "@unmute@" + p_name_2unmute);
		}

		return true;
	}

}
