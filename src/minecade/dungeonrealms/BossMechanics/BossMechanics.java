package minecade.dungeonrealms.BossMechanics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import minecade.dungeonrealms.Main;
import minecade.dungeonrealms.BossMechanics.commands.CommandSpawnBoss;
import minecade.dungeonrealms.EnchantMechanics.EnchantMechanics;
import minecade.dungeonrealms.HealthMechanics.HealthMechanics;
import minecade.dungeonrealms.Hive.ParticleEffect;
import minecade.dungeonrealms.InstanceMechanics.InstanceMechanics;
import minecade.dungeonrealms.ItemMechanics.ItemGenerators;
import minecade.dungeonrealms.ItemMechanics.ItemMechanics;
import minecade.dungeonrealms.LevelMechanics.LevelMechanics;
import minecade.dungeonrealms.MoneyMechanics.MoneyMechanics;
import minecade.dungeonrealms.MonsterMechanics.MonsterMechanics;
import minecade.dungeonrealms.enums.CC;
import minecade.dungeonrealms.jsonlib.JSONMessage;

public class BossMechanics implements Listener {
	/*
	 * Bosses: UNHOLY_PRIEST
	 */
	static final Logger log = Logger.getLogger("Minecraft");

	/** Keeps the boss type mapped to an entity **/
	public static ConcurrentHashMap<Entity, String> boss_map = new ConcurrentHashMap<>();

	/**
	 * Keeps track of how many minion waves have spawned from the boss (Unholy
	 * Priest) Entity, Amount of Minion Waves spawned. 9=max, 1=90% hp left
	 **/
	public static ConcurrentHashMap<Entity, Double> minion_count = new ConcurrentHashMap<>();

	/**
	 * Keeps track of the boss's minions
	 */
	public static ConcurrentHashMap<Entity, List<Entity>> minion_map = new ConcurrentHashMap<>();

	/**
	 * Keeps track of how many times the boss has been healed
	 */
	public static ConcurrentHashMap<Entity, Integer> boss_heals = new ConcurrentHashMap<>();

	/**
	 * Saves the boss freeze location
	 */
	public static HashMap<Entity, Location> boss_saved_location = new HashMap<>();

	/**
	 * Contains events the boss has already gone through
	 */
	public static HashMap<Entity, String> boss_event_log = new HashMap<>();

	/**
	 * Contains a list of bosses that are jumping
	 */
	public static HashSet<Entity> is_jumping = new HashSet<>();

	public static HashMap<Entity, Long> last_jump = new HashMap<>();

	public static HashSet<Entity> invincible_mob = new HashSet<>();

	public static HashMap<Entity, List<Entity>> aceron_minions = new HashMap<>();

	public static List<Entity> enraged_boss = new ArrayList<>();

	/*- he should heal himself three times before dying at 20-30% HP
	- He should have an AoE move with his axe (whirlwind)
	- He should be able to summon minions everytime they are killed and he loses 10% HP.
	- He should have a powerstrike that does high knockback, 5% chance of occuring.
	- When he is going to die, i.e the last 10%, he "rages" particle effects and beings to hit a lot harder.
	 */

	public void onEnable() {

		Main.plugin.getCommand("spawnboss").setExecutor(new CommandSpawnBoss());
		Main.plugin.getServer().getPluginManager().registerEvents(new AceronListener(), Main.plugin);
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.plugin);

		// spawns enraged boss particles
		new BukkitRunnable() {

			public void run() {
				for (Entity ent : enraged_boss) {
					try {
						ParticleEffect.sendToLocation(ParticleEffect.PORTAL, ent.getLocation(),
								new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.2F, 50);
						ParticleEffect.sendToLocation(ParticleEffect.WITCH_MAGIC, ent.getLocation(),
								new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.2F, 20);
					} catch (Exception e) {
						System.err.println(CC.YELLOW + "Unable to spawn boss particles!" + CC.DEFAULT);
					}
				}
			}
		}.runTaskTimerAsynchronously(Main.plugin, 10L * 20L, 10L);

