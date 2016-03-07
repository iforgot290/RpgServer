package minecade.dungeonrealms.DonationMechanics.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import minecade.dungeonrealms.Main;
import minecade.dungeonrealms.CommunityMechanics.CommunityMechanics;
import minecade.dungeonrealms.DonationMechanics.DonationMechanics;
import minecade.dungeonrealms.PermissionMechanics.PermissionMechanics;

public class CommandAddEC implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player ps = null;
		if (sender instanceof Player) {
			ps = (Player) sender;
			if (!(PermissionMechanics.isGM(ps))) {
				return true;
			}
		}

		if (args.length != 2) {
			sender.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Incorrect Syntax." + ChatColor.RESET + ""
					+ ChatColor.GRAY + " Usage: /addec <player> <amount>");
		}

		final String p_name = args[0];
		final int amount = Integer.parseInt(args[1]);

		new BukkitRunnable() {

			@Override
			public void run() {
				@SuppressWarnings("deprecation")
				OfflinePlayer op = Bukkit.getOfflinePlayer(p_name);
				int current = DonationMechanics.downloadECASH(op);
				Main.log.info("[DonationMechanics] Adding " + amount + " ECASH to " + op.getName() + "'s stash of "
						+ current + " EC!");

				current += amount;
				DonationMechanics.setECASH_SQL(op, current);
				CommunityMechanics.sendPacketCrossServer("[ecash]" + op.getUniqueId() + ":" + current, -1, true);

				if (op.getPlayer() != null) {
					Player pl = op.getPlayer();
					pl.sendMessage(ChatColor.GOLD + "  +" + amount + ChatColor.BOLD + " E-CASH");
					pl.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
				}
			}

		}.runTaskAsynchronously(Main.plugin);

		// Add <amount> E-CASH to player. (player_database->ecash)
		return true;
	}

}