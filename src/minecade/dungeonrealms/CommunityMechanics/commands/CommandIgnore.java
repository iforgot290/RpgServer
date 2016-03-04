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

public class CommandIgnore implements CommandExecutor {

    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        final Player p = (Player) sender;

        if (cmd.getName().equalsIgnoreCase("crypt")) {
            if (p != null) {
                if (!(p.isOp())) {
                    return true;
                }
            }

            return true;
        }

        if (!(args.length == 1)) {
            p.sendMessage(ChatColor.RED + "Incorrect syntax - " + ChatColor.BOLD + "/ignore <PLAYER>");
            return true;
        }

        final String to_add = args[0];
        if (to_add.equalsIgnoreCase(p.getName())) {
            p.sendMessage(ChatColor.RED + "Why would you want to ignore yourself silly?");
            return true;
        }

        new BukkitRunnable() {
            
            public void run() {
            	@SuppressWarnings("deprecation")
    			OfflinePlayer op = Bukkit.getOfflinePlayer(to_add);
            	
            	if (CommunityMechanics.isPlayerOnIgnoreList(p, op)) {
                    p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + to_add + ChatColor.YELLOW + " is already on your IGNORE LIST.");
                    p.sendMessage(ChatColor.GRAY + "Use " + ChatColor.BOLD + "/delete " + to_add + ChatColor.GRAY + " to remove them from your ignore list.");
                    return;
                }

                if (CommunityMechanics.isPlayerOnBuddyList(p, op)) {
                    CommunityMechanics.deleteFromAllLists(p, op);
                }
            	
                CommunityMechanics.addIgnore(p, op);
                CommunityMechanics.updateCommBook(p);
                p.sendMessage(ChatColor.RED + "You've added " + ChatColor.BOLD + op.getName() + ChatColor.RED + " to your IGNORE list.");
            }
        }.runTaskLaterAsynchronously(Main.plugin, 1L);
        return true;
    }

}