		// Aceron
		new BukkitRunnable() {

			public void run() {
				for (Entry<Entity, Long> e : last_jump.entrySet()) {
					long time = e.getValue();
					Entity ent = e.getKey();
					// So they dont jump in the air -_-
					if (time <= System.currentTimeMillis() && !invincible_mob.contains(ent)) {
						// They can jump again.
						if (!is_jumping.contains(ent)) {
							groundSlam(ent);
						}
					}
				}
			}
		}.runTaskTimerAsynchronously(Main.plugin, 10, 3 * 20);

		// checks if the boss should stay invisible or not while minions are
		// still alive
		new BukkitRunnable() {

			public void run() {
				List<Entity> remove = new ArrayList<>();
				for (Entry<Entity, List<Entity>> data : minion_map.entrySet()) {
					final Entity boss = data.getKey();
					if (!(boss_saved_location.containsKey(boss))) {
						continue;
					}
					List<Entity> minions = data.getValue();
					boolean minion_alive = false;
					for (Entity ent : minions) {
						if (ent != null && MonsterMechanics.mob_health.containsKey(ent)) {
							minion_alive = true;
							break;
						}
					}
					if (minions.isEmpty()) {
						minion_alive = false;
					}
					if (minion_alive == false) {
						LivingEntity le_boss = (LivingEntity) boss;
						boss_saved_location.remove(boss);
						le_boss.removePotionEffect(PotionEffectType.INVISIBILITY);
						remove.add(boss);
						for (Player pl : boss.getWorld().getPlayers()) {
							pl.sendMessage(ChatColor.GOLD + "" + ChatColor.UNDERLINE + "Burick The Fanatic: "
									+ ChatColor.WHITE + "Face me, pathetic creatures!");
						}
					} else {
						LivingEntity le = (LivingEntity) boss;
						if (le.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
							new BukkitRunnable() {

								public void run() {
									try {
										ParticleEffect.sendToLocation(ParticleEffect.PORTAL,
												boss.getLocation().add(0, 1, 0), new Random().nextFloat(),
												new Random().nextFloat(), new Random().nextFloat(), 0.2F, 50);
									} catch (Exception e) {
										e.printStackTrace();
									}

								}
							}.runTask(Main.plugin);

							boss.setVelocity(new Vector(0, 0.40F, 0));
						}
					}
				}

				for (Entity e : remove) {
					minion_map.remove(e);
				}
			}
		}.runTaskTimerAsynchronously(Main.plugin, 10L * 20L, 20L);

		// aceron shit i dont care about
		new BukkitRunnable() {

			public void run() {
				// This will only be for aceron
				for (final Entity boss : last_jump.keySet()) {
					if (Math.random() >= .5D) {
						int cur_hp = MonsterMechanics.getMHealth(boss);
						int max_hp = MonsterMechanics.getMaxMobHealth(boss);
						double percent_hp = (1.0f * cur_hp / max_hp) * 100;
						if ((percent_hp <= 60) && !invincible_mob.contains(boss) && !is_jumping.contains(boss)) {
							new BukkitRunnable() {

								public void run() {
									Item i = boss.getLocation().getWorld()
											.dropItemNaturally(boss.getWorld().getPlayers()
													.get(boss.getWorld().getPlayers().size() == 1 ? 0
															: new Random()
																	.nextInt(boss.getWorld().getPlayers().size() - 1))
													.getLocation(), new ItemStack(Material.EMERALD, 1));
									i.setMetadata("greedy", new FixedMetadataValue(Main.plugin, ""));
									i.setPickupDelay(3);
								}
							}.runTask(Main.plugin);
							for (Player p : boss.getWorld().getPlayers()) {
								p.sendMessage(ChatColor.GOLD + "" + ChatColor.UNDERLINE + "Aceron the Wicked:"
										+ ChatColor.WHITE + " Taste my riches!");
							}
						}
					}
				}
			}
		}.runTaskTimerAsynchronously(Main.plugin, 0, 10 * 20);

