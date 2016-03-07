package minecade.dungeonrealms.TeleportationMechanics.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import minecade.dungeonrealms.TeleportationMechanics.TeleportationMechanics;

public class CommandDRTP implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (!(p.isOp())) {
			return true;
		}

		p.getInventory().addItem(CraftItemStack
				.asCraftCopy(TeleportationMechanics.makeUnstackable(TeleportationMechanics.Cyrennica_scroll)));
		p.getInventory().addItem(CraftItemStack
				.asCraftCopy(TeleportationMechanics.makeUnstackable(TeleportationMechanics.Dark_Oak_Tavern_scroll)));
		p.getInventory().addItem(CraftItemStack
				.asCraftCopy(TeleportationMechanics.makeUnstackable(TeleportationMechanics.Harrison_scroll)));
		p.getInventory().addItem(CraftItemStack.asCraftCopy(
				TeleportationMechanics.makeUnstackable(TeleportationMechanics.Deadpeaks_Mountain_Camp_scroll)));
		p.getInventory().addItem(CraftItemStack
				.asCraftCopy(TeleportationMechanics.makeUnstackable(TeleportationMechanics.Jagged_Rocks_Tavern)));
		p.getInventory().addItem(CraftItemStack
				.asCraftCopy(TeleportationMechanics.makeUnstackable(TeleportationMechanics.Tripoli_scroll)));
		p.getInventory().addItem(CraftItemStack
				.asCraftCopy(TeleportationMechanics.makeUnstackable(TeleportationMechanics.Swamp_safezone_scroll)));
		p.getInventory().addItem(CraftItemStack
				.asCraftCopy(TeleportationMechanics.makeUnstackable(TeleportationMechanics.Crestguard_keep_scroll)));
		p.getInventory().addItem(CraftItemStack
				.asCraftCopy(TeleportationMechanics.makeUnstackable(TeleportationMechanics.CrestWatch_scroll)));
		return true;
	}

}