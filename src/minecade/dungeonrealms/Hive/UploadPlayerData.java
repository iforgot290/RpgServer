package minecade.dungeonrealms.Hive;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.fusesource.jansi.Ansi;

import minecade.dungeonrealms.Main;
import minecade.dungeonrealms.CommunityMechanics.CommunityMechanics;
import minecade.dungeonrealms.HealthMechanics.HealthMechanics;
import minecade.dungeonrealms.HearthstoneMechanics.HearthstoneMechanics;
import minecade.dungeonrealms.KarmaMechanics.KarmaMechanics;
import minecade.dungeonrealms.LevelMechanics.LevelMechanics;
import minecade.dungeonrealms.MoneyMechanics.MoneyMechanics;
import minecade.dungeonrealms.MountMechanics.MountMechanics;
import minecade.dungeonrealms.RealmMechanics.RealmMechanics;
import minecade.dungeonrealms.RecordMechanics.RecordMechanics;
import minecade.dungeonrealms.ShopMechanics.ShopMechanics;
import minecade.dungeonrealms.managers.PlayerManager;

public class UploadPlayerData extends Thread {

	private UUID id;
	int g_attempts = 0;
	static int g_up_attempts = 0;

	public UploadPlayerData(UUID id) {
		this.id = id;
	}

