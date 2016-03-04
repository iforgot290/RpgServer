package minecade.dungeonrealms.CommunityMechanics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import minecade.dungeonrealms.Main;
import minecade.dungeonrealms.Utils;
import minecade.dungeonrealms.ChatMechanics.ChatMechanics;
import minecade.dungeonrealms.EcashMechanics.EcashMechanics;
import minecade.dungeonrealms.GuildMechanics.GuildMechanics;
import minecade.dungeonrealms.Hive.Hive;
import minecade.dungeonrealms.InstanceMechanics.InstanceMechanics;
import minecade.dungeonrealms.MoneyMechanics.MoneyMechanics;
import minecade.dungeonrealms.MonsterMechanics.MonsterMechanics;
import minecade.dungeonrealms.PermissionMechanics.PermissionMechanics;
import minecade.dungeonrealms.PetMechanics.PetMechanics;
import minecade.dungeonrealms.ProfessionMechanics.ProfessionMechanics;
import minecade.dungeonrealms.ShopMechanics.ShopMechanics;
import minecade.dungeonrealms.config.Config;
import minecade.dungeonrealms.enums.LogType;
import minecade.dungeonrealms.jsonlib.JsonBuilder;
import minecade.dungeonrealms.managers.PlayerManager;
import minecade.dungeonrealms.models.LogModel;


public class ConnectProtocol implements Runnable {
	private Socket clientSocket;
	private String sendingIP;

	public ConnectProtocol(Socket s, String ip) {
		this.clientSocket = s;
		this.sendingIP = ip;
	}

	public static void sendResultCrossServer(String server_ip, String message, int server_num) {
		Socket kkSocket = null;
		PrintWriter out = null;

		try {
			try {
				kkSocket = CommunityMechanics.getSocket(server_num);
				out = new PrintWriter(kkSocket.getOutputStream(), true);
			} catch (Exception err) {
				String ipAndPort = server_ip;
				String ipNoPort = ipAndPort.contains(":") ? server_ip.split(":")[0] : ipAndPort;
				int port = ipAndPort.contains(":") && StringUtils.isNumeric(ipAndPort.split(":")[1]) ? Integer.parseInt(ipAndPort
						.split(":")[1]) : Config.transfer_port;
				kkSocket = new Socket();
				kkSocket.connect(new InetSocketAddress(ipNoPort, port), 150);
				out = new PrintWriter(kkSocket.getOutputStream(), true);
			}

			out.println(message);
			out.close();
		} catch (IOException e) {

		} finally {
			if (out != null) {
				out.close();
			}
		}
	}