		// handles teleporting the boss also i guess
		new BukkitRunnable() {

			public void run() {
				List<Entity> remove = new ArrayList<>();
				for (Entry<Entity, List<Entity>> data : minion_map.entrySet()) {
					Entity boss = data.getKey();
					LivingEntity le = (LivingEntity) boss;
					if (le == null || le.isDead()) {
						remove.add(boss);
					} else if (le.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
						if (boss_saved_location.containsKey(boss)
								&& (boss.getLocation().distanceSquared(boss_saved_location.get(boss)) > 64)) {
							le.teleport(boss_saved_location.get(boss));
						}
					}
					List<Entity> minions = minion_map.get(boss);
					List<Entity> to_remove = new ArrayList<>();
					for (Entity minion : minions) {
						// Check for the minions
						if (minion == null || minion.isDead()) {
							to_remove.add(minion);
						}
					}
					for (Entity mtr : to_remove) {
						minions.remove(mtr);
					}
					minion_map.put(boss, minions);
				}
				for (Entity e : remove) {
					minion_map.remove(e);
				}
			}
		}.runTaskTimerAsynchronously(Main.plugin, 10L * 20L, 2L);

		// spawns fire beneath the inferno boss and chance of spawning a magma
		// cube minion
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {

			public void run() {
				for (Entity boss : boss_map.keySet()) {
					String boss_type = boss.getMetadata("boss_type").get(0).asString();
					if (boss_type.equalsIgnoreCase("fire_demon") && boss.getVehicle() == null && boss.isOnGround()) {
						// Ignite the floor beneath this beast.
						Location loc = boss.getLocation();// .add(0, 1, 0);
						if (loc.getBlock().getType() == Material.AIR) {
							loc.getBlock().setType(Material.FIRE);
							// Chance of spawning some shit.
							if (new Random().nextInt(20) == 0) {
								Entity add = MonsterMechanics.spawnTierMob(loc, EntityType.MAGMA_CUBE, 3, -1, loc,
										false, "", "Spawn of Inferno", true, 4);
								add.setFireTicks(Integer.MAX_VALUE);
							}
						}
					}
				}
			}
		}, 10 * 20L, 5L);

		log.info("[BossMechanics] has been enabled.");
	}

	public void onDisable() {
		log.info("[BossMechanics] has been disabled.");
	}

	/**
	 * Gets nearby blocks I guess
	 * 
	 * @param loc
	 *            location to get nearby blocks at
	 * @param maxradius
	 *            radius of the blocks
	 * @return list of the nearby blocks
	 */
	public static List<Block> getNearbyBlocks(Location loc, int maxradius) {
		List<Block> return_list = new ArrayList<>();
		BlockFace[] faces = { BlockFace.UP, BlockFace.NORTH, BlockFace.EAST };
		BlockFace[][] orth = { { BlockFace.NORTH, BlockFace.EAST }, { BlockFace.UP, BlockFace.EAST },
				{ BlockFace.NORTH, BlockFace.UP } };
		for (int r = 0; r <= maxradius; r++) {
			for (int s = 0; s < 6; s++) {
				BlockFace f = faces[s % 3];
				BlockFace[] o = orth[s % 3];
				if (s >= 3)
					f = f.getOppositeFace();
				if (!(loc.getBlock().getRelative(f, r) == null)) {
					Block c = loc.getBlock().getRelative(f, r);

					for (int x = -r; x <= r; x++) {
						for (int y = -r; y <= r; y++) {
							Block a = c.getRelative(o[0], x).getRelative(o[1], y);
							return_list.add(a);
						}
					}
				}
			}
		}
		return return_list;
	}

	/**
	 * Slams the ground around the boss
	 * 
	 * @param boss
	 *            the boss
	 */
	public void groundSlam(final Entity boss) {
		boss.setVelocity(new Vector(0, 1.3, 0));
		is_jumping.add(boss);
		new BukkitRunnable() {

			public void run() {
				// Died mid air?
				if (boss != null && !boss.isDead()) {
					if (boss.isOnGround()) {
						cancel();
						// 10 seconds till next one
						last_jump.put(boss, System.currentTimeMillis() + (10 * 1000));
						if (is_jumping.contains(boss)) {
							is_jumping.remove(boss);
						}
						new BukkitRunnable() {

							public void run() {
								try {
									ParticleEffect.sendToLocation(ParticleEffect.HUGE_EXPLOSION, boss.getLocation(), 0F,
											0F, 0F, .3F, 15);
								} catch (Exception e1) {
									e1.printStackTrace();
								}
								for (Entity e : boss.getNearbyEntities(15, 15, 15)) {
									if (e instanceof Player) {
										Player p = (Player) e;
										// Max 1500 atleast 500 - Seems fair
										p.damage(new Random().nextInt(1000) + 1000, boss);
									}
								}
							}
						}.runTask(Main.plugin);

					}
				}
			}
		}.runTaskTimerAsynchronously(Main.plugin, 2L, 1L);
	}

	/**
	 * Announce how many gems the boss has dropped
	 * 
	 * @param total
	 *            total amount of gems
	 * @param perPlayer
	 *            gems per player
	 * @param toPlayers
	 *            list of players to give the gems to
	 */
	public void announceGemDrop(final int total, final int perPlayer, final List<Player> toPlayers) {
		new BukkitRunnable() {

			public void run() {
				for (Player p : toPlayers) {
					if (p == null)
						continue;
					p.sendMessage(ChatColor.DARK_PURPLE + "The boss has dropped " + ChatColor.LIGHT_PURPLE.toString()
							+ ChatColor.BOLD + total + ChatColor.DARK_PURPLE + " gems.");
					p.sendMessage(ChatColor.DARK_PURPLE + "Each player receives " + ChatColor.LIGHT_PURPLE.toString()
							+ ChatColor.BOLD + perPlayer + ChatColor.DARK_PURPLE + " gems!");
				}
			}
		}.runTaskLater(Main.plugin, 5L);
	}

	/**
	 * Announce what gear the boss has dropped
	 * 
	 * @param item
	 *            the item the boss dropped
	 * @param toPlayers
	 *            what players should recieve the message
	 */
	public void announceBossDrop(final ItemStack item, final List<Player> toPlayers) {
		new BukkitRunnable() {

			public void run() {
				JSONMessage m = new JSONMessage("The boss has dropped: ", ChatColor.DARK_PURPLE);
				m.addItem(item,
						(item.getItemMeta().getDisplayName() == null) ? "SHOW" : item.getItemMeta().getDisplayName());
				for (Player p : toPlayers) {
					if (p == null)
						continue;
					m.sendToPlayer(p);
				}
			}
		}.runTaskLater(Main.plugin, 5L);
	}

	/**
	 * Handles when a boss dies
	 * 
	 * @param e
	 *            Bukkit entity death event
	 */
	@EventHandler
	public void onBossDeath(EntityDeathEvent e) {
		final Entity ent = e.getEntity();
		if (boss_map.containsKey(ent)) {

			boolean final_boss = false;
			String server_message = "";

			String boss_name = boss_map.get(ent);

			if (boss_name.equalsIgnoreCase("unholy_priest") || boss_name.equalsIgnoreCase("aceron")
					|| boss_name.equalsIgnoreCase("bandit_leader") || boss_name.equalsIgnoreCase("fire_demon")) {
				final_boss = true;
			}

			if (boss_name.equalsIgnoreCase("unholy_priest")) {
				for (Block b : getNearbyBlocks(ent.getLocation(), 10)) {
					if (b.getType() == Material.FIRE) {
						b.setType(Material.AIR);
					}
				}

				server_message = ChatColor.GOLD.toString() + ChatColor.BOLD + ">> " + ChatColor.GOLD
						+ "The corrupt Unholy Priest " + ChatColor.UNDERLINE + "Burick The Fanatic" + ChatColor.RESET
						+ ChatColor.GOLD + " has been slain by a group of adventurers!";

				try {
					ParticleEffect.sendToLocation(ParticleEffect.FIREWORKS_SPARK, ent.getLocation().add(0, 2, 0),
							new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.2F, 200);
				} catch (Exception err) {
					err.printStackTrace();
				}

				LivingEntity le = (LivingEntity) ent;
				int do_i_drop_gear = new Random().nextInt(100);
				if (do_i_drop_gear < 80) { // 80% chance!
					List<ItemStack> possible_drops = new ArrayList<>();
					for (ItemStack is : le.getEquipment().getArmorContents()) {
						if (is == null || is.getType() == Material.AIR || is.getType() == Material.SKULL
								|| is.getType() == Material.SKULL_ITEM) {
							continue;
						}

						ItemMeta im = is.getItemMeta();
						if (im.hasEnchants()) {
							for (Map.Entry<Enchantment, Integer> data : im.getEnchants().entrySet()) {
								is.removeEnchantment(data.getKey());
							}
						}

						is.removeEnchantment(Enchantment.LOOT_BONUS_MOBS);
						is.removeEnchantment(Enchantment.KNOCKBACK);
						is.removeEnchantment(EnchantMechanics.getCustomEnchant());
						is.setItemMeta(im);

						possible_drops.add(is);
					}

					ItemStack weapon = le.getEquipment().getItemInMainHand();
					ItemMeta im = weapon.getItemMeta();
					if (im.hasEnchants()) {
						for (Map.Entry<Enchantment, Integer> data : im.getEnchants().entrySet()) {
							im.removeEnchant(data.getKey());
						}
					}
					weapon.removeEnchantment(Enchantment.LOOT_BONUS_MOBS);
					weapon.removeEnchantment(Enchantment.KNOCKBACK);
					weapon.removeEnchantment(EnchantMechanics.getCustomEnchant());
					weapon.setItemMeta(im);

					possible_drops.add(weapon);

					ItemStack reward = ItemMechanics
							.makeSoulBound(possible_drops.get(new Random().nextInt(possible_drops.size())));
					Item item = ent.getWorld().dropItemNaturally(ent.getLocation(), reward);
					item.setMetadata("boss_drop", new FixedMetadataValue(Main.plugin, ""));
					announceBossDrop(reward, ent.getLocation().getWorld().getPlayers());
					InstanceMechanics.world_item.put(reward, item.getWorld());
				}

				int gem_drop = new Random().nextInt(2500 - 1000) + 1000;

				int perPlayer = Math.round(gem_drop / ent.getWorld().getPlayers().size());

				announceGemDrop(gem_drop, perPlayer, ent.getWorld().getPlayers());

				for (Player p : ent.getWorld().getPlayers()) {
					MoneyMechanics.addMoneyCert(p, perPlayer, false);
					LevelMechanics.addXP(p, 5000);
				}
			}

			else if (boss_name.equalsIgnoreCase("aceron")) {

				try {
					ParticleEffect.sendToLocation(ParticleEffect.LAVA, ent.getLocation().add(0, 2, 0),
							new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 1F, 150);
				} catch (Exception err) {
					err.printStackTrace();
				}
				ent.getWorld().playSound(ent.getLocation(), Sound.ENTITY_ENDERDRAGON_DEATH, 1, 1);
				server_message = ChatColor.GOLD.toString() + ChatColor.BOLD + ">> " + ChatColor.GOLD + "The Greed King "
						+ ChatColor.UNDERLINE + "Aceron the Wicked" + ChatColor.RESET + ChatColor.GOLD
						+ " has been slain by a group of adventurers!";

				try {
					ParticleEffect.sendToLocation(ParticleEffect.FIREWORKS_SPARK, ent.getLocation().add(0, 2, 0),
							new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.2F, 200);
				} catch (Exception err) {
					err.printStackTrace();
				}

				LivingEntity le = (LivingEntity) ent;
				int do_i_drop_gear = new Random().nextInt(100);
				if (do_i_drop_gear < 80) { // 80% chance!
					List<ItemStack> possible_drops = new ArrayList<>();
					for (ItemStack is : le.getEquipment().getArmorContents()) {
						if (is == null || is.getType() == Material.AIR || is.getType() == Material.SKULL
								|| is.getType() == Material.SKULL_ITEM) {
							continue;
						}
						ItemMeta im = is.getItemMeta();
						if (im.hasEnchants()) {
							for (Map.Entry<Enchantment, Integer> data : im.getEnchants().entrySet()) {
								is.removeEnchantment(data.getKey());
							}
						}
						is.removeEnchantment(Enchantment.LOOT_BONUS_MOBS);
						is.removeEnchantment(Enchantment.KNOCKBACK);
						is.removeEnchantment(EnchantMechanics.getCustomEnchant());
						is.setItemMeta(im);

						possible_drops.add(is);
					}

					ItemStack weapon = le.getEquipment().getItemInMainHand();
					ItemMeta im = weapon.getItemMeta();
					if (im.hasEnchants()) {
						for (Map.Entry<Enchantment, Integer> data : im.getEnchants().entrySet()) {
							im.removeEnchant(data.getKey());
						}
					}
					weapon.removeEnchantment(Enchantment.LOOT_BONUS_MOBS);
					weapon.removeEnchantment(Enchantment.KNOCKBACK);
					weapon.removeEnchantment(EnchantMechanics.getCustomEnchant());
					weapon.setItemMeta(im);

					possible_drops.add(weapon);

					ItemStack reward = ItemMechanics
							.makeSoulBound(possible_drops.get(new Random().nextInt(possible_drops.size())));

					Item item = ent.getWorld().dropItemNaturally(ent.getLocation(), reward);
					item.setMetadata("boss_drop", new FixedMetadataValue(Main.plugin, ""));
					announceBossDrop(reward, ent.getLocation().getWorld().getPlayers());
					InstanceMechanics.world_item.put(reward, item.getWorld());
				}

				int gem_drop = new Random().nextInt(2000) + 10000;

				for (Entity ents : ent.getWorld().getEntities()) {
					if (ents instanceof Player)
						continue;
					if (ents instanceof Item)
						continue;
					ents.remove();
				}
				last_jump.remove(ent);
				invincible_mob.remove(ent);

				int perPlayer = Math.round(gem_drop / ent.getWorld().getPlayers().size());

				announceGemDrop(gem_drop, perPlayer, ent.getWorld().getPlayers());

				for (Player p : ent.getWorld().getPlayers()) {
					MoneyMechanics.addMoneyCert(p, perPlayer, false);
					LevelMechanics.addXP(p, 10000);
				}
			}

			else if (boss_name.equalsIgnoreCase("fire_demon")) {

				try {
					ParticleEffect.sendToLocation(ParticleEffect.HUGE_EXPLOSION, ent.getLocation().add(0, 2, 0),
							new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 1F, 50);
				} catch (Exception err) {
					err.printStackTrace();
				}

				final_boss = false;

				for (LivingEntity le : ent.getWorld().getLivingEntities()) {
					if (le instanceof Player) {
						continue;
					}
					if (MonsterMechanics.mob_health.containsKey(le)) {
						le.damage(Integer.MAX_VALUE);
						le.remove();
					}
				}

				for (Player pl : ent.getWorld().getPlayers()) {
					pl.sendMessage(ChatColor.GOLD + "" + ChatColor.UNDERLINE + "The Infernal Abyss: " + ChatColor.WHITE
							+ "You...have... defeated me...ARGHHHH!!!!!");
					MonsterMechanics.pushAwayPlayer(ent, pl, 6.0F);
					pl.playSound(pl.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);
					pl.playSound(pl.getLocation(), Sound.ENTITY_ENDERDRAGON_DEATH, 2F, 2F);
				}

				server_message = ChatColor.GOLD.toString() + ChatColor.BOLD + ">> " + ChatColor.GOLD
						+ "The evil fire demon known as " + ChatColor.UNDERLINE + "The Infernal Abyss" + ChatColor.RESET
						+ ChatColor.GOLD + " has been slain by a group of adventurers!";
				final String f_server_message = server_message;

				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {

					public void run() {
						try {
							ParticleEffect.sendToLocation(ParticleEffect.FIREWORKS_SPARK,
									ent.getLocation().add(0, 2, 0), new Random().nextFloat(), new Random().nextFloat(),
									new Random().nextFloat(), 0.5F, 200);
						} catch (Exception err) {
							err.printStackTrace();
						}

						LivingEntity le = (LivingEntity) ent;
						int do_i_drop_gear = new Random().nextInt(100);
						if (do_i_drop_gear < 80) { // 80% chance!
							List<ItemStack> possible_drops = new ArrayList<>();
							for (ItemStack is : le.getEquipment().getArmorContents()) {
								if (is == null || is.getType() == Material.AIR || is.getType() == Material.SKULL
										|| is.getType() == Material.SKULL_ITEM) {
									continue;
								}
								ItemMeta im = is.getItemMeta();
								if (im.hasEnchants()) {
									for (Map.Entry<Enchantment, Integer> data : im.getEnchants().entrySet()) {
										is.removeEnchantment(data.getKey());
									}
								}
								is.removeEnchantment(Enchantment.LOOT_BONUS_MOBS);
								is.removeEnchantment(Enchantment.KNOCKBACK);
								im.removeEnchant(EnchantMechanics.getCustomEnchant());
								is.setItemMeta(im);

								possible_drops.add(is);
							}

							ItemStack weapon = le.getEquipment().getItemInMainHand();
							ItemMeta im = weapon.getItemMeta();
							if (im.hasEnchants()) {
								for (Map.Entry<Enchantment, Integer> data : im.getEnchants().entrySet()) {
									weapon.removeEnchantment(data.getKey());
								}
							}
							weapon.removeEnchantment(Enchantment.LOOT_BONUS_MOBS);
							weapon.removeEnchantment(Enchantment.KNOCKBACK);
							weapon.removeEnchantment(EnchantMechanics.getCustomEnchant());
							weapon.setItemMeta(im);

							possible_drops.add(weapon);

							ItemStack reward = ItemMechanics
									.makeSoulBound(possible_drops.get(new Random().nextInt(possible_drops.size())));
							Item item = ent.getWorld().dropItemNaturally(ent.getLocation(), reward);
							InstanceMechanics.world_item.put(reward, item.getWorld());
							item.setMetadata("boss_drop", new FixedMetadataValue(Main.plugin, ""));
							announceBossDrop(reward, ent.getLocation().getWorld().getPlayers());
						}

						int gem_drop = new Random().nextInt(2000) + 10000;

						int perPlayer = Math.round(gem_drop / ent.getWorld().getPlayers().size());

						announceGemDrop(gem_drop, perPlayer, ent.getWorld().getPlayers());

						for (Player p : ent.getWorld().getPlayers()) {
							MoneyMechanics.addMoneyCert(p, perPlayer, false);
						}

						String instance_name = ent.getWorld().getName();
						List<String> party_members = InstanceMechanics.instance_party.get(instance_name);
						String adventurers = "";
						for (String s : party_members) {
							adventurers += s + ", ";
						}

						if (adventurers.endsWith(", ")) {
							adventurers = adventurers.substring(0, adventurers.lastIndexOf(","));
						}

						final String f_adv = adventurers;

						if (InstanceMechanics.isInstance(instance_name)) {
							InstanceMechanics.teleport_on_complete.put(instance_name, 60);
						}

						for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
							pl.sendMessage(f_server_message);
							pl.sendMessage(ChatColor.GRAY + "Group: " + f_adv);
						}

						boss_map.remove(ent);
					}
				}, 60L);

				for (Player p : ent.getWorld().getPlayers()) {
					LevelMechanics.addXP(p, 10000);
				}
			}

			else if (boss_name.equalsIgnoreCase("bandit_leader")) {
				server_message = ChatColor.GOLD.toString() + ChatColor.BOLD + ">> " + ChatColor.GOLD
						+ "The cunning bandit lord " + ChatColor.UNDERLINE + "Mayel The Cruel" + ChatColor.RESET
						+ ChatColor.GOLD + " has been slain by a group of adventurers!";
				try {
					ParticleEffect.sendToLocation(ParticleEffect.FIREWORKS_SPARK, ent.getLocation().add(0, 2, 0),
							new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.5F, 200);
				} catch (Exception err) {
					err.printStackTrace();
				}

				LivingEntity le = (LivingEntity) ent;
				int do_i_drop_gear = new Random().nextInt(100);
				if (do_i_drop_gear < 100) { // 100% chance!
					List<ItemStack> possible_drops = new ArrayList<>();
					for (ItemStack is : le.getEquipment().getArmorContents()) {
						if (is == null || is.getType() == Material.AIR || is.getType() == Material.SKULL
								|| is.getType() == Material.SKULL_ITEM) {
							continue;
						}

						ItemMeta im = is.getItemMeta();
						if (im.hasEnchants()) {
							for (Map.Entry<Enchantment, Integer> data : im.getEnchants().entrySet()) {
								is.removeEnchantment(data.getKey());
							}
						}
						is.removeEnchantment(Enchantment.LOOT_BONUS_MOBS);
						is.removeEnchantment(Enchantment.KNOCKBACK);
						is.removeEnchantment(EnchantMechanics.getCustomEnchant());
						is.setItemMeta(im);

						possible_drops.add(is);
					}

					possible_drops.add(ItemGenerators.customGenerator("mayelhelmet"));

					ItemStack weapon = le.getEquipment().getItemInMainHand();
					ItemMeta im = weapon.getItemMeta();
					if (im.hasEnchants()) {
						for (Map.Entry<Enchantment, Integer> data : im.getEnchants().entrySet()) {
							weapon.removeEnchantment(data.getKey());
						}
					}

					weapon.removeEnchantment(Enchantment.LOOT_BONUS_MOBS);
					weapon.removeEnchantment(Enchantment.KNOCKBACK);
					weapon.removeEnchantment(EnchantMechanics.getCustomEnchant());
					weapon.setItemMeta(im);

					possible_drops.add(weapon);
					// Makes the item swaggy
					ItemStack reward = ItemMechanics
							.makeSoulBound(possible_drops.get(new Random().nextInt(possible_drops.size())));
					Item item = ent.getWorld().dropItemNaturally(ent.getLocation(), reward);
					item.setMetadata("boss_type", new FixedMetadataValue(Main.plugin, ""));
					InstanceMechanics.world_item.put(reward, item.getWorld());
					announceBossDrop(reward, ent.getLocation().getWorld().getPlayers());
				}

				int gem_drop = new Random().nextInt(250 - 100) + 100;

				int perPlayer = Math.round(gem_drop / ent.getWorld().getPlayers().size());

				announceGemDrop(gem_drop, perPlayer, ent.getWorld().getPlayers());

				for (Player p : ent.getWorld().getPlayers()) {
					MoneyMechanics.addMoneyCert(p, perPlayer, false);
					LevelMechanics.addXP(p, 1000);
				}
			}

			else if (boss_name.equalsIgnoreCase("tnt_bandit")) {
				// EXPLODE!
				for (Player pl : ent.getWorld().getPlayers()) {
					pl.sendMessage(ChatColor.GOLD + "" + ChatColor.UNDERLINE + "Mad Bandit Pyromancer: "
							+ ChatColor.WHITE + "I won't be defeated so easily! Now you all die!");
					pl.sendMessage(ChatColor.RED.toString() + ChatColor.UNDERLINE
							+ "Mad Bandit Pyromancer has booby-trapped his body with explosives! Get away!");
					// TODO old sound was a fuse
					pl.playSound(pl.getLocation(), Sound.ENTITY_CREEPER_PRIMED, 1F, 1F);
				}
				Entity tnt = ent.getWorld().spawn(ent.getLocation(), TNTPrimed.class);
				((TNTPrimed) tnt).setFuseTicks(80);
				final Location explosion = ent.getLocation();

				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {

					public void run() {
						try {
							ParticleEffect.sendToLocation(ParticleEffect.HUGE_EXPLOSION,
									ent.getLocation().add(0, 1.5, 0), new Random().nextFloat(),
									new Random().nextFloat(), new Random().nextFloat(), 1F, 200);
						} catch (Exception err) {
							err.printStackTrace();
						}

						explosion.getWorld().playSound(explosion, Sound.ENTITY_GENERIC_EXPLODE, 10F, 1F);
						for (Player pl : explosion.getWorld().getPlayers()) {
							if (pl.getLocation().distanceSquared(explosion) <= 64) {
								// Hurt them.
								// Deal 90% of health in DMG
								double max_health = HealthMechanics.getMaxHealthValue(pl.getName());
								double dmg = max_health * 0.90;
								pl.damage(dmg, ent);
							}
						}
					}
				}, 80L);

			}

			if (final_boss) {
				// This will move them all out.
				String instance_name = ent.getWorld().getName();
				List<String> party_members = InstanceMechanics.instance_party.get(instance_name);
				String adventurers = "";
				for (String s : party_members) {
					adventurers += s + ", ";
				}

				if (adventurers.endsWith(", ")) {
					adventurers = adventurers.substring(0, adventurers.lastIndexOf(","));
				}

				final String f_adv = adventurers;

				if (InstanceMechanics.isInstance(instance_name)) {
					InstanceMechanics.teleport_on_complete.put(instance_name, 60);
				}

				if (server_message.length() > 0) {
					final String f_server_message = server_message;
					new BukkitRunnable() {

						public void run() {
							for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
								pl.sendMessage(f_server_message);
								pl.sendMessage(ChatColor.GRAY + "Group: " + f_adv);
							}
						}
					}.runTaskLater(Main.plugin, 30L);
				}
			}

			boss_map.remove(ent);
		}
	}

}
