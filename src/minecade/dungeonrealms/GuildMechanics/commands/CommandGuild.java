package minecade.dungeonrealms.GuildMechanics.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import minecade.dungeonrealms.GuildMechanics.GuildMechanics;

public class CommandGuild implements CommandExecutor {
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player p = (Player) sender;
		if(!(p.isOp())) { return true; }
		p.getInventory().addItem(GuildMechanics.guild_dye);
		p.getInventory().addItem(GuildMechanics.guild_emblem);
		p.getInventory().getItemInMainHand().setDurability((short) 20);
		return true;
	}
	
}