	public void run() {

		try {
			// PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
			// true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String inputLine;
			List<String> receivedData = new ArrayList<String>();

			while ((inputLine = in.readLine()) != null) {
				receivedData.add(inputLine);

				if (inputLine.startsWith("@date_update@")) {
					long time = Long.parseLong(inputLine.substring(inputLine.lastIndexOf("@") + 1, inputLine.length()));
					Runtime.getRuntime().exec("date -s @" + time);
				}

				else if (inputLine.startsWith("@instance_time@")) {
					// @instance_time@instance_template:seconds_they_took
					String instance_template = inputLine.substring(inputLine.lastIndexOf("@") + 1, inputLine.indexOf(":"));
					double seconds = Double.parseDouble(inputLine.substring(inputLine.indexOf(":") + 1, inputLine.length()));
					if (InstanceMechanics.total_timing.containsKey(instance_template)) {
						List<Integer> previous_times = InstanceMechanics.total_timing.get(instance_template);
						previous_times.add((int) seconds);
						InstanceMechanics.total_timing.put(instance_template, previous_times);
					} else {
						List<Integer> previous_times = new ArrayList<Integer>();
						previous_times.add((int) seconds);
						InstanceMechanics.total_timing.put(instance_template, previous_times);
					}
					return;
				}

				// @level100@p_name:
				else if (inputLine.startsWith("@level100@")) {
					String p_name = inputLine.split("100@")[1].split(":")[0];
					Bukkit.broadcastMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + p_name + ChatColor.WHITE + " has reached level 100!");
					return;
				}

				// @population@US-1:online_players/max_players
				else if (inputLine.startsWith("@population@")) {

					final String server_prefix = inputLine.substring(inputLine.lastIndexOf("@") + 1, inputLine.indexOf(":"));
					final int online_players = Integer.parseInt(inputLine.substring(inputLine.indexOf(":") + 1, inputLine.indexOf("/")));
					final int max_players = Integer.parseInt(inputLine.substring(inputLine.indexOf("/") + 1, inputLine.length()));
					final int server_num = Hive.getServerNumFromPrefix(server_prefix);

					if (online_players > 0 && Hive.offline_servers.contains(server_prefix)) {
						Hive.offline_servers.remove(server_prefix);
					}
					new BukkitRunnable() {

						public void run() {
							Hive.last_ping.put(server_num, System.currentTimeMillis());
							Hive.server_population.put(server_num, new ArrayList<Integer>(Arrays.asList(online_players, max_players)));
						}
					}.runTask(Main.plugin);
					return;
				}

				// @rollout@
				else if (inputLine.equals("@rollout@")) {
					Hive.startShutdown(15, true);
					return;
				}

				// @restart@[:seconds] (@restart@:30) default to 60
				else if (inputLine.startsWith("@restart@")) {

					int seconds = 60;
					if (inputLine.contains(":")) seconds = Integer.parseInt(inputLine.split(":")[1]);
					Hive.startShutdown(seconds, false);

					return;
				}

				// {LOCK}
				else if (inputLine.startsWith("{LOCK}")) {
					Hive.server_lock = true;
					Hive.force_kick = true;
					return;
				}

				// {UNLOCK}
				else if (inputLine.startsWith("{UNLOCK}")) {
					Hive.server_lock = false;
					Hive.force_kick = false;
					return;
				}

				// !!!message
				else if (inputLine.startsWith("!!!")) {
					String msg = inputLine.substring(3, inputLine.length());
					Bukkit.getServer().broadcastMessage(ChatColor.AQUA + "" + ChatColor.BOLD + ">> " + ChatColor.AQUA + msg);
					return;
				}

				// @ban@permanent/uuid[:reason] permanent = 1
				else if (inputLine.startsWith("@ban@")) {
					inputLine = inputLine.replaceAll("@ban@", "");

					int perm = Integer.parseInt(inputLine.split("/")[0]);
					UUID p_uuid = null;
					String reason = "N/A";

					if (inputLine.contains(":")){
						String[] args = inputLine.split("/")[1].split(":");
						p_uuid = UUID.fromString(args[0]);
						reason = args[1];
					} else {
						p_uuid = UUID.fromString(inputLine.split("/")[1]);
					}

					if (Bukkit.getPlayer(p_uuid) != null) {
						String first_line = ChatColor.RED + "Your account has been " + ChatColor.UNDERLINE + "TEMPORARILY"
								+ ChatColor.RED + " locked due to suspicious activity.";

						if (perm == 1) first_line.replace("TEMPORARILY", "PERMANENTLY");

						Hive.to_kick.put(
								p_uuid,
								first_line + "\n" + ChatColor.RED + "Reason: " + ChatColor.GRAY + reason + "\n\n"
										+ ChatColor.GRAY.toString() + "For further information about this suspension, please visit "
										+ ChatColor.UNDERLINE.toString() + "http://www.dungeonrealms.net/bans");
					}
				}

				// @kick@uuid:reason
				else if (inputLine.startsWith("@kick@")) {
					inputLine = inputLine.replaceAll("@kick@", "");
					UUID id = UUID.fromString(inputLine.split(":")[0]);
					String reason = inputLine.split(":")[1];

					if (Bukkit.getPlayer(id) != null) {
						Hive.to_kick.put(id, ChatColor.RED + "You have been disconnected from the game server." + "\n\n" + ChatColor.RED + "Reason: "
								+ ChatColor.GRAY + reason);
					}
				}

				// @mute@uuid:unmute_time (time is in minutes)
				else if (inputLine.startsWith("@mute@")) {
					inputLine = inputLine.replaceAll("@mute@", "");
					UUID id = UUID.fromString(inputLine.split(":")[0]);
					long unmute_time = Long.parseLong(inputLine.split(":")[1]);

					ChatMechanics.mute_list.put(id, unmute_time);
					if (Bukkit.getPlayer(id) != null) {
						Player muted = Bukkit.getPlayer(id);
						muted.sendMessage("");
						muted.sendMessage(ChatColor.RED + "You have been " + ChatColor.BOLD + "GLOBALLY MUTED" + ChatColor.RED
								+ " for " + unmute_time + " minute(s).");
						muted.sendMessage("");
					}
				}

				// @unmute@uuid
				else if (inputLine.startsWith("@unmute@")) {
					inputLine = inputLine.replaceAll("@unmute@", "");
					UUID id = UUID.fromString(inputLine);

					ChatMechanics.mute_list.remove(id);

					if (Bukkit.getPlayer(id) != null) {
						Player muted = Bukkit.getPlayer(id);
						muted.sendMessage("");
						muted.sendMessage(ChatColor.GREEN + "Your " + ChatColor.BOLD + "GLOBAL MUTE" + ChatColor.GREEN + " has been removed.");
						muted.sendMessage("");
					}
				}

				// @collection_bin@p_name&DATA
				else if (inputLine.startsWith("@collection_bin@")) {
					// We've just recieved new collection bin data for a certain user!
					inputLine = inputLine.replace("@collection_bin@", "");
					UUID id = UUID.fromString(inputLine.substring(0, inputLine.indexOf("&")));

					if (Bukkit.getPlayer(id) == null && !(Hive.pending_upload.contains(id))) {
						in.close();
						return; // Do nothing, they're not online.
					}

					String collection_bin_s = inputLine.substring(inputLine.indexOf("&"), inputLine.length());
					if (collection_bin_s != null && collection_bin_s.contains("@item@")) {
						Inventory collection_bin_inv = Hive.convertStringToInventory(null, collection_bin_s, "Collection Bin", 54);
						ShopMechanics.collection_bin.put(id, collection_bin_inv);
						CommunityMechanics.log.info("[ShopMechanics] Downloaded new collection bin data for user: " + id);
					}
				}

				// @money@owner_uuid,price.amount:buyer_name#itemname#
				else if (inputLine.startsWith("@money@")) {
					String uuid_raw = inputLine.substring(inputLine.lastIndexOf("@") + 1, inputLine.lastIndexOf(","));
					UUID owner_id = UUID.fromString(uuid_raw);
					int total_price = Integer.parseInt(inputLine.substring(inputLine.lastIndexOf(",") + 1, inputLine.indexOf(".")));
					int amount = Integer.parseInt(inputLine.substring(inputLine.indexOf(".") + 1, inputLine.indexOf(":")));
					String buyer = "Unknown";
					String i_name = "???";
					try {
						buyer = inputLine.substring(inputLine.indexOf(":") + 1, inputLine.indexOf("#"));
						i_name = inputLine.substring(inputLine.indexOf("#") + 1, inputLine.lastIndexOf("#"));
					} catch (StringIndexOutOfBoundsException SIOOBE) {
						// Do nothing, log error.
						SIOOBE.printStackTrace();
					}

					if (Bukkit.getPlayer(owner_id) != null) {
						int old_net = MoneyMechanics.bank_map.get(owner_id);
						int new_net = old_net + total_price;
						MoneyMechanics.bank_map.put(owner_id, new_net);

						Player owner = Bukkit.getPlayer(owner_id);
						owner.sendMessage(ChatColor.GREEN + "SOLD " + amount + "x '" + ChatColor.WHITE + i_name + ChatColor.GREEN + "' for "
								+ ChatColor.BOLD + total_price + "g" + ChatColor.GREEN + " to " + ChatColor.WHITE + "" + ChatColor.BOLD + buyer);
					}
				}

				// [toggleshard]
				else if (inputLine.startsWith("[toggleshard]")) {
					Hive.no_shard = !Hive.no_shard;
				}

				// [crash]server -- server formatted like: US-1
				else if (inputLine.startsWith("[crash]")) {
					String crashed_server = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.length());
					if (!(Hive.offline_servers.contains(crashed_server))) {
						Hive.offline_servers.add(crashed_server);
					}
					System.out.println(">> Server " + crashed_server + " has reported as CRASHED!!");
				}

				// [online]server -- server formatted like: US-1
				else if (inputLine.startsWith("[online]")) {
					String online_server = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.length());
					if (Hive.offline_servers.contains(online_server)) {
						Hive.offline_servers.remove(online_server);
					}
					System.out.println(">> Server " + online_server + " has reported as ONLINE.");
				}