	public void run() {

		while (Hive.local_ddos || Hive.hive_ddos) { // Don't even try to upload,
													// there's no connectivity
													// mate.
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
			continue;
		}

		try {
			File data = new File(Hive.rootDir + "/" + Hive.main_world_name + "/playerdata/" + id.toString() + ".dat");
			if (!(Hive.no_upload.contains(id))) { // Don't upload data if
														// they're in the
														// no_upload list, just
														// set them offline.

				Hive.uploadPlayerDatabaseData(id);
				final int n_money = MoneyMechanics.bank_map.get(id)
						+ RealmMechanics.getMoneyInInventory(Hive.player_inventory.get(id));
				final int n_pdeaths = RecordMechanics.player_deaths.get(id);
				final int n_unlawful_kills = RecordMechanics.player_kills.get(id).get(0);
				final int n_lawful_kills = RecordMechanics.player_kills.get(id).get(1);
				final int n_lmob_kills = RecordMechanics.mob_kills.get(id);
				final int n_duel_lose = RecordMechanics.duel_statistics.get(id).get(0);
				final int n_duel_win = RecordMechanics.duel_statistics.get(id).get(1);
				LevelMechanics.getPlayerData(id).saveData(true);
				RecordMechanics.updateStatisticData(id, n_money, n_pdeaths, n_unlawful_kills, n_lawful_kills,
						n_lmob_kills, n_duel_win, n_duel_lose);

				Hive.player_inventory.remove(id);
				Hive.player_location.remove(id);
				Hive.player_hp.remove(id);
				Hive.player_level.remove(id);
				Hive.player_food_level.remove(id);
				Hive.player_armor_contents.remove(id);
				Hive.player_ecash.remove(id);
				Hive.player_ip.remove(id);
				Hive.player_portal_shards.remove(id);
				Hive.player_first_login.remove(id);

				Hive.local_player_ip.remove(id);
				Hive.remote_player_data.remove(id);
				Hive.last_sync.remove(id);
				Hive.forum_usergroup.remove(id);
				Hive.login_time.remove(id);

				MoneyMechanics.uploadBankDatabaseData(id, true);
				ShopMechanics.uploadShopDatabaseData(id, true);
				HearthstoneMechanics.saveData(id);

				if (RealmMechanics.isWorldLoaded(id.toString())) {
					RealmMechanics.uploadWorld(id); // mumoxx was
																// here
				}

				KarmaMechanics.align_map.remove(id);
				KarmaMechanics.align_time.remove(id);

				PlayerManager.getPlayerModel(id).setIgnoreList(new ArrayList<UUID>());
				PlayerManager.getPlayerModel(id).setBuddyList(new ArrayList<UUID>());

				HealthMechanics.noob_player_warning.remove(id);
				HealthMechanics.noob_players.remove(id);

				RealmMechanics.realm_title.remove(id);
				// RealmMechanics.realm_tier.remove(id);

				MountMechanics.mule_inventory.remove(id);
				MountMechanics.mule_itemlist_string.remove(id);
			} else if (Hive.no_upload.contains(id)) {
				// Just delete some local data.
				Hive.player_inventory.remove(id);
				Hive.player_location.remove(id);
				Hive.player_hp.remove(id);
				Hive.player_level.remove(id);
				Hive.player_food_level.remove(id);
				Hive.player_armor_contents.remove(id);
				Hive.player_ecash.remove(id);
				KarmaMechanics.align_map.remove(id);
				KarmaMechanics.align_time.remove(id);
				PlayerManager.getPlayerModel(id).setIgnoreList(new ArrayList<UUID>());
				PlayerManager.getPlayerModel(id).setBuddyList(new ArrayList<UUID>());
				PlayerManager.getPlayerModel(id).setToggleList(new ArrayList<String>());
				HealthMechanics.noob_player_warning.remove(id);
				HealthMechanics.noob_players.remove(id);
				RealmMechanics.realm_title.remove(id);
				// RealmMechanics.realm_tier.remove(id);
				MountMechanics.mule_inventory.remove(id);
				MountMechanics.mule_itemlist_string.remove(id);
			}

			if (Hive.server_swap.containsKey(id) && Main.plugin.getServer().getPlayer(id) != null) {
				Player pl = Main.plugin.getServer().getPlayer(id);
				String server_prefix = Hive.server_swap.get(id);

				Hive.setPlayerOffline(id, 0, true); // Instant set offline!

				List<Object> qdata = new ArrayList<Object>();
				qdata.add("@server_num@" + id.toString() + ":" + Hive.getServerNumFromPrefix(server_prefix));
				qdata.add(null);
				qdata.add(true);
				CommunityMechanics.social_query_list.put(id.toString(), qdata);

				Thread.sleep(50);
				// CommunityMechanics.sendPacketCrossServer("@server_num@" +
				// id + ":" + Hive.getServerNumFromPrefix(server_prefix),
				// -1, true);
				PlayerManager.getPlayerModel(id).setServerNum(Hive.getServerNumFromPrefix(server_prefix));
				Thread.sleep(50);

				ByteArrayOutputStream b = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(b);
				try {
					out.writeUTF("Connect");
					out.writeUTF(server_prefix);
				} catch (IOException eee) {
					Bukkit.getLogger().info("You'll never see me!");
				}

				pl.sendPluginMessage(Main.plugin, "BungeeCord", b.toByteArray());
				// CommunityMechanics.sendPacketCrossServer("@server_num@" +
				// id + ":" + -1, -1, true);
				// Tells players the user has left the old server.
			}

			if (!(Hive.server_swap.containsKey(id))) {
				data.delete();
			}
			Hive.lockout_players.remove(id);
			Hive.no_upload.remove(id);
			Hive.loaded_players.remove(id); // no longer loaded hehe.

			if (!Hive.player_to_npc.containsKey(id) && !(Hive.server_swap.containsKey(id))) { // &&
																										// !(HealthMechanics.in_combat.containsKey(id))){
				// They don't have an NPC spawned, so let them be logged out
				// ASAP
				// If they have an NPC spawned, this will be handled by the
				// async timer.
				Hive.setPlayerOffline(id, 1);
			}

			Hive.pending_upload.remove(id);
			RealmMechanics.player_god_mode.remove(id);
			Hive.log.info(Ansi.ansi().fg(Ansi.Color.YELLOW).boldOff().toString()
					+ "[HIVE (SLAVE Edition)] Player data for " + id.toString() + " uploaded (quit event)."
					+ Ansi.ansi().fg(Ansi.Color.WHITE).boldOff().toString());
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof FileNotFoundException) {
				Hive.log.info("[HIVE (SLAVE Edition)] Failed to upload player data for " + id.toString()
						+ " due to the player data not being present.");
				return; // We can't do anything, the file is long gone.
			}
			if (e instanceof NullPointerException) {
				// Some data not present, skip upload.
				Hive.log.info("[HIVE (SLAVE Edition)] Failed to upload player data for " + id.toString()
						+ " due to the player data not being present.");
				return; // We can't do anything, the data is long gone.
			}

			try {
				// Something went wrong, meaning we could have desync problem.
				// Let's lock them out from all servers and try again.
				if (!(Hive.lockout_players.contains(id))) {
					Hive.lockout_players.add(id);
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
				}
				g_attempts++;
				if (g_attempts > 5 && !Hive.local_ddos && !Hive.hive_ddos && Hive.isHiveOnline()) {
					Hive.log.info("Failed to upload data for player " + id.toString()
							+ " 5 times, and the HIVE is reported as online, SKIPPING...");
					Hive.setPlayerOffline(id, 1);
					Hive.pending_upload.remove(id);
					return;
				}
				this.run(); // Rerun the method and try again.
				return;
			} catch (Exception e2) {
				e2.printStackTrace();
				Hive.log.info("Something SERIOUSLY FUCKED up on player data upload for " + id.toString());
				return;
			}

		}
	}

}
