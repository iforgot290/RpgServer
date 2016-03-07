package minecade.dungeonrealms.DonationMechanics.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import minecade.dungeonrealms.Main;
import minecade.dungeonrealms.CommunityMechanics.CommunityMechanics;
import minecade.dungeonrealms.DonationMechanics.DonationMechanics;
import minecade.dungeonrealms.PermissionMechanics.PermissionMechanics;

public class CommandAddPet implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player p = null;
		if (sender instanceof Player) {
			p = (Player) sender;
			if (!(PermissionMechanics.isGM(p))) {
				return true;
			}
		}

		if (args.length != 2) {
			sender.sendMessage("Incorrect Syntax. /addpet <player> <pet>");
			return true;
		}

		final String pname = args[0];
		final String pet = args[1];

		sender.sendMessage("Added pet '" + pet + "' to player " + pname + ".");

		new BukkitRunnable() {

			@Override
			public void run() {

				@SuppressWarnings("deprecation")
				OfflinePlayer player = Bukkit.getOfflinePlayer(pname);

				DonationMechanics.addPetToPlayer(player, pet);
				CommunityMechanics.sendPacketCrossServer("[addpet]" + player.getUniqueId() + ":" + pet, -1, true);

				Main.log.info("[PetMechanics] Added pet '" + pet + "' to player " + player.getName() + ".");

			}

		}.runTaskAsynchronously(Main.plugin);

		return true;
	}

}