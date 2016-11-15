package me.neildennis.crypticrpg.monsters.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import me.neildennis.crypticrpg.CrypticCommand;
import me.neildennis.crypticrpg.monsters.MobManager;
import me.neildennis.crypticrpg.monsters.spawnblock.SpawnBlock;
import me.neildennis.crypticrpg.permission.Rank;
import me.neildennis.crypticrpg.player.CrypticPlayer;

public class CommandSpawner extends CrypticCommand{

	@Override
	protected boolean sendUsage() {
		sender.sendMessage(new String[]{ChatColor.RED + "Usage:",
				ChatColor.RED + "/" + label + " show <radius>",
				ChatColor.RED + "/" + label + " hide <radius>",
				ChatColor.RED + "/" + label + " hideall [-f]"});
		return true;
	}

	@Override
	public boolean command(CrypticPlayer pl) {
		if (pl.getRank().getPriority() < Rank.ADMIN.getPriority()) return false;
		if (args.length == 0) return sendUsage();

		if (args[0].equalsIgnoreCase("show")){
			if (args.length < 2) return sendUsage();

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
			if (args.length < 2) return sendUsage();

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
		
		return true;
	}

	@Override
	public boolean console() {
		if (args.length > 0 && args[0].equalsIgnoreCase("hideall")){
			for (SpawnBlock blk : MobManager.getSpawnBlocks())
				blk.setVisible(false);
			sender.sendMessage(ChatColor.GREEN + "All spawn blocks have been hidden");
		} else {
			sender.sendMessage(ChatColor.RED + "Only hideall option available in console");
		}
		return true;
	}

}
