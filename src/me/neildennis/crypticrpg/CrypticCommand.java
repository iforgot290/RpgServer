package me.neildennis.crypticrpg;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;

public abstract class CrypticCommand implements CommandExecutor{

	protected abstract boolean sendUsage();
	public abstract boolean command(CrypticPlayer pl);
	public abstract boolean console();
	
	protected CommandSender sender;
	protected Command cmd;
	protected String label;
	protected String[] args;
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		this.sender = sender;
		this.cmd = cmd;
		this.label = label;
		this.args = args;
		
		if (sender instanceof Player){
			Player pl = (Player)sender;
			CrypticPlayer cpl = PlayerManager.getCrypticPlayer(pl);
			if (!command(cpl)) noPerms();
			return true;
		}
		
		console();
		return true;
	}
	
	public void noPerms(){
		sender.sendMessage(ChatColor.RED + "You do not have permission to do this");
	}
	
}
