package minecade.dungeonrealms.SpawnMechanics.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import minecade.dungeonrealms.SpawnMechanics.SpawnMechanics;

public class CommandSpawn implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (!(p.isOp())) {
			return true;
		}
		Location respawn_location = SpawnMechanics.getRandomSpawnPoint(p);
		p.teleport(respawn_location);
		return true;
	}

}