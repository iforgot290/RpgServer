package minecade.dungeonrealms.CommunityMechanics.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import minecade.dungeonrealms.Main;
import minecade.dungeonrealms.CommunityMechanics.CommunityMechanics;

public class CommandDelete implements CommandExecutor {
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player p = (Player) sender;
		
		if(cmd.getName().equalsIgnoreCase("crypt")) {
			if(p != null) {
				if(!(p.isOp())) { return true; }
			}
			
			return true;
		}
		
		if(!(args.length == 1)) {
			p.sendMessage(ChatColor.RED + "Incorrect syntax - " + ChatColor.BOLD + "/delete <PLAYER>");
			return true;
		}
		
		final String op_name = args[0];
		
		new BukkitRunnable() {
			
			public void run() {
				@SuppressWarnings("deprecation")
				OfflinePlayer to_remove = Bukkit.getOfflinePlayer(op_name);
				CommunityMechanics.deleteFromAllLists(p, to_remove);
				CommunityMechanics.updateCommBook(p);
			}
		}.runTaskLaterAsynchronously(Main.plugin, 1L);
		
		// TODO: Send "X has logged out" to person who was deleted.
		return true;
	}
	
}
