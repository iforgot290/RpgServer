package me.neildennis.crypticrpg;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;

public abstract class CrypticCommand implements CommandExecutor{

	protected abstract void sendUsage();
	public abstract void command(CrypticPlayer pl, Command cmd, String label, String[] args);
	public abstract void console(Command cmd, String label, String[] args);
	
	protected CommandSender sender;
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		this.sender = sender;
		
		if (sender instanceof Player){
			Player pl = (Player)sender;
			CrypticPlayer cpl = PlayerManager.getCrypticPlayer(pl);
			command(cpl, cmd, label, args);
			return true;
		}
		
		console(cmd, label, args);
		return true;
	}
	
	public void noPerms(){
		sender.sendMessage(ChatColor.RED + "You do not have permission to do this");
	}
	
}
