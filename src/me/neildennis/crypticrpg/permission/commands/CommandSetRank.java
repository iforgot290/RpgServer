package me.neildennis.crypticrpg.permission.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neildennis.crypticrpg.CrypticCommand;
import me.neildennis.crypticrpg.permission.Rank;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;
import net.md_5.bungee.api.ChatColor;

public class CommandSetRank extends CrypticCommand{
	
	@Override
	protected void sendUsage(){
		sender.sendMessage(ChatColor.RED + "/setrank <player> <rank>");
	}

	@Override
	public void command(Player pl, Command cmd, String label, String[] args) {
		CrypticPlayer cp = PlayerManager.getCrypticPlayer(pl);
		
		if (cp.getRankData().getRank().getPriority() < Rank.ADMIN.getPriority()){
			noPerms();
			return;
		}
		
		if (args.length == 0){
			sendUsage();
			return;
		}
		
		UUID id;
		CrypticPlayer toset;
		Rank rank;
		
		if (args.length == 1) {
			toset = cp;
			id = cp.getId();
			rank = Rank.valueOf(args[0]);
		} else {
			@SuppressWarnings("deprecation")
			OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
			id = op.getUniqueId();
			toset = PlayerManager.getCrypticPlayer(op);
			rank = Rank.valueOf(args[1]);
		}
		
		if (toset == null){
			
		}
		
		
		
	}

	@Override
	public void console(Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		
	}

}