				// [professionbuff]p_name@from_server
				else if (inputLine.startsWith("[professionbuff]")) {
					String player_string = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.indexOf("@"));
					String from_server = inputLine.substring(inputLine.indexOf("@") + 1, inputLine.length());

					Bukkit.getServer().broadcastMessage("");
					Bukkit.getServer().broadcastMessage(
							ChatColor.GOLD + "" + ChatColor.BOLD + ">> " + "(" + from_server + ") " + ChatColor.RESET + player_string + ChatColor.GOLD
							+ " has just activated " + ChatColor.UNDERLINE + "+20% Global Mining / Fishing EXP Rates" + ChatColor.GOLD
							+ " for 30 minutes by using 'Global Profession Buff' from the E-CASH store!");
					Bukkit.getServer().broadcastMessage("");

					ProfessionMechanics.profession_buff = true;
					ProfessionMechanics.profession_buff_timeout = System.currentTimeMillis() + (1800 * 1000); // 30 minutes
				}

				// [lootbuff]p_name@from_server
				else if (inputLine.startsWith("[lootbuff]")) {
					String player_string = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.indexOf("@"));
					String from_server = inputLine.substring(inputLine.indexOf("@") + 1, inputLine.length());

					Bukkit.getServer().broadcastMessage("");
					Bukkit.getServer().broadcastMessage(
							ChatColor.GOLD + "" + ChatColor.BOLD + ">> " + "(" + from_server + ") " + ChatColor.RESET + player_string + ChatColor.GOLD
							+ " has just activated " + ChatColor.UNDERLINE + "+20% Global Drop Rates" + ChatColor.GOLD
							+ " for 30 minutes by using 'Global Loot Buff' from the E-CASH store!");
					Bukkit.getServer().broadcastMessage("");

					MonsterMechanics.loot_buff_timeout = System.currentTimeMillis() + (1800 * 1000); // 30
					// minutes.
					MonsterMechanics.loot_buff = true;
				}

				// [globalmessage]player_string@from_server:msg
				// ecash global message
				else if (inputLine.startsWith("[globalmessage]")) {
					String player_string = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.indexOf("@"));
					String from_server = inputLine.substring(inputLine.indexOf("@") + 1, inputLine.indexOf(":"));
					String msg = inputLine.substring(inputLine.indexOf(":") + 1, inputLine.length());

					EcashMechanics.sendGlobalMessage(msg, from_server, player_string);
				}

				//[addpet]p_uuid:new_pet
				else if (inputLine.startsWith("[addpet]")) {
					String uuid_raw = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.indexOf(":"));
					UUID id = UUID.fromString(uuid_raw);
					String new_pet_type = inputLine.substring(inputLine.indexOf(":") + 1, inputLine.length());

					if (Hive.player_inventory.containsKey(id)) { // They're
						// online!

						List<String> pets;
						if (PetMechanics.player_pets.containsKey(id)) {
							pets = PetMechanics.player_pets.get(id);
						} else {
							pets = new ArrayList<String>();
						}

						pets.add(new_pet_type);
						PetMechanics.player_pets.put(id, pets);

						Player pl = Bukkit.getServer().getPlayer(id);
						if (pl.getInventory().firstEmpty() != -1) {

							EntityType pettype = null;
							String strmeta = "";

							// Add egg.
							if (new_pet_type.equalsIgnoreCase("baby_zombie")) pettype = EntityType.ZOMBIE;
							else if (new_pet_type.equalsIgnoreCase("baby_mooshroom")) pettype = EntityType.MUSHROOM_COW;
							else if (new_pet_type.equalsIgnoreCase("baby_cat")) pettype = EntityType.OCELOT;

							else if (new_pet_type.equalsIgnoreCase("lucky_baby_sheep")) {
								pettype = EntityType.SHEEP;
								strmeta = "green";
							}

							else if (new_pet_type.equalsIgnoreCase("easter_chicken")) pettype = EntityType.CHICKEN;
							else if (new_pet_type.equalsIgnoreCase("april_creeper")) pettype = EntityType.CREEPER;
							else if (new_pet_type.equalsIgnoreCase("beta_slime")) pettype = EntityType.SLIME;

							else if (new_pet_type.equalsIgnoreCase("july_creeper")) {
								pettype = EntityType.CREEPER;
								strmeta = "july";
							}

							else if (new_pet_type.equalsIgnoreCase("baby_horse")) pettype = EntityType.HORSE;

							if (pettype == null){
								CommunityMechanics.log.warning("Weird pet type recieved from other server: "+new_pet_type);
								in.close();
								return;
							}

							if (!(PetMechanics.containsPet(pl.getInventory(), new_pet_type))
									&& !(PetMechanics.containsPet(MoneyMechanics.bank_contents.get(pl.getName()), new_pet_type))) {
								pl.getInventory().addItem(PetMechanics.generatePetEgg(pettype, strmeta));
							}
						}
					}
				}

				// [ecash]uuid:new_net
				else if (inputLine.startsWith("[ecash]")) {
					String raw_uuid = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.indexOf(":"));
					UUID id = UUID.fromString(raw_uuid);
					int ecash = Integer.parseInt(inputLine.substring(inputLine.indexOf(":") + 1, inputLine.length()));

					if (Hive.player_inventory.containsKey(id)) { // They're
						// online!
						int amount = ecash;
						if (Hive.player_ecash.containsKey(id)) {
							amount = ecash - Hive.player_ecash.get(id);
						}
						Hive.player_ecash.put(id, ecash);
						if (Bukkit.getPlayer(id) != null) {
							Player pl = Bukkit.getPlayer(id);
							pl.sendMessage(ChatColor.GOLD + "  +" + amount + ChatColor.BOLD + " E-CASH");
							pl.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 1F);
						}
					}
				}

				// [forum_group]uuid:rank
				else if (inputLine.startsWith("[forum_group]")) {
					// Locally save a player's forum group
					String raw_id = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.indexOf(":"));
					UUID id = UUID.fromString(raw_id);
					int rank = Integer.parseInt(inputLine.substring(inputLine.indexOf(":") + 1, inputLine.length()));

					Hive.forum_usergroup.put(id, rank);
				}

				// [rank_map]uuid:rank
				else if (inputLine.startsWith("[rank_map]")) {
					// Locally save a player's rank.
					String raw_id = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.indexOf(":"));
					UUID id = UUID.fromString(raw_id);
					String rank = inputLine.substring(inputLine.indexOf(":") + 1, inputLine.length());

					PermissionMechanics.rank_map.put(id, rank);
				}

				// @server_num@uuid:server_num
				else if (inputLine.startsWith("@server_num@")) {
					// New server number data for a player.
					String raw_id = inputLine.substring(inputLine.lastIndexOf("@") + 1, inputLine.indexOf(":"));
					UUID id = UUID.fromString(raw_id);
					int server_num = Integer.parseInt(inputLine.substring(inputLine.indexOf(":") + 1, inputLine.length()));

					PlayerManager.getPlayerModel(id).setServerNum(server_num);
				}

				// [sq_online]uuid:server_name
				else if (inputLine.startsWith("[sq_online]")) {
					// Send login data to all buddies.
					String raw_id = inputLine.substring(inputLine.lastIndexOf("]") + 1, inputLine.indexOf(":"));
					UUID id = UUID.fromString(raw_id);
					String server_name = inputLine.substring(inputLine.indexOf(":") + 1, inputLine.length());
					OfflinePlayer op = Bukkit.getServer().getOfflinePlayer(id);

					if (op.isOp()) {
						in.close();
						return;
					}

					for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
						if (PlayerManager.getPlayerModel(pl).getBuddyList() != null) {
							List<UUID> lbuddy_list = PlayerManager.getPlayerModel(pl).getBuddyList();
							if (lbuddy_list.contains(id)) {
								// Tell them! and update book!

								pl.sendMessage(ChatColor.YELLOW + op.getName() + " has joined " + server_name + ".");
								pl.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2F, 1.2F);
								CommunityMechanics.updateCommBook(pl);
							}
						}
					}
				}

				// [sq_offline]uuid:server_name
				else if (inputLine.startsWith("[sq_offline]")) {
					// Send login data to all buddies.
					String raw_id = inputLine.substring(inputLine.lastIndexOf("]") + 1, inputLine.indexOf(":"));
					UUID id = UUID.fromString(raw_id);
					String server_name = inputLine.substring(inputLine.indexOf(":") + 1, inputLine.length());
					OfflinePlayer op = Bukkit.getServer().getOfflinePlayer(id);

					if (op.isOp()) {
						in.close();
						return;
					}

					for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
						if (PlayerManager.getPlayerModel(pl).getBuddyList() != null) {
							List<UUID> lbuddy_list = PlayerManager.getPlayerModel(pl).getBuddyList();
							if (lbuddy_list.contains(id)) {
								// Tell them! and update book!
								pl.sendMessage(ChatColor.YELLOW + op.getName() + " has logged out of " + server_name + ".");
								pl.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2F, 0.5F);
							}
						}
					}
				}

				// START GUILDS

				// [gdisband]g_name
				else if (inputLine.startsWith("[gdisband]")) {

					String g_name = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.length());

					if (!(GuildMechanics.guild_map.containsKey(g_name))) {
						in.close();
						return; // Nothing to do here.
					}

					for (UUID id : GuildMechanics.getGuildMembers(g_name)) {
						if (Bukkit.getPlayer(id) != null) {
							Player pl = Bukkit.getPlayer(id);
							pl.sendMessage("");
							pl.sendMessage(ChatColor.RED + "Your guild, '" + ChatColor.DARK_AQUA + g_name + ChatColor.RED
									+ "', has been disbanded by your leader.");
						}
						GuildMechanics.leaveGuild(id);
					}

					GuildMechanics.guild_map.remove(g_name);
					GuildMechanics.guild_handle_map.remove(g_name);
					GuildMechanics.guild_colors.remove(g_name);
					GuildMechanics.guild_server.remove(g_name);
					GuildMechanics.guild_motd.remove(g_name);
				}

				// [gbio]g_name$bio$
				else if (inputLine.startsWith("[gbio]")) {
					String g_name = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.indexOf("$"));
					String bio = inputLine.substring(inputLine.indexOf("$") + 1, inputLine.lastIndexOf("$"));

					if (!(GuildMechanics.guild_map.containsKey(g_name))) {
						in.close();
						return; // Nothing to do here.
					}

					GuildMechanics.setLocalGuildBIO(g_name, bio);
				}

				// [gmotd]g_name$motd$
				else if (inputLine.startsWith("[gmotd]")) {
					String g_name = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.indexOf("$"));
					String motd = inputLine.substring(inputLine.indexOf("$") + 1, inputLine.lastIndexOf("$"));

					if (!(GuildMechanics.guild_map.containsKey(g_name))) {
						in.close();
						return; // Nothing to do here.
					}

					GuildMechanics.setLocalGuildMOTD(g_name, motd);
				}

				// [gadd]p_uuid,g_name:p_inviter
				else if (inputLine.startsWith("[gadd]")) {
					String raw_id = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.indexOf(","));
					UUID id = UUID.fromString(raw_id);
					String g_name = inputLine.substring(inputLine.indexOf(",") + 1, inputLine.indexOf(":"));
					String p_inviter = inputLine.substring(inputLine.indexOf(":") + 1, inputLine.length());

					if (!(GuildMechanics.guild_map.containsKey(g_name))) {
						in.close();
						return; // Nothing to do here.
					}

					GuildMechanics.addPlayerToGuild(id, g_name);
					OfflinePlayer op = Bukkit.getOfflinePlayer(id);

					for (UUID s : GuildMechanics.getGuildMembers(g_name)) {
						if (Bukkit.getPlayer(s) != null) {
							Player pty_mem = Bukkit.getPlayer(s);
							pty_mem.sendMessage(ChatColor.DARK_AQUA + "<" + ChatColor.BOLD + GuildMechanics.guild_handle_map.get(g_name) + ChatColor.DARK_AQUA
									+ "> " + ChatColor.DARK_AQUA.toString() + op.getName() + ChatColor.GRAY.toString() + " has " + ChatColor.UNDERLINE + "joined"
									+ ChatColor.GRAY + " your guild. [INVITE: " + ChatColor.ITALIC + p_inviter + ChatColor.GRAY + "]");
						}
					}

				}

				// [gdemote]p_uuid,g_name:rank
				else if (inputLine.startsWith("[gdemote]")) {
					String raw_id = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.indexOf(","));
					UUID id = UUID.fromString(raw_id);
					OfflinePlayer op = Bukkit.getOfflinePlayer(id);
					String g_name = inputLine.substring(inputLine.indexOf(",") + 1, inputLine.indexOf(":"));
					int rank = Integer.parseInt(inputLine.substring(inputLine.indexOf(":") + 1, inputLine.indexOf("*")));

					if (!(GuildMechanics.guild_map.containsKey(g_name))) {
						in.close();
						return; // Nothing to do here.
					}

					if (rank == 1) { // rank == 1 for demotion to member from officer
						GuildMechanics.setGuildRank(id, rank); 

						// Tell the world!
						if (Bukkit.getPlayer(id) != null) {
							Player demoted = Bukkit.getPlayer(id);
							demoted.sendMessage(ChatColor.RED + "You have been " + ChatColor.UNDERLINE + "demoted" + ChatColor.RED + " to the rank of "
									+ ChatColor.BOLD + "GUILD MEMBER" + ChatColor.RED + " in " + g_name);
						}

						for (Player s : GuildMechanics.getOnlineGuildMembers(g_name)) {
							s.sendMessage(ChatColor.DARK_AQUA.toString() + "<" + ChatColor.BOLD + GuildMechanics.guild_handle_map.get(g_name)
							+ ChatColor.DARK_AQUA + ">" + ChatColor.RED + " " + op.getName() + " has been " + ChatColor.UNDERLINE + "demoted" + ChatColor.RED
							+ " to the rank of " + ChatColor.BOLD + "GUILD MEMBER.");
						}
					} else if (rank == 2) {
						GuildMechanics.setGuildRank(id, rank);
						// Tell the world!
						if (Bukkit.getPlayer(id) != null) {
							Player demoted = Bukkit.getPlayer(id);
							demoted.sendMessage(ChatColor.RED + "You have been " + ChatColor.UNDERLINE + "demoted" + ChatColor.RED + " to the rank of "
									+ ChatColor.BOLD + "GUILD OFFICER" + ChatColor.RED + " in " + g_name);
						}

						for (Player s : GuildMechanics.getOnlineGuildMembers(g_name)) {
							s.sendMessage(ChatColor.DARK_AQUA.toString() + "<" + ChatColor.BOLD + GuildMechanics.guild_handle_map.get(g_name)
							+ ChatColor.DARK_AQUA + ">" + ChatColor.RED + " " + op.getName() + " has been " + ChatColor.UNDERLINE + "demoted" + ChatColor.RED
							+ " to the rank of " + ChatColor.BOLD + "GUILD OFFICER.");
						}
					}
				}

				// [gpromote]p_uuid,g_name:rank*[sender]
				else if (inputLine.startsWith("[gpromote]")) {
					String raw_id = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.indexOf(","));
					UUID id = UUID.fromString(raw_id);
					OfflinePlayer op = Bukkit.getOfflinePlayer(id);
					String g_name = inputLine.substring(inputLine.indexOf(",") + 1, inputLine.indexOf(":"));
					int rank = Integer.parseInt(inputLine.substring(inputLine.indexOf(":") + 1, inputLine.indexOf("*")));
					String o_name = !inputLine.substring(inputLine.indexOf("*") + 1).equals("") ? inputLine.substring(inputLine.indexOf("*") + 1, inputLine.length()) : ""; // Only used by promote to owner

					if (!(GuildMechanics.guild_map.containsKey(g_name))) {
						in.close();
						return; // Nothing to do here.
					}

					if (rank == 2) {
						GuildMechanics.downloadGuildDataSQL(g_name, false);
						if (Bukkit.getPlayer(id) != null) {
							Player promoted = Bukkit.getPlayer(id);
							promoted.sendMessage(ChatColor.DARK_AQUA + "You have been " + ChatColor.UNDERLINE + "promoted" + ChatColor.DARK_AQUA
									+ " to the rank of " + ChatColor.BOLD + "GUILD OFFICER" + ChatColor.DARK_AQUA + " in " + GuildMechanics.getGuild(id));
						}

						for (Player s : GuildMechanics.getOnlineGuildMembers(g_name)) {
							s.sendMessage(ChatColor.DARK_AQUA.toString() + "<" + ChatColor.BOLD + GuildMechanics.guild_handle_map.get(g_name)
							+ ChatColor.DARK_AQUA + ">" + ChatColor.GREEN + " " + op.getName() + " has been " + ChatColor.UNDERLINE + "promoted"
							+ ChatColor.GREEN + " to the rank of " + ChatColor.BOLD + "GUILD OFFICER.");
						}
					} else if (rank == 3) {
						GuildMechanics.downloadGuildDataSQL(g_name, false);
						if (Bukkit.getPlayer(id) != null) {
							Player promoted = Bukkit.getPlayer(id);
							promoted.sendMessage(ChatColor.DARK_AQUA + "You have been " + ChatColor.UNDERLINE + "promoted" + ChatColor.DARK_AQUA
									+ " to the rank of " + ChatColor.BOLD + "GUILD CO-OWNER" + ChatColor.DARK_AQUA + " in " + GuildMechanics.getGuild(id));
						}

						for (Player s : GuildMechanics.getOnlineGuildMembers(g_name)) {
							s.sendMessage(ChatColor.DARK_AQUA.toString() + "<" + ChatColor.BOLD + GuildMechanics.guild_handle_map.get(g_name)
							+ ChatColor.DARK_AQUA + ">" + ChatColor.GREEN + " " + op.getName() + " has been " + ChatColor.UNDERLINE + "promoted"
							+ ChatColor.GREEN + " to the rank of " + ChatColor.BOLD + "GUILD CO-OWNER.");
						}
					} else if (rank == 4) {
						if (o_name == "") { in.close(); return; } // Didn't receive sender name
						GuildMechanics.downloadGuildDataSQL(g_name, false);
						if (Bukkit.getPlayer(id) != null) {
							Player promoted = Bukkit.getPlayer(id);
							promoted.sendMessage(ChatColor.GRAY + "You have been promoted to " + ChatColor.AQUA  + "" + ChatColor.BOLD + "GUILD LEADER" + ChatColor.GRAY + " of " + ChatColor.AQUA + "" + ChatColor.UNDERLINE + GuildMechanics.getGuild(id));
							GuildMechanics.updateGuildTabList(promoted);
						}

						for (Player s : GuildMechanics.getOnlineGuildMembers(g_name)) {
							GuildMechanics.updateGuildTabList(s);
							s.sendMessage(ChatColor.DARK_AQUA.toString() + "<" + ChatColor.BOLD + GuildMechanics.guild_handle_map.get(g_name)
							+ ChatColor.DARK_AQUA + ">" + ChatColor.AQUA + " " + ChatColor.UNDERLINE + o_name + ChatColor.GRAY + " has set " + ChatColor.AQUA  + "" + ChatColor.UNDERLINE + op.getName() + ChatColor.GRAY + " as the " + ChatColor.BOLD + "LEADER" + ChatColor.GRAY + " of your guild.");
						}
					}
				}

				// [gquit]player_uuid,guild_name
				else if (inputLine.startsWith("[gquit]")) {
					String raw_id = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.indexOf(","));
					UUID id = UUID.fromString(raw_id);
					OfflinePlayer op = Bukkit.getOfflinePlayer(id);
					String g_name = inputLine.substring(inputLine.indexOf(",") + 1, inputLine.length());

					if (!(GuildMechanics.guild_map.containsKey(g_name))) {
						in.close();
						return; // Nothing to do here.
					}

					GuildMechanics.leaveGuild(id, g_name, true);
					// Leave guild on every server so there's no data
					// inconsistancies. FTW.

					for (Player s : GuildMechanics.getOnlineGuildMembers(g_name)) {
						s.sendMessage(ChatColor.DARK_AQUA + "<" + ChatColor.BOLD + GuildMechanics.guild_handle_map.get(g_name) + ChatColor.DARK_AQUA
								+ "> " + ChatColor.DARK_AQUA + op.getName() + ChatColor.GRAY + " has " + ChatColor.UNDERLINE + "left" + ChatColor.GRAY
								+ " the guild.");
					}
				}

				// [gkick]p_name,g_name:p_kicker
				else if (inputLine.startsWith("[gkick]")) {
					String raw_id = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.indexOf(","));
					UUID id = UUID.fromString(raw_id);
					OfflinePlayer op = Bukkit.getOfflinePlayer(id);
					String g_name = inputLine.substring(inputLine.indexOf(",") + 1, inputLine.indexOf(":"));
					String p_kicker = inputLine.substring(inputLine.indexOf(":") + 1, inputLine.length());

					if (!(GuildMechanics.guild_map.containsKey(g_name))) {
						in.close();
						return; // Nothing to do here.
					}

					GuildMechanics.leaveGuild(id, g_name, true);
					// Leave guild on every server so there's no data
					// inconsistancies. FTW.

					for (Player s : GuildMechanics.getOnlineGuildMembers(g_name)) {
							s.sendMessage(ChatColor.DARK_AQUA + "<" + ChatColor.BOLD + GuildMechanics.guild_handle_map.get(g_name) + ChatColor.DARK_AQUA
									+ "> " + ChatColor.DARK_AQUA + op.getName() + " has been " + ChatColor.UNDERLINE + "kicked" + ChatColor.DARK_AQUA + " by "
									+ p_kicker + ".");
					}
				}

				// [quit]player_uuid,guild_name@server_name
				else if (inputLine.startsWith("[quit]")) {
					String raw_id = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.indexOf(","));
					UUID id = UUID.fromString(raw_id);
					OfflinePlayer op = Bukkit.getOfflinePlayer(id);
					String g_name = inputLine.substring(inputLine.indexOf(",") + 1, inputLine.indexOf("@"));
					String server_name = inputLine.substring(inputLine.indexOf("@") + 1, inputLine.length());

					GuildMechanics.guild_member_server.remove(id);

					if (!(GuildMechanics.guild_map.containsKey(g_name))) {
						in.close();
						return; // Nothing to do here.
					}

					for (UUID g_member : GuildMechanics.getGuildMembers(g_name)) {
						Player pl_g_member = null;
						if (Bukkit.getPlayer(g_member) != null) {
							pl_g_member = Bukkit.getPlayer(g_member);
						}

						if (pl_g_member == null) {
							continue;
						}

						if (id.equals(pl_g_member.getUniqueId())) {
							continue; // Don't tell ourselves. (server
							// transfers)
						}

						/*
						 * if(CommunityMechanics.socialQuery(g_member, p_name, "CHECK_BUD")){ continue; // They're buddies, let the buddy plugin take care of
						 * telling them they logged in. }
						 */

						pl_g_member.sendMessage(ChatColor.DARK_AQUA + "<" + ChatColor.BOLD + GuildMechanics.guild_handle_map.get(g_name) + ChatColor.DARK_AQUA
								+ "> " + ChatColor.GRAY + op.getName() + " has logged out of " + server_name + ".");
					}

				}

				// [gupdate]guild_name
				else if (inputLine.startsWith("[gupdate]")) {
					String g_name = inputLine.substring(inputLine.indexOf("]") +  1, inputLine.length());
					if (g_name.endsWith(" ")) g_name = g_name.substring(0, g_name.length() -1);
					GuildMechanics.updateGuildSQL(g_name);
					for (Player mem : GuildMechanics.getOnlineGuildMembers(g_name)) {
						GuildMechanics.updateGuildTabList(mem);
					}
				}

				// TODO: Add /shard to character journal
				// TODO: Fix all the inconsistancies with buddy login/logout
				// Players should ONLY get messages about a player logging
				// in/out if ONE player has them added and they are NOT on other
				// player's ignore list.

				// [join]player_uuid,guild_name@server_name
				else if (inputLine.startsWith("[join]")) {
					// [join]p_name,g_name@server_name
					String raw_id = inputLine.substring(inputLine.indexOf("]") + 1, inputLine.indexOf(","));
					UUID id = UUID.fromString(raw_id);
					String g_name = inputLine.substring(inputLine.indexOf(",") + 1, inputLine.indexOf("@"));
					String server_name = inputLine.substring(inputLine.indexOf("@") + 1, inputLine.length());

					GuildMechanics.guild_member_server.put(id, server_name);
					// Set local data so that tab list will show them as online
					// another server.

					if (!(GuildMechanics.guild_map.containsKey(g_name))) {
						in.close();
						return; // Nothing to do here.
					}
					
					OfflinePlayer op = Bukkit.getOfflinePlayer(id);

					for (UUID g_member : GuildMechanics.getGuildMembers(g_name)) {
						Player pl_g_member = null;
						if (Bukkit.getPlayer(g_member) != null) {
							pl_g_member = Bukkit.getPlayer(g_member);
						}

						if (pl_g_member == null) {
							continue;
						}

						if (id.equals(g_member)) {
							continue; // Don't tell ourselves. (server
							// transfers)
						}

						/*
						 * if(CommunityMechanics.socialQuery(g_member, p_name, "CHECK_BUD")){ continue; // They're buddies, let the buddy plugin take care of
						 * telling them they logged in. }
						 */

						pl_g_member.sendMessage(ChatColor.DARK_AQUA + "<" + ChatColor.BOLD + GuildMechanics.guild_handle_map.get(g_name) + ChatColor.DARK_AQUA
								+ "> " + ChatColor.GRAY + op.getName() + " has joined " + server_name + ".");

					}

				}

				else if (inputLine.startsWith("&")) {
					if (inputLine.substring(inputLine.indexOf("&") + 1, inputLine.indexOf("/")).equalsIgnoreCase("staffchat")) {
						// packet_data = staffchat/US-O*from: packet_data contents here
						String raw_id = inputLine.substring(inputLine.indexOf("*") + 1, inputLine.indexOf(":"));
						UUID id = UUID.fromString(raw_id);
						OfflinePlayer op = Bukkit.getOfflinePlayer(id);
						String sender_server_name = inputLine.substring(inputLine.indexOf("/") + 1, inputLine.indexOf("*"));
						String raw_message = inputLine.substring(inputLine.indexOf(":") + 1, inputLine.length());

						if (Bukkit.getOnlinePlayers().size() <= 0) { in.close(); return; } // No one to receive message
						for (Player pl : Bukkit.getOnlinePlayers()) {
							if (PermissionMechanics.isStaff(pl)) {
								ChatColor pColor = ChatMechanics.getPlayerColor(id, pl.getUniqueId());
								String prefix = ChatMechanics.getPlayerPrefix(id);
								String message = raw_message;

								if (ChatMechanics.hasAdultFilter(pl)) message = ChatMechanics.censorMessage(message);
								if (message.endsWith(" ")) message = message.substring(0, message.length() - 1);
								message = ChatMechanics.fixCapsLock(message);

								pl.sendMessage(ChatColor.GOLD + "<" + ChatColor.BOLD + "SC" + ChatColor.GOLD  + "> " + sender_server_name + " " + prefix + pColor + op.getName() + ": " + ChatColor.WHITE + message);
							}
						}
						// END STAFF CHAT //
					} else {


						// packet_data = to_guild/from@US-0: packet_data contents here
						String g_name = inputLine.substring(1, inputLine.indexOf("/"));

						if (!(GuildMechanics.guild_map.containsKey(g_name))) {
							in.close();
							return; // Nothing to do here.
						}

						String raw_id = inputLine.substring(inputLine.indexOf("/") + 1, inputLine.indexOf("@"));
						UUID id = UUID.fromString(raw_id);
						OfflinePlayer op = Bukkit.getOfflinePlayer(id);
						String sender_server_name = inputLine.substring(inputLine.indexOf("@") + 1, inputLine.indexOf(":"));
						String raw_msg = inputLine.substring(inputLine.indexOf(":") + 1, inputLine.length());

						for (UUID g_member : GuildMechanics.getGuildMembers(g_name)) {
							Player pl_g_member = null;
							if (Bukkit.getPlayer(g_member) != null) {
								pl_g_member = Bukkit.getPlayer(g_member);
							}

							if (pl_g_member == null) {
								continue;
							}

							ChatColor p_color = ChatMechanics.getPlayerColor(id, g_member);
							String prefix = ChatMechanics.getPlayerPrefix(id, false);

							String personal_msg = raw_msg;
							if (ChatMechanics.hasAdultFilter(pl_g_member)) {
								personal_msg = ChatMechanics.censorMessage(personal_msg);
							}

							if (personal_msg.endsWith(" ")) {
								personal_msg = personal_msg.substring(0, personal_msg.length() - 1);
							}

							personal_msg = ChatMechanics.fixCapsLock(personal_msg);

							pl_g_member.sendMessage(ChatColor.DARK_AQUA.toString() + "<" + ChatColor.BOLD + GuildMechanics.guild_handle_map.get(g_name)
							+ ChatColor.DARK_AQUA + ">" + " " + ChatColor.GRAY + "" + sender_server_name + " " + prefix + p_color
							+ op.getName() + ": " + ChatColor.GRAY + personal_msg);
						}
					}
				}

				// END GUILDS

				else if (inputLine.startsWith("#")) { // Reply to a "^" message.
					// #player_name_to_tell/player_name_about=offline, OR
					// #player_name_to_tell/player_name_about=online,US-0:MESSAGE
					String p_name_to_tell = inputLine.substring(1, inputLine.indexOf("/"));
					String p_name_about = inputLine.substring(inputLine.indexOf("/") + 1, inputLine.indexOf("="));

					String status = inputLine.substring(inputLine.indexOf("=") + 1, inputLine.indexOf(","));
					if (status.equalsIgnoreCase("offline")) {
						if (Bukkit.getPlayer(p_name_to_tell) != null) {
							Player p_to_tell = Bukkit.getPlayer(p_name_to_tell);
							p_to_tell.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + p_name_about + ChatColor.RED + " is OFFLINE.");
						}
						in.close();
						return;
					}

					if (status.equalsIgnoreCase("notells")) {
						Bukkit.getPlayer(p_name_to_tell).sendMessage(
								ChatColor.RED + "" + ChatColor.BOLD + p_name_about + ChatColor.RED + " currently has private messages " + ChatColor.UNDERLINE
								+ "DISABLED.");
						in.close();
						return;
					}

					if (status.equalsIgnoreCase("online")) {
						if (Bukkit.getPlayer(p_name_to_tell) != null) {
							Player p_to_tell = Bukkit.getPlayer(p_name_to_tell);
							String server_name = inputLine.substring(inputLine.indexOf(",") + 1, inputLine.indexOf(":"));
							String message = inputLine.substring(inputLine.indexOf(":") + 1, inputLine.length());
							p_to_tell.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "TO (" + server_name + ") " + ChatColor.RESET + p_name_about
									+ ":" + ChatColor.WHITE + message);
						}
						in.close();
						return;
					}
					in.close();
					return;
				}

				// ^p_from_uuid%p_to_name/guild_prefix@p_from_name;from_server:message
				else if (inputLine.startsWith("^")) { // Sending message.
					inputLine = inputLine.substring(inputLine.indexOf("^") + 1, inputLine.length());

					UUID p_from_uuid = UUID.fromString(inputLine.substring(0, inputLine.indexOf("%")));
					String p_to_name = inputLine.substring(inputLine.indexOf("%") + 1, inputLine.indexOf("/"));
					String guild_prefix = inputLine.substring(inputLine.indexOf("/") + 1, inputLine.indexOf("@"));
					String p_from_name = inputLine.substring(inputLine.indexOf("@") + 1, inputLine.indexOf(";"));
					String from_server = inputLine.substring(inputLine.indexOf(";") + 1, inputLine.indexOf(":"));
					String message = inputLine.substring(inputLine.indexOf(":") + 1, inputLine.length());

					int server_num = Integer.parseInt(from_server.split("-")[1]);
					if (from_server.contains("EU-")) {
						server_num += 1000;
					}
					if (from_server.contains("BR-")) {
						server_num += 2000;
					}
					String server_ip = CommunityMechanics.server_list.get(server_num);
					if (!(PermissionMechanics.rank_map.containsKey(p_from_name))) {
						PermissionMechanics.downloadRank(p_from_uuid); // Get
						// rank
						// data
						// of
						// player
						// on
						// local
						// server.
					}

					if (Bukkit.getPlayer(p_to_name) != null) {
						Player p_to = Bukkit.getPlayer(p_to_name);
						if (!PermissionMechanics.getRank(p_from_uuid).equalsIgnoreCase("gm")
								&& !PermissionMechanics.getRank(p_from_uuid).equalsIgnoreCase("pmod")
								&& PlayerManager.getPlayerModel(p_to).getToggleList().contains("tells")
								&& !(CommunityMechanics.isPlayerOnBuddyList(p_to.getUniqueId(), p_from_uuid))) {
							String result = "#" + p_from_name + "/" + p_to_name + "=" + "notells,";
							sendResultCrossServer(server_ip, result, server_num);
							in.close();
							return;
						}

						if (!PermissionMechanics.getRank(p_from_uuid).equalsIgnoreCase("gm")
								&& !PermissionMechanics.getRank(p_from_uuid).equalsIgnoreCase("pmod")
								&& CommunityMechanics.isPlayerOnIgnoreList(p_to.getUniqueId(), p_from_uuid)) {
							String result = "#" + p_from_name + "/" + p_to_name + "=" + "offline,";
							sendResultCrossServer(server_ip, result, server_num);
							in.close();
							return;
						}

						ChatColor c = ChatColor.GRAY;

						c = ChatMechanics.getPlayerColor(p_from_uuid, p_to.getUniqueId());
						String from_prefix = ChatMechanics.getPlayerPrefix(p_from_uuid, true);

						ChatColor to_c = ChatMechanics.getPlayerColor(p_to.getUniqueId(), p_from_uuid);
						String to_prefix = ChatMechanics.getPlayerPrefix(p_to, true);

						String to_personal_msg = message;
						if (ChatMechanics.hasAdultFilter(p_to)) {
							to_personal_msg = ChatMechanics.censorMessage(message);
						}

						p_to.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "FROM (" + from_server + ") " + ChatColor.WHITE + guild_prefix
								+ ChatColor.RESET + from_prefix + c + p_from_name + ":" + ChatColor.WHITE + to_personal_msg);

						if (PlayerManager.getPlayerModel(p_to).getLastReply() == null
								|| !PlayerManager.getPlayerModel(p_to).getLastReply().equalsIgnoreCase(p_from_name)) {
							p_to.playSound(p_to.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2F, 1.2F);
							PlayerManager.getPlayerModel(p_to).setLastReply(p_from_name);
						}

						String result = "#" + p_from_name + "/" + to_prefix + to_c.toString() + p_to_name + "=" + "online,"
								+ Bukkit.getMotd().substring(0, Bukkit.getMotd().indexOf(" ")) + ":" + message;
						sendResultCrossServer(server_ip, result, server_num);
						CommunityMechanics.log.info(p_from_name + "@" + from_server + " -> " + p_to_name + " " + message);
					} else if (Bukkit.getPlayer(p_to_name) == null) {
						String result = "#" + p_from_name + "/" + p_to_name + "=" + "offline,";
						sendResultCrossServer(server_ip, result, server_num);
					}
				}

			}

			int line = 0;
			JsonBuilder data = new JsonBuilder("receiving_server", Utils.getShard()).setData("sending_server", sendingIP);

			for (String dataLine : receivedData) {
				line++;
				data.setData("line_" + String.valueOf(line), dataLine);
			}

			new LogModel(LogType.PACKET, "CONSOLE", data.getJson());
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
}
