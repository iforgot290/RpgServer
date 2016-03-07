package minecade.dungeonrealms.BossMechanics.commands;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import minecade.dungeonrealms.BossMechanics.BossMechanics;
import minecade.dungeonrealms.MonsterMechanics.MonsterMechanics;

public class CommandSpawnBoss implements CommandExecutor {

	private Set<Material> air;

	public CommandSpawnBoss() {
		air = new HashSet<Material>();
		air.add(Material.AIR);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = null;
		Location loc = null;
		if (sender instanceof Player) {
			p = (Player) sender;
			if (!(p.isOp())) {
				return true;
			}
			loc = p.getTargetBlock(air, 16).getLocation();
		}

		if (sender instanceof BlockCommandSender) {
			BlockCommandSender cb = (BlockCommandSender) sender;
			loc = cb.getBlock().getLocation();
		}

		if (args.length != 1) {
			return true; // Do nothing, did not give us a
							// boss name!
		}

		loc.add(0, 2, 0);

		String boss_name = args[0];
		if (boss_name.equalsIgnoreCase("unholy_priest")) {
			Entity boss = MonsterMechanics.spawnBossMob(loc, EntityType.SKELETON, "wither", "Burick The Fanatic", 3);
			BossMechanics.boss_map.put(boss, "unholy_priest");
			for (Player pl : boss.getWorld().getPlayers()) {
				pl.sendMessage(ChatColor.GOLD + "" + ChatColor.UNDERLINE + "Burick The Fanatic: " + ChatColor.WHITE
						+ "Ahahaha! You dare try to kill ME?! I am Burick, disciple of Goragath! None of you will leave this place alive!");
			}
			// TODO old sound was ENDERDRAGON_HIT
			boss.getWorld().playSound(boss.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 4F, 0.5F);
		}

		if (boss_name.equalsIgnoreCase("tnt_bandit")) {
			Entity boss = MonsterMechanics.spawnBossMob(loc, EntityType.SKELETON, "bandit", "Mad Bandit Pyromancer", 1);
			BossMechanics.boss_map.put(boss, "tnt_bandit");
			for (Player pl : boss.getWorld().getPlayers()) {
				pl.sendMessage(ChatColor.GOLD + "" + ChatColor.UNDERLINE + "Mad Bandit Pyromancer: " + ChatColor.WHITE
						+ "WAHAHAHA! EXPLOSIONS! BOOM, BOOM, BOOM! I'm gonna blow you all up!");
			}
			// TODO old sound was tnt exploding
			boss.getWorld().playSound(boss.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);
		}

		if (boss_name.equalsIgnoreCase("bandit_leader")) {
			Entity boss = MonsterMechanics.spawnBossMob(loc, EntityType.SKELETON, "bandit", "Mayel The Cruel", 1);
			BossMechanics.boss_map.put(boss, "bandit_leader");
			for (Player pl : boss.getWorld().getPlayers()) {
				pl.sendMessage(ChatColor.GOLD + "" + ChatColor.UNDERLINE + "Mayel The Cruel: " + ChatColor.WHITE
						+ "How dare you challenge ME, the leader of the Cyrene Bandits! To me, my brethern, let us crush these incolents!");
			}
			boss.getWorld().playSound(boss.getLocation(), Sound.AMBIENT_CAVE, 1F, 1F);
		}

		if (boss_name.equalsIgnoreCase("fire_demon")) {
			Entity boss = MonsterMechanics.spawnBossMob(loc, EntityType.SKELETON, "wither", "The Infernal Abyss", 4);
			BossMechanics.boss_map.put(boss, "fire_demon");
			for (Player pl : boss.getWorld().getPlayers()) {
				pl.sendMessage(ChatColor.GOLD + "" + ChatColor.UNDERLINE + "The Infernal Abyss: " + ChatColor.WHITE
						+ "... I have nothing to say to you foolish mortals, except for this: Burn.");
			}
			boss.getWorld().playSound(boss.getLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 1F, 1F);
		}
		if (boss_name.equalsIgnoreCase("aceron")) {
			Entity boss = MonsterMechanics.spawnBossMob(loc, EntityType.SKELETON, "wither", "Aceron the Wicked", 4);
			BossMechanics.boss_map.put(boss, "aceron");
			for (Player pl : boss.getWorld().getPlayers()) {
				pl.sendMessage(ChatColor.GOLD + "" + ChatColor.UNDERLINE + "Aceron the Wicked: " + ChatColor.WHITE
						+ "Aceron is here! Welcome to my cave of treasures!");
			}
			boss.getWorld().playSound(boss.getLocation(), Sound.ENTITY_LIGHTNING_THUNDER, 1F, 1F);
		}
		return true;
	}

}
