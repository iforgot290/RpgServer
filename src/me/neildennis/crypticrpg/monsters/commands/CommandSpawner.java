package me.neildennis.crypticrpg.monsters.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;

import me.neildennis.crypticrpg.CrypticCommand;
import me.neildennis.crypticrpg.monsters.MobManager;
import me.neildennis.crypticrpg.monsters.SpawnBlock;
import me.neildennis.crypticrpg.permission.Rank;
import me.neildennis.crypticrpg.player.CrypticPlayer;

public class CommandSpawner extends CrypticCommand{

	@Override
	protected void sendUsage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void command(CrypticPlayer pl, Command cmd, String label, String[] args) {
		if (pl.getRank().getPriority() < Rank.ADMIN.getPriority()){
			this.noPerms();
			return;
		}

		if (args.length == 0){
			sendUsage();
			return;
		}

		if (args[0].equalsIgnoreCase("show")){
			if (args.length < 2){
				sendUsage();
				return;
			}
			
			try {
				int radius = Integer.parseInt(args[1]);
				Location ploc = pl.getPlayer().getLocation();
				int shown = 0;
				for (SpawnBlock blk : MobManager.getSpawnBlocks()){
					if (blk.isBlockShown()) continue;
					if (ploc.distanceSquared(blk.getLocation()) <= radius * radius){
						blk.setVisible(true);
						shown++;
					}
				}
				pl.sendMessage(ChatColor.GREEN + "Showing " + shown + " spawners in a " + radius + " block radius");
			} catch (NumberFormatException e){
				sendUsage();
			}
		}
		
		else if (args[0].equalsIgnoreCase("hide")){
			if (args.length < 2){
				sendUsage();
				return;
			}
			
			try {
				int radius = Integer.parseInt(args[1]);
				Location ploc = pl.getPlayer().getLocation();
				int hidden = 0;
				for (SpawnBlock blk : MobManager.getSpawnBlocks()){
					if (!blk.isBlockShown()) continue;
					if (ploc.distanceSquared(blk.getLocation()) <= radius * radius){
						blk.setVisible(false);
						hidden++;
					}
				}
				pl.sendMessage(ChatColor.GREEN + "Hid " + hidden + " spawners in a " + radius + " block radius");
			} catch (NumberFormatException e){
				sendUsage();
			}
		}
		
		else if (args[0].equalsIgnoreCase("hideall")){
			boolean force = false;
			if (args.length == 2){
				if (args[1].equalsIgnoreCase("-f")) force = true;
			}
			
			int hidden = 0;
			for (SpawnBlock blk : MobManager.getSpawnBlocks()){
				if (!force && !blk.isBlockShown()) continue;
				blk.setVisible(false);
				hidden++;
			}
			pl.sendMessage(ChatColor.GREEN + "Hid " + hidden + " spawners");
		}
	}

	@Override
	public void console(Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub

	}

}
