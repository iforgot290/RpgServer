package minecade.dungeonrealms.AchievementMechanics;

import java.util.Random;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import minecade.dungeonrealms.Main;
import minecade.dungeonrealms.DuelMechanics.DuelMechanics;
import minecade.dungeonrealms.GuildMechanics.GuildMechanics;
import minecade.dungeonrealms.Hive.Hive;
import minecade.dungeonrealms.Hive.ParticleEffect;
import minecade.dungeonrealms.ItemMechanics.ItemMechanics;
import minecade.dungeonrealms.LevelMechanics.LevelMechanics;
import minecade.dungeonrealms.MoneyMechanics.MoneyMechanics;
import minecade.dungeonrealms.PermissionMechanics.PermissionMechanics;
import minecade.dungeonrealms.PetMechanics.PetMechanics;
import minecade.dungeonrealms.RealmMechanics.RealmMechanics;
import minecade.dungeonrealms.managers.PlayerManager;

public class AchievementMechanics implements Listener {
	static Logger log = Logger.getLogger("Minecraft");

	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.plugin);

		// TODO convert to event based
		// Check all current player zones / locations for 'exploration' achievs.
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
			public void run() {
				for (Player pl : Bukkit.getOnlinePlayers()) {
					String region = DuelMechanics.getRegionName(pl.getLocation());
					if (region.equalsIgnoreCase("cityofcyrennica")) {
						addAchievement(pl, "Explorer: Cyrennica");
						continue;
					}
					if (region.equalsIgnoreCase("villagesafe")) {
						addAchievement(pl, "Explorer: Harrisons Fields");
						continue;
					}
					if (region.equalsIgnoreCase("plainsofcyrene")) {
						addAchievement(pl, "Explorer: Plains of Cyrene");
						continue;
					}
					if (region.equalsIgnoreCase("darkoakwild2")) {
						addAchievement(pl, "Explorer: Darkoak");
						continue;
					}
					if (region.equalsIgnoreCase("infrontoftavern")) { // TODO:
																		// This
																		// is
																		// just
																		// an
																		// entrance
																		// region.
						addAchievement(pl, "Explorer: Jagged Rocks");
						continue;
					}
					if (region.equalsIgnoreCase("goblincity")) {
						addAchievement(pl, "Explorer: Skullneck");
						continue;
					}
					if (region.equalsIgnoreCase("trollcity1")) {
						addAchievement(pl, "Explorer: Trollingor");
						continue;
					}
					if (region.equalsIgnoreCase("crystalpeakt")) {
						addAchievement(pl, "Explorer: Crystalpeak Tower");
						continue;
					}
					if (region.equalsIgnoreCase("transitional3")) {
						addAchievement(pl, "Explorer: Helmchen");
						continue;
					}
					if (region.equalsIgnoreCase("alsahra")) {
						addAchievement(pl, "Explorer: Al Sahra");
						continue;
					}
					if (region.equalsIgnoreCase("savannahsafezone")) {
						addAchievement(pl, "Explorer: Tripoli");
						continue;
					}
					if (region.equalsIgnoreCase("swampvillage_2")) {
						addAchievement(pl, "Explorer: Dreadwood");
						continue;
					}
					if (region.equalsIgnoreCase("swamp_1")) {
						addAchievement(pl, "Explorer: Gloomy Hallows");
						continue;
					}
					if (region.equalsIgnoreCase("crestguard")) {
						addAchievement(pl, "Explorer: Avalon Peaks");

						continue;
					}
					if (region.equalsIgnoreCase("cstrip6")) {
						addAchievement(pl, "Explorer: The Frozen North");
						continue;
					}
					if (region.equalsIgnoreCase("underworld")) {
						addAchievement(pl, "Explorer: The Lost City of Avalon");
						continue;
					}
					if (region.equalsIgnoreCase("Cheifs")) {
						addAchievement(pl, "Explorer: Cheif\'s Glory");
						continue;
					}
					if (region.equalsIgnoreCase("Dead_Peaks")) {
						addAchievement(pl, "Explorer: Deadpeaks");
						continue;
					}
					if (region.equalsIgnoreCase("Mure")) {
						addAchievement(pl, "Explorer: Mure");
						continue;
					}
					if (region.equalsIgnoreCase("Sebrata")) {
						addAchievement(pl, "Explorer: Sebrata");
						continue;
					}
					if (pl.getWorld().getName().contains("fireydungeon")) {
						addAchievement(pl, "Explorer: The Infernal Abyss");
						continue;
					}
					if (region.equalsIgnoreCase("tutorial_island")) {
						addAchievement(pl, "Explorer: Tutorial Island");
						continue;
					}
				}
			}
		}, 30 * 20L, 10 * 20L);

		log.info("[AchievementMechanics] V1.0 has been enabled.");
	}

	public void onDisable() {
		log.info("[AchievementMechanics] V1.0 has been disabled.");
	}

	/**
	 * Handles join event
	 * 
	 * Calls the process login achievements
	 * 
	 * @param e
	 *            Bukkit join event
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent e) {
		final Player pl = e.getPlayer();
		new BukkitRunnable() {

			public void run() {
				processLoginAchievements(pl);
			}
		}.runTaskLater(Main.plugin, 20L);
	}

	/**
	 * Handles opening a loot chest
	 * 
	 * Gives achievement if the loot chest contains another chest inside
	 * 
	 * @param e
	 *            Bukkit inventory open event
	 */
	@EventHandler
	public void onInventoryOpenEvent(InventoryOpenEvent e) {
		if (e.getInventory().getName().equalsIgnoreCase("Loot Chest")) {
			if (e.getInventory().contains(Material.CHEST)) {
				Player pl = (Player) e.getPlayer();
				addAchievement(pl, "A chest within a chest");
			}
		}
	}
	
	/**
	 * Adds achievement to player, also checks for a few other achievements
	 * @param player player to add achievement to
	 * @param achieve achievement to add
	 */
	public static void addAchievement(Player player, String achieve){
		addAchievement(player.getUniqueId(), achieve);
	}

	/**
	 * Adds achievement to player, also checks for a few other achievements
	 * 
	 * @param id
	 *            player to add achievement to
	 * @param achieve
	 *            achievement to add
	 */
	public static void addAchievement(UUID id, String achieve) {
		if (hasAchievement(id, achieve)) {
			return;
		}

		if (PlayerManager.getPlayerModel(id).getAchievements() == null) {
			PlayerManager.getPlayerModel(id).setAchievements(achieve);
			return;
		}

		String other_achieve = PlayerManager.getPlayerModel(id).getAchievements();

		if (other_achieve.endsWith(",")) {
			other_achieve = other_achieve + achieve + ",";
		} else {
			other_achieve = other_achieve + "," + achieve + ",";
		}

		PlayerManager.getPlayerModel(id).setAchievements(other_achieve);

		if (Bukkit.getPlayer(id) != null) {
			Player player = Bukkit.getPlayer(id);
			if (achieve.equalsIgnoreCase("Explorer: The Lost City of Avalon")) {
				LevelMechanics.addXP(player, 1000);
			} else if (achieve.equalsIgnoreCase("Explorer: Avalon Peaks")) {
				LevelMechanics.addXP(player, 750);
			} else if (achieve.equalsIgnoreCase("Explorer: Gloomy Hallows")) {
				LevelMechanics.addXP(player, 500);
			}

			if (!Hive.first_login.contains(player.getUniqueId())
					&& Hive.forum_usergroup.containsKey(player.getUniqueId())
					&& Hive.forum_usergroup.get(player.getUniqueId()) == -1) {
				player.sendMessage(
						ChatColor.RED + "You just earned the '" + ChatColor.UNDERLINE + achieve + ChatColor.RED
								+ "' Achievement, unfortunetly you can't claim this achievement until you register at "
								+ ChatColor.UNDERLINE + "dungeonrealms.net/forum/register.php");
			} else {
				player.sendMessage(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD.toString() + ">> "
						+ ChatColor.DARK_AQUA.toString() + ChatColor.UNDERLINE.toString() + "Achievement Unlocked:"
						+ ChatColor.DARK_AQUA.toString() + " '" + ChatColor.GRAY + achieve
						+ ChatColor.DARK_AQUA.toString() + "'!");
				player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F, 1F);

				try {
					ParticleEffect.sendToLocation(ParticleEffect.TOWN_AURA, player.getLocation().add(0, 2, 0),
							new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 1F, 10);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		log.info("[AchievementMechanics] Added achievement '" + achieve + "' to player " + id.toString() + "!");

		int achievement_count = other_achieve.split(",").length;
		if (achievement_count >= 10) {
			addAchievement(id, "Dungeon Realms Novice");
			if (achievement_count >= 25) {
				addAchievement(id, "Dungeon Realms Apprentice");
				if (achievement_count >= 50) {
					addAchievement(id, "Dungeon Realms Adept");
					if (achievement_count >= 100) {
						addAchievement(id, "Dungeon Realms Expert");
						if (achievement_count >= 200) {
							addAchievement(id, "Dungeon Realms Master");
						}
					}
				}
			}
		}

		int explorer_count = getExplorerAchievementCount(id);
		if (explorer_count >= 10) {
			addAchievement(id, "Tourist");
			if (explorer_count >= 20) {
				addAchievement(id, "Adventurer");
			}
		}

	}
	
	/**
	 * Gets how many explorer achievements the player has
	 * @param player player to check
	 * @return amount of explorer achievements
	 */
	public static int getExplorerAchievementCount(Player player){
		return getExplorerAchievementCount(player.getUniqueId());
	}

	/**
	 * Gets how many explorer achievements the player has
	 * 
	 * @param id
	 *            player to check
	 * @return amount of explorer achievements
	 */
	public static int getExplorerAchievementCount(UUID id) {
		int count = 0;
		if (PlayerManager.getPlayerModel(id).getAchievements() != null) {
			for (String s : PlayerManager.getPlayerModel(id).getAchievements().split(",")) {
				if (s.toLowerCase().contains("explorer:")) {
					count++;
				}
			}
		}

		return count;
	}

	/**
	 * Checks if the player has an achievement
	 * 
	 * @param player
	 *            player to check
	 * @param a
	 *            achievement to check
	 * @return true if the player has the achievement
	 */
	public static boolean hasAchievement(Player player, String a) {
		return hasAchievement(player.getUniqueId(), a);
	}
	
	/**
	 * Checks if the player has an achievement
	 * 
	 * @param id player to check
	 * @param a achievement to check
	 * @return true if the player has the achievement
	 */
	public static boolean hasAchievement(UUID id, String a){
		if (PlayerManager.getPlayerModel(id).getAchievements() == null) {
			return false;
		}
		String al = PlayerManager.getPlayerModel(id).getAchievements();
		for (String achiev : al.split(",")) {
			if (achiev.equalsIgnoreCase(a)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Processes login achievements
	 * 
	 * @param player
	 *            player to process login achievements for
	 */
	public static void processLoginAchievements(Player player) {

		if (!(Hive.first_login.contains(player.getUniqueId()))) {
			AchievementMechanics.addAchievement(player, "Explorer: Tutorial Island");
		}

		if (RealmMechanics.realm_tier.containsKey(player.getUniqueId())) {
			int tier = RealmMechanics.realm_tier.get(player.getUniqueId());
			if (tier >= 2) {
				AchievementMechanics.addAchievement(player, "Expanding I");
				if (tier >= 4) {
					AchievementMechanics.addAchievement(player, "Expanding II");
					if (tier >= 6) {
						AchievementMechanics.addAchievement(player, "Expanding III");
						if (tier == 7) {
							AchievementMechanics.addAchievement(player, "Expanding IV");
						}
					}
				}
			}
		}

		if (MoneyMechanics.bank_map.containsKey(player.getUniqueId())) {
			int bank_net = MoneyMechanics.bank_map.get(player.getUniqueId());
			if (bank_net >= 100) {
				AchievementMechanics.addAchievement(player, "Acquire Currency I");
				if (bank_net >= 1000) {
					AchievementMechanics.addAchievement(player, "Acquire Currency II");
					if (bank_net >= 5000) {
						AchievementMechanics.addAchievement(player, "Acquire Currency III");
						if (bank_net >= 10000) {
							AchievementMechanics.addAchievement(player, "Acquire Currency IV");
							if (bank_net >= 50000) {
								AchievementMechanics.addAchievement(player, "Acquire Currency V");
								if (bank_net >= 100000) {
									AchievementMechanics.addAchievement(player, "Acquire Currency VI");
									if (bank_net >= 500000) {
										AchievementMechanics.addAchievement(player, "Acquire Currency VII");
										if (bank_net >= 1000000) {
											AchievementMechanics.addAchievement(player, "Acquire Currency IX");
										}
									}
								}
							}
						}
					}
				}
			}
		}

		if (PetMechanics.player_pets.containsKey(player.getUniqueId())) {
			if (PetMechanics.player_pets.get(player.getUniqueId()).contains("baby_zombie")
					|| (Hive.forum_usergroup.containsKey(player.getUniqueId())
							&& Hive.forum_usergroup.get(player.getUniqueId()) == 9)) {
				AchievementMechanics.addAchievement(player, "Old Timer");
			}
			if (PetMechanics.player_pets.get(player.getUniqueId()).size() > 0) {
				AchievementMechanics.addAchievement(player, "A Companion");
			}
			if (PetMechanics.player_pets.get(player.getUniqueId()).size() >= 3) {
				AchievementMechanics.addAchievement(player, "The Tamer");
			}
		}

		if (PermissionMechanics.getRank(player.getUniqueId()).contains("sub")) {
			AchievementMechanics.addAchievement(player, "Subscriber");
		}
		if (PermissionMechanics.getRank(player.getUniqueId()).contains("sub+")) {
			AchievementMechanics.addAchievement(player, "Subscriber");
			AchievementMechanics.addAchievement(player, "Subscriber+");
		}
		if (PermissionMechanics.getRank(player.getUniqueId()).contains("sub++")) {
			AchievementMechanics.addAchievement(player, "Subscriber");
			AchievementMechanics.addAchievement(player, "Subscriber+");
			AchievementMechanics.addAchievement(player, "Lifetime Subscriber");
		}

		if (PermissionMechanics.getRank(player.getUniqueId()).contains("pmod")) {
			AchievementMechanics.addAchievement(player, "Eyes and Ears");
		}

		if (GuildMechanics.inGuild(player.getUniqueId())) {
			AchievementMechanics.addAchievement(player, "Guildmember");
		}

		if (player != null) {
			int music = player.getInventory().first(Material.JUKEBOX);
			if (music != -1) {
				ItemStack music_box = player.getInventory().getItem(music);
				if (music_box.hasItemMeta()
						&& music_box.getItemMeta().getDisplayName().contains(ChatColor.GOLD.toString())) {
					AchievementMechanics.addAchievement(player, "The Bard");
				}
			}
		}
	}

	/**
	 * Processes armor achievements
	 * 
	 * @param player
	 *            player to process achievements for
	 * @param armor
	 *            armor that the player is wearing
	 */
	public static void processArmorAchievements(Player player, ItemStack[] armor) {
		boolean t1 = true, t2 = true, t3 = true, t4 = true, t5 = true;
		for (ItemStack is : armor) {
			if (is == null || is.getType() == Material.AIR) {
				return; // Nothing.
			}
			if (ItemMechanics.getItemTier(is) != 1) {
				t1 = false;
			}
			if (ItemMechanics.getItemTier(is) != 2) {
				t2 = false;
			}
			if (ItemMechanics.getItemTier(is) != 3) {
				t3 = false;
			}
			if (ItemMechanics.getItemTier(is) != 4) {
				t4 = false;
			}
			if (ItemMechanics.getItemTier(is) != 5) {
				t5 = false;
			}
		}

		if (t1) {
			addAchievement(player, "Full T1");
		}
		if (t2) {
			addAchievement(player, "Full T2");
		}
		if (t3) {
			addAchievement(player, "Full T3");
		}
		if (t4) {
			addAchievement(player, "Full T4");
		}
		if (t5) {
			addAchievement(player, "Full T5");
		}

		if (!t1 && !t2 && !t3 && !t4 && !t5) {
			addAchievement(player, "Mix and Match");
		}

	}

}
