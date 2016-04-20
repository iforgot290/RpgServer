package me.neildennis.crypticrpg;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class CrypticCommand implements CommandExecutor{

	protected abstract void sendUsage();
	public abstract void command(Player pl, Command cmd, String label, String[] args);
	public abstract void console(Command cmd, String label, String[] args);
	
	protected CommandSender sender;
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		this.sender = sender;
		
		if (sender instanceof Player){
			Player pl = (Player)sender;
			command(pl, cmd, label, args);
			return true;
		}
		
		console(cmd, label, args);
		return true;
	}
	
	public void noPerms(){
		sender.sendMessage(ChatColor.RED + "You do not have permission to do this");
	}
	
}
