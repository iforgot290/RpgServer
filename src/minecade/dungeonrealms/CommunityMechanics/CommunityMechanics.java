package minecade.dungeonrealms.CommunityMechanics;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import minecade.dungeonrealms.Main;
import minecade.dungeonrealms.ChatMechanics.ChatMechanics;
import minecade.dungeonrealms.CommunityMechanics.commands.CommandAdd;
import minecade.dungeonrealms.CommunityMechanics.commands.CommandDebug;
import minecade.dungeonrealms.CommunityMechanics.commands.CommandDelete;
import minecade.dungeonrealms.CommunityMechanics.commands.CommandIgnore;
import minecade.dungeonrealms.CommunityMechanics.commands.CommandRoll;
import minecade.dungeonrealms.CommunityMechanics.commands.CommandTips;
import minecade.dungeonrealms.CommunityMechanics.commands.CommandToggleChaos;
import minecade.dungeonrealms.CommunityMechanics.commands.CommandToggleFF;
import minecade.dungeonrealms.CommunityMechanics.commands.CommandToggleFilter;
import minecade.dungeonrealms.CommunityMechanics.commands.CommandToggleGlobal;
import minecade.dungeonrealms.CommunityMechanics.commands.CommandToggleGlobalChat;
import minecade.dungeonrealms.CommunityMechanics.commands.CommandToggleGuild;
import minecade.dungeonrealms.CommunityMechanics.commands.CommandToggleGuildChat;
import minecade.dungeonrealms.CommunityMechanics.commands.CommandToggleIndicator;
import minecade.dungeonrealms.CommunityMechanics.commands.CommandToggleParty;
import minecade.dungeonrealms.CommunityMechanics.commands.CommandToggleProfile;
import minecade.dungeonrealms.CommunityMechanics.commands.CommandTogglePvP;
import minecade.dungeonrealms.CommunityMechanics.commands.CommandToggleStarterPack;
import minecade.dungeonrealms.CommunityMechanics.commands.CommandToggleTabChat;
import minecade.dungeonrealms.CommunityMechanics.commands.CommandToggleTells;
import minecade.dungeonrealms.CommunityMechanics.commands.CommandToggleTips;
import minecade.dungeonrealms.CommunityMechanics.commands.CommandToggleTradeChat;
import minecade.dungeonrealms.CommunityMechanics.commands.CommandToggles;
import minecade.dungeonrealms.DuelMechanics.DuelMechanics;
import minecade.dungeonrealms.FatigueMechanics.FatigueMechanics;
import minecade.dungeonrealms.GuildMechanics.GuildMechanics;
import minecade.dungeonrealms.HealthMechanics.HealthMechanics;
import minecade.dungeonrealms.Hive.Hive;
import minecade.dungeonrealms.InstanceMechanics.InstanceMechanics;
import minecade.dungeonrealms.ItemMechanics.ItemMechanics;
import minecade.dungeonrealms.KarmaMechanics.KarmaMechanics;
import minecade.dungeonrealms.LevelMechanics.LevelMechanics;
import minecade.dungeonrealms.ModerationMechanics.ModerationMechanics;
import minecade.dungeonrealms.PermissionMechanics.PermissionMechanics;
import minecade.dungeonrealms.ScoreboardMechanics.ScoreboardMechanics;
import minecade.dungeonrealms.TradeMechanics.TradeMechanics;
import minecade.dungeonrealms.config.Config;
import minecade.dungeonrealms.database.ConnectionPool;
import minecade.dungeonrealms.managers.PlayerManager;

public class CommunityMechanics implements Listener {
	public static Logger log = Logger.getLogger("Minecraft");

	public static final String encryption_key = "Hzixcwi3M58539PM";

	public static HashMap<Integer, String> server_list = new HashMap<Integer, String>();
	// Server #, Server IP

	public static HashMap<Integer, Socket> sock_list = new HashMap<Integer, Socket>();
	// Server num, Active socket

	/**
	 * Player sender UUID mapped to their message
	 */
	public static volatile ConcurrentHashMap<UUID, String> async_pm = new ConcurrentHashMap<UUID, String>();

	public static volatile CopyOnWriteArrayList<String> socket_list = new CopyOnWriteArrayList<String>();
	// List of all sockets to be processed in ConnectProtocl.

	/**
	 * List of all sockets to be processed in ConnectProtocol<br>
	 * String key mapped to actual query
	 */
	public static volatile ConcurrentHashMap<String, List<Object>> social_query_list = new ConcurrentHashMap<String, List<Object>>();

	public static List<String> ip_whitelist = new ArrayList<String>();

	public static List<String> toggle_map = new ArrayList<String>();
	// A map used to cycle through when generating toggle menu of /toggle.

	Thread ConnectProtocol;
	// Controls socket handling.

	Thread CrossServerPacketThread;
	// SocialQueries

	public Thread message_listener;
	public static CommunityMechanics instance;
	public TipMechanics TipMechanics = new TipMechanics(this);

	public static Thread PMThread;

	private int serverNum = -1;

	public void onEnable() {
		instance = this;

		Main.plugin.getCommand("add").setExecutor(new CommandAdd());
		Main.plugin.getCommand("debug").setExecutor(new CommandDebug());
		Main.plugin.getCommand("delete").setExecutor(new CommandDelete());
		Main.plugin.getCommand("ignore").setExecutor(new CommandIgnore());
		Main.plugin.getCommand("roll").setExecutor(new CommandRoll());
		Main.plugin.getCommand("tips").setExecutor(new CommandTips());
		Main.plugin.getCommand("togglechaos").setExecutor(new CommandToggleChaos());
		Main.plugin.getCommand("toggleff").setExecutor(new CommandToggleFF());
		Main.plugin.getCommand("togglefilter").setExecutor(new CommandToggleFilter());
		Main.plugin.getCommand("toggleglobal").setExecutor(new CommandToggleGlobal());
		Main.plugin.getCommand("toggleguild").setExecutor(new CommandToggleGuild());
		Main.plugin.getCommand("toggleguildchat").setExecutor(new CommandToggleGuildChat());
		Main.plugin.getCommand("toggleparty").setExecutor(new CommandToggleParty());
		Main.plugin.getCommand("toggleprofile").setExecutor(new CommandToggleProfile());
		Main.plugin.getCommand("togglepvp").setExecutor(new CommandTogglePvP());
		Main.plugin.getCommand("toggles").setExecutor(new CommandToggles());
		Main.plugin.getCommand("togglestarterpack").setExecutor(new CommandToggleStarterPack());
		Main.plugin.getCommand("toggletells").setExecutor(new CommandToggleTells());
		Main.plugin.getCommand("toggletips").setExecutor(new CommandToggleTips());
		Main.plugin.getCommand("toggletradechat").setExecutor(new CommandToggleTradeChat());
		Main.plugin.getCommand("toggleindicator").setExecutor(new CommandToggleIndicator());
		Main.plugin.getCommand("toggleglobalchat").setExecutor(new CommandToggleGlobalChat());
		Main.plugin.getCommand("toggletabchat").setExecutor(new CommandToggleTabChat());

		Bukkit.getServer().getPluginManager().registerEvents(this, Main.plugin);
		Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(Main.plugin, "BungeeCord");

		TipMechanics.loadTips();

		serverNum = Integer.parseInt(Bukkit.getMotd().substring(Bukkit.getMotd().indexOf("-") + 1, Bukkit.getMotd().indexOf(" ")));

		new BukkitRunnable() {

			public void run() {
				processPM();
			}
		}.runTaskTimerAsynchronously(Main.plugin, 5 * 20L, 2L);

		// Tip batch sender.
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.plugin, new Runnable() {
			@SuppressWarnings("static-access")
			public void run() {
				TipMechanics.displayRandomTip();
			}
		}, 400 * 20L, 300 * 20L);

		//server_list.put(0, "69.69.69.69");

		// the following ports are listen ports, not the minecraft ports the servers are actually running on!
		//server_list.put(0, "37.187.136.77:25705");
		server_list.put(0, "127.0.0.1:25644");
		server_list.put(1, "127.0.0.1:25645");
		server_list.put(2, "127.0.0.1:25646");
		// server_list.put(2, "37.187.136.77:25705");
		//server_list.put(3, "69.69.69.69:6429");
		// server_list.put(4, "69.69.69.69:6430"); 
		// server_list.put(5, "69.69.69.69:6431");

		//server_list.put(100, "69.69.69.69");

		/* moved to new server boxes on 7/2/14
		server_list.put(1, "69.69.69.69"); // 74..63.245.13 US-1
		server_list.put(2, "69.69.69.69"); // 74..63.245.14 US-2
		server_list.put(3, "69.69.69.69"); // US-3
		server_list.put(4, "69.69.69.69"); // US-4
		// server_list.put(5, "69.69.69.69"); // US-5
		// server_list.put(6, "69.69.69.69"); // US-6
		// server_list.put(7, "69.69.69.69"); // US-7
		// server_list.put(8, "69.69.69.69"); // US-8
		server_list.put(9, "69.69.69.69"); // US-9 (VIP)
		server_list.put(10, "69.69.69.69"); // US-10 (VIP)
		server_list.put(11, "69.69.69.69"); // US-11 (RP)
		server_list.put(100, "69.69.69.69"); // US-100

		server_list.put(2001, "69.69.69.69"); // BR-1
		 */

		for (String s : server_list.values()) {
			ip_whitelist.add(s);
		}

		ip_whitelist.add("192.99.45.33"); //Live Shards
		ip_whitelist.add("127.0.0.1");
		ip_whitelist.add(Config.Hive_IP);
		//ip_whitelist.add("37.187.136.77"); // Donation Back-end Server AND US-0
		//ip_whitelist.add("69.69.69.69");
		//ip_whitelist.add("69.69.69.69");
		//ip_whitelist.add("69.69.69.69");
		//ip_whitelist.add("69.69.69.69");
		//ip_whitelist.add("69.69.69.69");
		//ip_whitelist.add("69.69.69.69");
		//ip_whitelist.add("69.69.69.69");
		//ip_whitelist.add("69.69.69.69"); // Testing packet connection for bradez
		//ip_whitelist.add("69.69.69.69"); // GM Tool Packet Manager

		toggle_map.add("toggledebug");
		toggle_map.add("toggleff");
		toggle_map.add("toggletrade");
		toggle_map.add("toggleduel");
		toggle_map.add("toggletells");
		toggle_map.add("toggleglobal");
		toggle_map.add("togglefilter");
		toggle_map.add("toggleparty");
		toggle_map.add("toggletradechat");
		toggle_map.add("togglechaos");
		toggle_map.add("togglepvp");
		toggle_map.add("toggletips");
		toggle_map.add("toggleprofile");
		toggle_map.add("togglestarterpack");
		toggle_map.add("toggleindicator");
		toggle_map.add("toggleglobalchat");
		toggle_map.add("toggletabchat");
		toggle_map.add("toggleguildchat");

		CrossServerPacketThread = new CrossServerPacketThread();
		CrossServerPacketThread.start();

		message_listener = new ListenThread();
		message_listener.start();

		log.info("[CommunityMechanics] has been enabled.");
	}

	public void onDisable() {
		log.info("[CommunityMechanics] has been disabled.");
	}

	/**
	 * Processes all async PM's... This method is called from async thread
	 */
	public void processPM() {
		if (CommunityMechanics.async_pm.size() <= 0) {
			return;
		}
		for (Entry<UUID, String> data : CommunityMechanics.async_pm.entrySet()) {
			UUID p_uuid = data.getKey();
			String msg = data.getValue();

			try {
				Player sent_from = Bukkit.getServer().getPlayer(p_uuid);

				String sent_to_s = "";
				String sent_from_s = sent_from.getName();
				if (msg.contains(" ")) {
					sent_to_s = msg.substring(1, msg.indexOf(" "));
				} else if (!(msg.contains(" "))) {
					sent_to_s = msg.substring(1, msg.length());
				}
				
				if (sent_to_s.equalsIgnoreCase("")){
					if (PlayerManager.getPlayerModel(p_uuid).getLastReply() == null){
						sent_from.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "ERROR: " + ChatColor.RED + "You have no conversation to respond to!");
						continue;
					}
					sent_to_s = PlayerManager.getPlayerModel(p_uuid).getLastReply();
				}
				
				@SuppressWarnings("deprecation")
				OfflinePlayer sent_to = Bukkit.getOfflinePlayer(sent_from_s);

				if (PlayerManager.getPlayerModel(sent_from.getUniqueId()).getToggleList().contains("tells")
						&& (!(CommunityMechanics.isPlayerOnBuddyList(sent_from, sent_to)))) {

					sent_from.sendMessage(ChatColor.RED + "You currently have non-BUD private messages " + ChatColor.BOLD + "DISABLED." + ChatColor.RED
							+ " Type '/toggletells' to re-enable.");
					continue;
				}

				if ((!PermissionMechanics.getRank(sent_from).equalsIgnoreCase("gm")
						&& (PlayerManager.getPlayerModel(sent_from).getIgnoreList().contains(sent_to_s)) || (CommunityMechanics
								.socialQuery(sent_from.getUniqueId(), sent_to.getUniqueId(), "CHECK_FOE")))
						|| (!sent_from.isOp() && ModerationMechanics.isPlayerVanished(sent_to.getUniqueId()))) {
					sent_from.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + sent_to.getName() + ChatColor.RED + " is OFFLINE.");
					continue;
				}

				int sent_to_server_data = Hive.getPlayerServer(sent_to.getUniqueId(), true); // Update.

				if (sent_to_server_data < 0) {
					if (sent_to_server_data == -1) {
						sent_from.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + sent_to_s + ChatColor.RED + " is OFFLINE.");
					} else if (sent_to_server_data == -2) {
						sent_from.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + sent_to_s + ChatColor.RED + " has " + ChatColor.UNDERLINE + "NEVER"
								+ ChatColor.RED + " logged in to Dungeon Realms.");
					}
					continue;
				}

				if (!(CommunityMechanics.server_list.containsKey(sent_to_server_data))) {
					sent_from.sendMessage(ChatColor.RED + "Your message could not be delivered because the server ID " + sent_to_server_data
							+ " is not defined.");
					continue;
				}

				String prefix = "US-";

				if (sent_to_server_data > 1000 && sent_to_server_data < 2000) {
					sent_to_server_data -= 1000;
					prefix = "EU-";
				}

				if (sent_to_server_data > 2000 && sent_to_server_data < 3000) {
					sent_to_server_data -= 2000;
					prefix = "BR-";
				}

				if (sent_to_server_data >= 3000) {
					sent_to_server_data -= 3000;
					prefix = "US-YT";
				}

				String sent_to_server = prefix + sent_to_server_data;
				String local_server = Bukkit.getMotd().substring(0, Bukkit.getMotd().indexOf(" "));
				String message = "";

				if (msg.contains(" ")) {
					message = msg.substring(msg.indexOf(" "), msg.length());
				} else if (!(msg.contains(" "))) {
					message = "";
				}

				if (sent_to.getPlayer() != null) {
					// Local msg'ing.
					Player sent_to_p = sent_to.getPlayer();

					PlayerManager.getPlayerModel(sent_from).setLastPMTime(System.currentTimeMillis());

					if (!sent_from.isOp()
							&& (PlayerManager.getPlayerModel(sent_to_p).getToggleList().contains("tells") && !(CommunityMechanics.isPlayerOnBuddyList(
									sent_to, sent_from)))
							|| (PlayerManager.getPlayerModel(sent_from).getToggleList().contains("tells") && !(CommunityMechanics.isPlayerOnBuddyList(
									sent_from, sent_to)))) {
						if (PlayerManager.getPlayerModel(sent_to_p).getToggleList().contains("tells")) {
							sent_from.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + sent_to_p.getName() + ChatColor.RED
									+ " currently has private messaging " + ChatColor.UNDERLINE + "DISABLED.");
							continue;
						}
						continue;
					}

					CommunityMechanics.log.info(sent_from_s + " --> " + sent_to_s + " " + msg);

					ChatColor st_c = ChatMechanics.getPlayerColor(sent_from, sent_to_p);
					ChatColor sf_c = ChatMechanics.getPlayerColor(sent_to_p, sent_from);

					String from_prefix = ChatMechanics.getPlayerPrefix(sent_from);
					String to_prefix = ChatMechanics.getPlayerPrefix(sent_to_p);

					String to_personal_msg = message;
					if (ChatMechanics.hasAdultFilter(sent_to_p)) {
						to_personal_msg = ChatMechanics.censorMessage(message);
					}

					String from_personal_msg = message;
					if (ChatMechanics.hasAdultFilter(sent_from)) {
						from_personal_msg = ChatMechanics.censorMessage(message);
					}

					sent_to_p.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "FROM " + from_prefix + st_c + sent_from_s + ":" + ChatColor.WHITE
							+ to_personal_msg);
					sent_from.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "TO " + to_prefix + sf_c + sent_to_p.getName() + ":" + ChatColor.WHITE
							+ from_personal_msg);
					if (PlayerManager.getPlayerModel(sent_to_p).getLastReply() == null
							|| !PlayerManager.getPlayerModel(sent_to_p).getLastReply().equalsIgnoreCase(sent_from_s)) {
						sent_to_p.playSound(sent_to_p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 2F, 1.2F);
						PlayerManager.getPlayerModel(sent_to_p).setLastReply(sent_from_s);
					}
				} else if (!(sent_to_server.equalsIgnoreCase(local_server))) {
					List<Object> query = new ArrayList<Object>();
					query.add("^" + sent_from.getUniqueId().toString() + "%" + sent_to_s + "/" + GuildMechanics.getGuildPrefix(sent_from) + "@" + sent_from_s + ";" + local_server + ":" + message);
					query.add(sent_to_s);
					query.add(false);
					social_query_list.put(p_uuid.toString(), query);
					// log.info("d1 - " + (String)query.get(0));
					// CommunityMechanics.sendMessageCrossServer(message_to_send,
					// CommunityMechanics.server_list.get(o_sent_to_server),
					// o_sent_to_server);
				}

				CommunityMechanics.async_pm.remove(p_uuid);
			} catch (Exception err) {
				err.printStackTrace();
				CommunityMechanics.async_pm.remove(p_uuid);
				continue;
			}
		}

		CommunityMechanics.async_pm.clear();
	}

	/**
	 * Sets the player name color
	 * @param pl player to set
	 * @param c color to set player as
	 */
	public static void setColor(Player pl, ChatColor c) {
		if (pl == null) {
			System.out.print("Player was null! chatcolor > " + c.toString());
			return;
		}

		ScoreboardMechanics.setPlayerColor(c, pl);
	}

	/**
	 * Gets if the player is online or not (Thread blocking)
	 * @param p_uuid player to check
	 * @return if the player is online
	 */
	public static boolean isPlayerOnline(UUID p_uuid) {
		int server_num = Hive.getPlayerServer(p_uuid, true);
		return server_num > -1;
	}
	
	/**
	 * Gets if the player is online or not (Thread blocking)
	 * @param player player to check
	 * @return if the player is online
	 */
	public static boolean isPlayerOnline(OfflinePlayer player){
		return isPlayerOnline(player.getUniqueId());
	}
	
	/**
	 * Gets if the player is online or not (Only thread blocking if refresh is true)
	 * @param p_uuid player to check
	 * @param refresh refresh from sql server
	 * @return if the player is online
	 */
	public static boolean isPlayerOnline(UUID p_uuid, boolean refresh){
		int server_num = Hive.getPlayerServer(p_uuid, refresh);
		return server_num > -1;
	}

	/**
	 * Gets if the player is online or not (Only thread blocking if refresh is true)
	 * @param player player to check
	 * @param refresh refresh from sql server
	 * @return if the player is online
	 */
	public static boolean isPlayerOnline(OfflinePlayer player, boolean refresh) {
		return isPlayerOnline(player.getUniqueId(), refresh);
	}

	/**
	 * Only updates the first few pages of the character journal, the pages with realtime, non-SQL/socket based information.
	 * @param p player to update book for
	 */
	public static void updateCombatPage(Player p) {
		List<String> new_pages = new ArrayList<String>();
		int slot = -1;
		if (p.getInventory().contains(Material.WRITTEN_BOOK)) {
			for (Entry<Integer, ? extends ItemStack> data : p.getInventory().all(Material.WRITTEN_BOOK).entrySet()) {
				ItemStack is = data.getValue();
				if (isSocialBook(is)) {
					slot = data.getKey();
					break;
				}
			}
		}

		if (slot != -1) {
			ItemStack journal = p.getInventory().getItem(slot);

			BookMeta bm = (BookMeta) journal.getItemMeta();
			List<String> pages = bm.getPages();
			String page1_string = "";
			String page2_string = "";
			String page3_string = "";
			String page4_string = "";
			String page5_string = "";
			String page6_string = "";
			String new_line = "\n" + ChatColor.WHITE.toString() + "`" + "\n";
			String pretty_align = KarmaMechanics.getAlignment(p);
			String raw_align = KarmaMechanics.getRawAlignment(p);
			String align_descrip = KarmaMechanics.getAlignmentDescription(raw_align);
			String align_expire_message = "";

			if (raw_align.equalsIgnoreCase("neutral")) {
				align_expire_message = "\n" + ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "Lawful" + ChatColor.BLACK.toString() + " in "
						+ KarmaMechanics.getSecondsUntilAlignmentChange(p) + "s";
			}

			if (raw_align.equalsIgnoreCase("evil")) {
				align_expire_message = "\n" + ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "Neutral" + ChatColor.BLACK.toString() + " in "
						+ KarmaMechanics.getSecondsUntilAlignmentChange(p) + "s";
			}

			DecimalFormat df = new DecimalFormat("#.##");

			String money_space = "";
			if (align_expire_message.length() > 0) {
				money_space = new_line;
			} else {
				money_space = new_line;
			}

			String gold_find = "1.00";
			if (ItemMechanics.gfind_data.containsKey(p.getUniqueId())) {
				gold_find = (df.format((((double) ItemMechanics.gfind_data.get(p.getUniqueId())) / 100.0D) + 1.0D));
				if (gold_find.equalsIgnoreCase("1")) {
					gold_find = "1.00";
				}

				String item_find = "1.00";
				if (ItemMechanics.ifind_data.containsKey(p.getUniqueId())) {
					item_find = (df.format((((double) ItemMechanics.ifind_data.get(p.getUniqueId())) / 100.0D) + 1.0D));
				}
				if (item_find.equalsIgnoreCase("1")) {
					item_find = "1.00";
				}

				page1_string = ChatColor.BLACK.toString() + "" + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "  Your Character" + "   " + "\n"
						+ ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "Alignment: " + pretty_align + align_expire_message + "\n"
						+ ChatColor.BLACK.toString() + align_descrip + new_line + ChatColor.BLACK.toString() + "   " + HealthMechanics.getPlayerHP(p.getName())
						+ " / " + HealthMechanics.health_data.get(p.getUniqueId()) + "" + ChatColor.BOLD.toString() + " HP" + "\n" + ChatColor.BLACK.toString()
						+ "   " + ItemMechanics.armor_data.get(p.getUniqueId()).get(0) + " - " + ItemMechanics.armor_data.get(p.getUniqueId()).get(1) + "% "
						+ ChatColor.BOLD.toString() + "Armor" + "\n" + ChatColor.BLACK.toString() + "   " + ItemMechanics.dmg_data.get(p.getUniqueId()).get(0)
						+ " - " + ItemMechanics.dmg_data.get(p.getUniqueId()).get(1) + "% " + ChatColor.BOLD.toString() + "DPS" + "\n" + ChatColor.BLACK.toString()
						+ "   " + (HealthMechanics.health_regen_data.get((p.getUniqueId())) + 5) + " " + ChatColor.BOLD.toString() + "HP/s" + "\n"
						+ ChatColor.BLACK.toString() + "   " + df.format((((double) FatigueMechanics.energy_regen_data.get((p.getUniqueId()))) * 100.0D) + 90.0D)
						+ "% " + ChatColor.BOLD.toString() + "Energy" + "\n" + ChatColor.BLACK.toString() + "   " + gold_find + "x "
						+ ChatColor.BOLD.toString() + "Gem Find" + "\n" + ChatColor.BLACK.toString() + "   " + item_find + "x " + ChatColor.BOLD.toString()
						+ "Item Find" + money_space + ChatColor.BLACK.toString() + "" + Hive.player_ecash.get(p.getUniqueId()) + " " + ChatColor.BOLD.toString()
						+ "E-CASH";
				
				page2_string = ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "  ** LEVEL/EXP **\n\n" + ChatColor.BLACK + ChatColor.BOLD
						+ "       LEVEL\n\n" + "         " + ChatColor.BLACK + LevelMechanics.getPlayerLevel(p) + "\n\n" + ChatColor.BLACK + ChatColor.BOLD
						+ "          XP" + "\n" + ChatColor.BLACK + "       " + LevelMechanics.getPlayerData(p).getXP() + " / "
						+ LevelMechanics.getPlayerData(p).getEXPNeeded(LevelMechanics.getPlayerLevel(p));
				
				page3_string = "\n" + ChatColor.BLACK.toString() + "   " + ItemMechanics.fire_res_data.get(p.getUniqueId()) + "% " + ChatColor.BOLD.toString()
						+ "Fire Resist" + "\n" + ChatColor.BLACK.toString() + "   " + ItemMechanics.ice_res_data.get(p.getUniqueId()) + "% "
						+ ChatColor.BOLD.toString() + "Ice Resist" + "\n" + ChatColor.BLACK.toString() + "   " + ItemMechanics.poison_res_data.get(p.getUniqueId())
						+ "% " + ChatColor.BOLD.toString() + "Poison Resist" + new_line + ChatColor.BLACK.toString() + "Monsters that deal" + "\n"
						+ "elemental damage will" + "\n" + "ignore 80% of your" + "\n" + "ARMOR." + new_line + ChatColor.BLACK.toString()
						+ "Fire, Ice, and Poison" + "\n" + "resistances will take" + "\n" + "the place of your" + "\n" + "ARMOR vs. elements.";

				int str_val = ItemMechanics.str_data.get(p.getUniqueId());
				int dex_val = ItemMechanics.dex_data.get(p.getUniqueId());
				int vit_val = ItemMechanics.vit_data.get(p.getUniqueId());
				int int_val = ItemMechanics.int_data.get(p.getUniqueId());

				page4_string = ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "+ " + str_val + " Strength"
						+ "\n" + ChatColor.BLACK.toString() + "   " + ChatColor.UNDERLINE.toString() + "'The Warrior'"
						+ ChatColor.BLACK.toString() + "+" + df.format(str_val * 0.03) + "% Armor" + "\n"
						+ ChatColor.BLACK.toString() + "+" + df.format(str_val * 0.017) + "% Block" + "\n"
						+ ChatColor.BLACK.toString() + "+" + df.format(str_val * 0.015) + "% Axe DMG" + "\n"
						+ ChatColor.BLACK.toString() + "+" + df.format(str_val * 0.023) + "% Polearm DMG" + "\n" + "\n"
						+ ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "+ " + dex_val + " Dexterity" + "\n"
						+ ChatColor.BLACK.toString() + "   " + ChatColor.UNDERLINE.toString() + "'The Archer'"
						+ ChatColor.BLACK.toString() + "+" + df.format(dex_val * 0.03) + "% Dodge" + "\n"
						+ ChatColor.BLACK.toString() + "+" + df.format(dex_val * 0.015) + "% Bow DMG" + "\n"
						+ ChatColor.BLACK.toString() + "+" + df.format(dex_val * 0.005) + "% Critical Hit" + "\n"
						+ ChatColor.BLACK.toString() + "+" + df.format(dex_val * 0.009) + "% Armor Pen.";

				page5_string = ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "+ " + vit_val + " Vitality"
						+ "\n" + ChatColor.BLACK.toString() + "   " + ChatColor.UNDERLINE.toString() + "'The Defender'"
						+ ChatColor.BLACK.toString() + "+" + df.format(vit_val * 0.034) + "% Health" + "\n"
						+ ChatColor.BLACK.toString() + "+" + df.format(vit_val * 0.3) + "   HP/s" + "\n"
						+ ChatColor.BLACK.toString() + "+" + df.format(vit_val * 0.04) + "% Ele Resist" + "\n"
						+ ChatColor.BLACK.toString() + "+" + df.format(vit_val * 0.01) + "% Sword DMG" + "\n" + "\n"
						+ ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "+ " + int_val + " Intellect" + "\n"
						+ ChatColor.BLACK.toString() + "   " + ChatColor.UNDERLINE.toString() + "'The Mage'" + new_line
						+ ChatColor.BLACK.toString() + "+" + df.format(int_val * 0.015) + "% Energy" + "\n"
						+ ChatColor.BLACK.toString() + "+" + df.format(int_val * 0.05) + "% Ele Damage" + "\n"
						+ ChatColor.BLACK.toString() + "+" + df.format(int_val * 0.025) + "% Critical Hit" + "\n"
						+ ChatColor.BLACK.toString() + "+" + df.format(int_val * 0.02) + "% Staff DMG";

				page6_string = ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "Portal Key Shards" + "\n" + ChatColor.BLACK.toString()
				+ ChatColor.ITALIC.toString()
				+ "A sharded fragment from the great portal of Maltai that may be exchanged at the Dungeoneer for epic equipment." + new_line
				+ ChatColor.DARK_GRAY.toString() + "Portal Shards: " + ChatColor.BLACK + InstanceMechanics.getPortalShardCount(p, 1) + "\n"
				+ ChatColor.GREEN.toString() + "Portal Shards: " + ChatColor.BLACK + InstanceMechanics.getPortalShardCount(p, 2) + "\n"
				+ ChatColor.AQUA.toString() + "Portal Shards: " + ChatColor.BLACK + InstanceMechanics.getPortalShardCount(p, 3) + "\n"
				+ ChatColor.LIGHT_PURPLE.toString() + "Portal Shards: " + ChatColor.BLACK + InstanceMechanics.getPortalShardCount(p, 4)
				+ "\n" + ChatColor.GOLD.toString() + "Portal Shards: " + ChatColor.BLACK + InstanceMechanics.getPortalShardCount(p, 5);

				new_pages.add(page1_string);
				new_pages.add(page2_string);
				new_pages.add(page3_string);
				new_pages.add(page4_string);
				new_pages.add(page5_string);
				new_pages.add(page6_string);
				for (String s : pages) {
					if (!s.contains("Your Character") && !s.startsWith(ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "+ ")
							&& !(s.contains("Monsters that deal")) && !(s.contains("Portal Key Shards")) && !s.contains("LEVEL/EXP")) {
						new_pages.add(s);
					}
				}

				bm.setPages(new_pages);
				journal.setItemMeta(bm);
				p.getInventory().setItem(slot, journal);
				p.updateInventory();
			}

			if (slot == -1) { // They don't even have a book.
				if (p.getInventory().getItem(8) == null || p.getInventory().getItem(8).getType() == Material.AIR) {
					p.getInventory().setItem(8, generateCommBook(p));
				} else {
					p.getInventory().setItem(p.getInventory().firstEmpty(), generateCommBook(p));
				}
			}
		}
	}

	/*
	 * Generates a full character journal. -- updateCombatPage() will run after this everytime the book opens to insert those pages.
	 */
	//TODO fuck this book
	public static ItemStack generateCommBook(final Player p) {
		if (p == null || !p.isOnline()) {
			return null;
		}
		//String new_line = "\n" + ChatColor.WHITE.toString() + "`" + "\n";
		ItemStack i = new ItemStack(Material.WRITTEN_BOOK, 1);
		/*List<String> the_pages = new ArrayList<String>();
		List<String> lbuddy_list = PlayerManager.getPlayerModel(p).getBuddyList();
		List<String> lignore_list = PlayerManager.getPlayerModel(p).getIgnoreList();

		int buddies_printed = 0;
		boolean first_buddy_page = true;

		String pretty_align = KarmaMechanics.getAlignment(p.getName());
		String raw_align = KarmaMechanics.align_map.get(p.getName());
		String align_descrip = KarmaMechanics.getAlignmentDescription(raw_align);
		String align_expire_message = "";

		if (!KarmaMechanics.align_map.containsKey(p.getName())) {
			raw_align = "good";
			align_expire_message = "";
		}

		if (raw_align.equalsIgnoreCase("neutral")) {
			align_expire_message = "\n" + ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "Lawful" + ChatColor.BLACK.toString() + " in "
					+ KarmaMechanics.getSecondsUntilAlignmentChange(p.getName()) + "s";
		}

		if (raw_align.equalsIgnoreCase("evil")) {
			align_expire_message = "\n" + ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "Neutral" + ChatColor.BLACK.toString() + " in "
					+ KarmaMechanics.getSecondsUntilAlignmentChange(p.getName()) + "s";
		}

		DecimalFormat df = new DecimalFormat("#.##");

		String money_space = "";
		if (align_expire_message.length() > 0) {
			money_space = new_line;
		} else {
			money_space = new_line;
		}

		String gold_find = "1.00";
		if (ItemMechanics.gfind_data.containsKey(p.getName())) {
			gold_find = (df.format((((double) ItemMechanics.gfind_data.get(p.getName())) / 100.0D) + 1.0D));
			if (gold_find.equalsIgnoreCase("1")) {
				gold_find = "1.00";
			}
		}

		String item_find = "1.00";
		if (ItemMechanics.ifind_data.containsKey(p.getName())) {
			item_find = (df.format((((double) ItemMechanics.ifind_data.get(p.getName())) / 100.0D) + 1.0D));
		}
		if (item_find.equalsIgnoreCase("1")) {
			item_find = "1.00";
		}

		try {
			the_pages.add(ChatColor.BLACK.toString() + "" + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "  Your Character" + "   " + "\n"
					+ ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "Alignment: " + pretty_align + align_expire_message + "\n"
					+ ChatColor.BLACK.toString() + align_descrip + new_line + ChatColor.BLACK.toString() + "   " + HealthMechanics.getPlayerHP(p.getName())
					+ " / " + HealthMechanics.health_data.get(p.getName()) + "" + ChatColor.BOLD.toString() + " HP" + "\n" + ChatColor.BLACK.toString() + "   "
					+ ItemMechanics.armor_data.get(p.getName()).get(0) + " - " + ItemMechanics.armor_data.get(p.getName()).get(1) + "% "
					+ ChatColor.BOLD.toString() + "Armor" + "\n" + ChatColor.BLACK.toString() + "   " + ItemMechanics.dmg_data.get(p.getName()).get(0) + " - "
					+ ItemMechanics.dmg_data.get(p.getName()).get(1) + "% " + ChatColor.BOLD.toString() + "DPS" + "\n" + ChatColor.BLACK.toString() + "   "
					+ (HealthMechanics.health_regen_data.get((p.getName())) + 5) + " " + ChatColor.BOLD.toString() + "HP/s" + "\n" + ChatColor.BLACK.toString()
					+ "   " + df.format((((double) FatigueMechanics.energy_regen_data.get((p.getName()))) * 100.0D) + 90.0D) + "% " + ChatColor.BOLD.toString()
					+ "Energy" + "\n" + ChatColor.BLACK.toString() + "   " + gold_find + "x " + ChatColor.BOLD.toString() + "Gem Find" + "\n"
					+ ChatColor.BLACK.toString() + "   " + item_find + "x " + ChatColor.BOLD.toString() + "Item Find" + money_space
					+ ChatColor.BLACK.toString() + "" + Hive.player_ecash.get(p.getName()) + " " + ChatColor.BOLD.toString() + "E-CASH");
			// + RealmMechanics.getMoneyInInventory(p) + " / " +
			// (MoneyMechanics.bank_map.get(p.getName()) +
			// RealmMechanics.getMoneyInInventory(p)) +
			// ChatColor.BOLD.toString() + "G");
		} catch (NullPointerException npe) {
			log.info("[CommunityMechanics] Failed to give book to " + p.getName());
			npe.printStackTrace();
			return null;
		}
		if (lbuddy_list == null || lbuddy_list.size() == 0 || PlayerManager.getPlayerModel(p).getBuddyList() == null) {
			the_pages.add(ChatColor.BLACK.toString() + "" + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "     Buddy List" + "      " + "\n"
					+ ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "@<PLAYER> <MSG>" + "\n" + ChatColor.BLACK.toString()
					+ "Sends <MSG> to <PLAYER>." + new_line + ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "/add <PLAYER>" + "\n"
					+ ChatColor.BLACK.toString() + "Adds PLAYER to buddy list." + new_line + ChatColor.BLACK.toString() + ChatColor.BOLD.toString()
					+ "/delete <PLAYER>" + "\n" + ChatColor.BLACK.toString() + "Deletes PLAYER from all lists.");
		}

		if (!(lbuddy_list == null)) {
			List<String> format_buddy_list = new ArrayList<String>();

			for (String s : lbuddy_list) {
				s = ChatColor.stripColor(s);
				String online_mark = "";
				// Boolean is_buddy = socialQuery(p.getName(), s, "CHECK_BUD");
				boolean online = isPlayerOnline(s);

				if (online) { // is_buddy &&
					online_mark = ChatColor.DARK_GREEN.toString() + ChatColor.BOLD.toString() + "O";
				} else if (!(online)) {
					online_mark = ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString() + "O";
				}

				int server_num = getPlayerServer(s); // TODO: See if this is
				// causing SQL
				// bottleneck.

				OfflinePlayer op = Bukkit.getServer().getOfflinePlayer(s);

				if (server_num >= 0 && !op.isOp()) {

					String prefix = "US-";

					if (server_num > 1000 && server_num < 2000) {
						server_num -= 1000;
						prefix = "EU-";
					}

					if (server_num > 2000 && server_num < 3000) {
						server_num -= 2000;
						prefix = "BR-";
					}

					if (server_num >= 3000) {
						server_num -= 3000;
						prefix = "US-YT";
					}

					String remote_server = prefix + server_num;

					format_buddy_list.add(ChatColor.BLACK.toString() + "" + online_mark + ChatColor.BLACK.toString() + " " + ChatColor.BOLD.toString() + s
							+ "\n" + ChatColor.BLACK.toString() + "Shard: " + remote_server + new_line);
				}
			}

			for (String s : lbuddy_list) {
				s = ChatColor.stripColor(s);
				String online_mark = "";
				online_mark = ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString() + "O";

				int server_num = getPlayerServer(s); // TODO: See if this is
				// causing SQL
				// bottleneck.

				OfflinePlayer op = Bukkit.getServer().getOfflinePlayer(s);

				if (server_num == -1 || op.isOp()) {
					Long last_login = getLastLogin(s, false); // TODO: Make this time save locally or have it DL database at start
					String release_s = "";

					if (last_login == -1L || op.isOp()) {
						release_s = "NEVER";
					} else {
						Date startTime, endTime;
						endTime = new Date(System.currentTimeMillis());
						startTime = new Date(last_login);

						long sec;
						sec = (endTime.getTime() - startTime.getTime()) / 1000;
						int hour = (int) (sec / 3600);
						sec = sec % 3600;
						int min = (int) (sec / 60);
						sec = sec % 60;

						if (min <= 0) {
							release_s = sec + "s ago";
						} else if (release_s == "" && (min > 0 && min <= 60) && hour <= 0) {
							release_s = min + "m ago";
						} else if (release_s == "" && (hour > 0 && hour <= 24)) {
							release_s = hour + "h ago";
						} else if (release_s == "") {
							release_s = String.valueOf(Math.round((hour / 24))) + "d ago";
						}
					}

					format_buddy_list.add(ChatColor.BLACK.toString() + "" + online_mark + ChatColor.BLACK.toString() + " " + ChatColor.BOLD.toString() + s
							+ "\n" + ChatColor.BLACK.toString() + "Last On: " + release_s + new_line);

					//
					// page_structure += ChatColor.BLACK.toString() + "" + online_mark + ChatColor.BLACK.toString() + " " + ChatColor.BOLD.toString() + s + "\n"
					// + ChatColor.BLACK.toString() + "Last On: " + release_s + new_line; being_added_to_book.remove(s);
					//
				}
			}

			for (String s : lbuddy_list) {
				s = ChatColor.stripColor(s);
				String online_mark = "";
				online_mark = ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString() + "O";

				int server_num = getPlayerServer(s); // TODO: See if this is
				// causing SQL
				// bottleneck.

				if (server_num == -2) {
					format_buddy_list.add(ChatColor.BLACK.toString() + "" + online_mark + ChatColor.BLACK.toString() + " " + ChatColor.BOLD.toString() + s
							+ "\n" + ChatColor.BLACK.toString() + "Last On: " + "NEVER" + new_line);

					//
					// page_structure += ChatColor.BLACK.toString() + "" + online_mark + ChatColor.BLACK.toString() + " " + ChatColor.BOLD.toString() + s + "\n"
					// + ChatColor.BLACK.toString() + "Last On: " + "NEVER" + new_line; being_added_to_book.remove(s);
					//
				}
			}

			int attempts = 50;
			while (format_buddy_list.size() > buddies_printed && attempts > 0) {
				attempts--;
				int buddies_to_print = 5;

				if (first_buddy_page == true) {
					buddies_to_print = 4;
				}

				if ((buddies_printed + buddies_to_print) > lbuddy_list.size()) {
					buddies_to_print = lbuddy_list.size() - buddies_printed;
					// Less than 5 buddies left to add. So we just add one final
					// page.
				}

				CopyOnWriteArrayList<String> being_added_to_book = new CopyOnWriteArrayList<String>(format_buddy_list.subList(buddies_printed,
						(buddies_printed + buddies_to_print)));
				String page_structure = "";

				if (first_buddy_page == true) {
					page_structure += ChatColor.BLACK.toString() + "" + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "     Buddy List"
							+ "      " + "\n" + ChatColor.BLACK.toString();
				}

				for (String s : being_added_to_book) {
					page_structure += s;
				}

				the_pages.add(page_structure);
				buddies_printed += buddies_to_print;
				first_buddy_page = false;
			}
		}

		if (lignore_list == null || lignore_list.size() == 0 || PlayerManager.getPlayerModel(p).getIgnoreList() == null) {
			the_pages.add(ChatColor.BLACK.toString() + "" + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "    Ignore List" + "     " + new_line
					+ ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "/ignore <PLAYER>" + "\n" + ChatColor.BLACK.toString()
					+ "Adds PLAYER to ignore list." + new_line + ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "/delete <PLAYER>" + "\n"
					+ ChatColor.BLACK.toString() + "Deletes PLAYER from all lists.");
		}

		if (lignore_list != null) {
			int ignores_printed = 0;
			boolean first_ignore_page = true;

			while (lignore_list.size() > ignores_printed) {
				int ignores_to_print = 13;

				if (first_ignore_page == true) {
					ignores_to_print = 11;
				}

				if ((ignores_printed + ignores_to_print) > lignore_list.size()) {
					ignores_to_print = lignore_list.size() - ignores_printed;
				}
				// Less than 5 ignores left to add. So we just add one final
				// page.
				List<String> being_added_to_book = lignore_list.subList(ignores_printed, (ignores_printed + ignores_to_print));
				String page_structure = "";

				if (first_ignore_page == true) {
					page_structure += ChatColor.BLACK.toString() + "" + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "    Ignore List"
							+ "     " + new_line + ChatColor.BLACK.toString();
					first_ignore_page = false;
				}

				for (String s : being_added_to_book) {
					page_structure += ChatColor.BLACK.toString() + s + "\n";
				}

				the_pages.add(page_structure);
				ignores_printed += ignores_to_print;
			}
		}

		the_pages.add(ChatColor.BLACK.toString() + "" + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "   Command Guide  " + new_line
				+ ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "@<PLAYER> <MSG>" + "\n" + ChatColor.BLACK.toString() + "Sends a PM." + new_line
				+ ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "/shard" + "\n" + ChatColor.BLACK.toString() + "Opens game shard selection menu."
				+ new_line + ChatColor.BLACK.toString() + ChatColor.BOLD + "Press TAB (CHAT)" + "\n" + ChatColor.BLACK.toString()
				+ "Sends MESSAGE typed to all players on the shard. ");

		the_pages.add(ChatColor.BLACK.toString() + ChatColor.BOLD + "/realm" + ChatColor.BLACK.toString() + " <TITLE>" + "\n" + ChatColor.BLACK.toString()
		+ "Sets your realm description to TITLE." + new_line + ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "/add "
		+ ChatColor.BLACK.toString() + "<PLAYER>" + "\n" + ChatColor.BLACK.toString() + "Adds PLAYER to buddy list." + new_line
		+ ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "/ignore " + ChatColor.BLACK.toString() + "<PLAYER>" + "\n"
		+ ChatColor.BLACK.toString() + "Adds PLAYER to ignore list." + new_line + ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "/delete "
		+ ChatColor.BLACK.toString() + "<PLAYER>" + "\n" + ChatColor.BLACK.toString() + "Deletes PLAYER from           all lists.");

		the_pages.add(ChatColor.BLACK.toString() + ChatColor.BOLD.toString()
		+ "/[p/g]invite"
		+ "\n"
		+ ChatColor.BLACK.toString()
		+ "Invite to p(arty) or g(uild)" // "               "
		+ new_line + ChatColor.BLACK + ChatColor.BOLD.toString() + "/[p/g]kick " + "\n" + ChatColor.BLACK.toString()
		+ "Kick player from p(arty) or g(uild)" + new_line + ChatColor.BLACK + ChatColor.BOLD.toString() + "/[p/g]quit " + "\n"
		+ ChatColor.BLACK.toString() + "Leave your p(arty) or g(uild)" + new_line + ChatColor.BLACK.toString() + ChatColor.BOLD.toString()
		+ "/[p/g]promote " + "\n" + ChatColor.BLACK.toString() + "Set to p(arty) leader         g(uild) officer"); // " + "
		// " + "party
		// leader"

		//
		 //the_pages.add(ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "/toggledebug" + "\n" + ChatColor.BLACK.toString() + "Toggles debug messages."
		 ////"               " + new_line + ChatColor.BLACK + ChatColor.BOLD.toString() + "/toggleff " + "\n" + ChatColor.BLACK.toString() +
		 //"Toggles friendly fire against buddies." + new_line + ChatColor.BLACK + ChatColor.BOLD.toString() + "/toggletrade " + "\n" +
		 //ChatColor.BLACK.toString() + "Toggles accepting player trades." + new_line + ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "/toggleduel "
		 // + "\n" + ChatColor.BLACK.toString() + "Toggles accepting" + "        " + "     duels");
		 //
		 //the_pages.add(ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "/toggletells " + "\n" + ChatColor.BLACK.toString() +
		 //"Toggles Non-BUD private messages." + new_line + ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "/toggleglobal " + "\n" +
		 //ChatColor.BLACK.toString() + "Toggles Global Chat messaging." + new_line + ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "/togglefilter "
		 //+ "\n" + ChatColor.BLACK.toString() + "Toggles adult chat filter." + new_line + ChatColor.BLACK.toString() + ChatColor.BOLD.toString() +
		 //"/toggleparty " + "\n" + ChatColor.BLACK.toString() + "Toggles Non-BUD              party invites");
		 //

		the_pages.add(ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "/toggles " + "\n" + ChatColor.BLACK.toString() + "Display Toggle Menu."
				+ new_line + ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "/roll " + "\n" + ChatColor.BLACK.toString() + "Rolls a random number."
				+ new_line + ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "/logout " + "\n" + ChatColor.BLACK.toString()
				+ "Safetly logs out your character." + new_line + ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "/report " + "\n"
				+ ChatColor.BLACK.toString() + "Submit a ticket to the staff.");

		the_pages.add(ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "/biography " + "\n" + ChatColor.BLACK.toString() + "Write your player bio."
				+ new_line + ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "/gbiography " + "\n" + ChatColor.BLACK.toString()
				+ "Write your guild bio." + new_line + ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "/gbanner " + "\n" + ChatColor.BLACK.toString()
				+ "Upload your guild banner." + new_line + ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "/suicide " + "\n"
				+ ChatColor.BLACK.toString() + "KILLS your character.");

		the_pages.add(ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "/reboot " + "\n" + ChatColor.BLACK.toString()
		+ "Time until next scheduled reboot." + new_line + ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "/profile " + "\n"
		+ ChatColor.BLACK.toString() + "Displays link to player profile." + new_line + ChatColor.BLACK.toString() + ChatColor.BOLD.toString()
		+ "/ecash " + "\n" + ChatColor.BLACK.toString() + "Opens E-CASH Vendor." + new_line + ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString()
		+ "      ! CAUTION !" + "\n" + ChatColor.BLACK.toString() + ChatColor.BOLD.toString() + "/resetrealm " + "\n" + ChatColor.BLACK.toString()
		+ "Resets your player owned realm. ");
		*/
		
		List<String> the_pages = new ArrayList<String>();
		
		the_pages.add("Fuck this book");

		BookMeta bm = (BookMeta) i.getItemMeta();

		bm.setAuthor("");
		bm.setLore(new ArrayList<String>(Arrays.asList(ChatColor.GRAY.toString() + "A book that displays", ChatColor.GRAY.toString() + "your character's data",
				ChatColor.GREEN.toString() + "Left Click: " + ChatColor.GRAY + "Invite to Party", ChatColor.GREEN.toString() + "Sneak-Left Click:"
						+ ChatColor.GRAY + " Setup Shop")));
		bm.setTitle(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "Character Journal");
		bm.setPages(the_pages);

		i.setItemMeta(bm);

		return i;
	}

	/**
	 * Gets the last login time (Thread blocking if not found locally)
	 * @param p_uuid player uuid to fetch login time for
	 * @return the last login time for the player
	 */
	public static long getLastLogin(UUID p_uuid) {

		if (PlayerManager.getPlayerModel(p_uuid).getLastLocalLogin() > 0)
			return PlayerManager.getPlayerModel(p_uuid).getLastLocalLogin();

		Connection con = null;
		PreparedStatement pst = null;

		try {

			pst = ConnectionPool.getConnection().prepareStatement("SELECT last_login_time FROM player_database WHERE p_name = '" + p_uuid + "'");

			pst.execute();
			ResultSet rs = pst.getResultSet();
			if (!rs.next()) {
				return -1L;
			}
			long amount = rs.getLong("last_login_time");
			PlayerManager.getPlayerModel(p_uuid).setLastLocalLogin(amount);
			return amount;

		} catch (SQLException ex) {
			log.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				log.log(Level.WARNING, ex.getMessage(), ex);
			}
		}

		return -1L;
	}
	
	/**
	 * Gets the last login time (Thread blocking if not found locally)
	 * @param player player to fetch login time for
	 * @return the last login time for the player
	 */
	public static long getLastLogin(OfflinePlayer player){
		return getLastLogin(player.getUniqueId());
	}

	/**
	 * Checks if the specified itemstack is the social book
	 * @param i itemstack to check
	 * @return if the itemstack is a social book
	 */
	public static boolean isSocialBook(ItemStack i) {
		try {
			if (i.getType() == Material.WRITTEN_BOOK && hasTitle(i)) {
				String fake_var = CraftItemStack.asNMSCopy(i).getTag().getString("title");
				if (fake_var.equalsIgnoreCase(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "Character Journal")) {
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}

		return false;
	}

	/**
	 * Checks if the specified itemstack has a book title
	 * @param i itemstack to check
	 * @return if the itemstack has a title
	 */
	public static boolean hasTitle(ItemStack i) {
		try {
			try {
				String fake_var = CraftItemStack.asNMSCopy(i).getTag().getString("title");
				if (fake_var != null && fake_var.length() > 0) {
					return true;
				}
			} catch (NullPointerException npe) {
				return false;
			}

		} catch (ClassCastException cce) {
			return false;
		}

		return false;
	}

	/**
	 * Async uploads the social lists of a player (buddy and ignore)
	 * @param uuid uuid to upload social lists for
	 */
	public static void upload_social_lists(final UUID uuid){

		Bukkit.getScheduler().runTaskAsynchronously(Main.plugin, new Runnable(){

			@SuppressWarnings("resource")
			public void run(){
				Connection con = null;
				PreparedStatement pst = null;

				if (PlayerManager.getPlayerModel(uuid).getBuddyList() == null && PlayerManager.getPlayerModel(uuid).getIgnoreList() == null) {
					return;
				}

				String buddy_list_string = "";
				String ignore_list_string = "";

				if (PlayerManager.getPlayerModel(uuid).getBuddyList() != null) {
					List<UUID> lbuddy_list = PlayerManager.getPlayerModel(uuid).getBuddyList();
					for (UUID s : lbuddy_list) {
						buddy_list_string += s.toString() + ",";
					}
				}

				if (PlayerManager.getPlayerModel(uuid).getIgnoreList() != null) {
					List<UUID> lignore_list = PlayerManager.getPlayerModel(uuid).getIgnoreList();
					for (UUID s : lignore_list) {
						ignore_list_string += s.toString() + ",";
					}

				}
				try {
					if (PlayerManager.getPlayerModel(uuid).getBuddyList() != null) {
						pst = ConnectionPool.getConnection().prepareStatement(
								"INSERT INTO player_database (p_name, buddy_list)" + " VALUES" + "('" + uuid.toString() + "', '"
										+ StringEscapeUtils.escapeSql(buddy_list_string) + "') ON DUPLICATE KEY UPDATE buddy_list='"
										+ StringEscapeUtils.escapeSql(buddy_list_string) + "'");

						pst.executeUpdate();
					}

					if (PlayerManager.getPlayerModel(uuid).getIgnoreList() != null) {
						pst = ConnectionPool.getConnection().prepareStatement(
								"INSERT INTO player_database (p_name, ignore_list)" + " VALUES" + "('" + uuid.toString() + "', '"
										+ StringEscapeUtils.escapeSql(ignore_list_string) + "') ON DUPLICATE KEY UPDATE ignore_list='"
										+ StringEscapeUtils.escapeSql(ignore_list_string) + "'");

						pst.executeUpdate();
					}

				} catch (SQLException ex) {
					log.log(Level.SEVERE, ex.getMessage(), ex);

				} finally {
					try {
						if (pst != null) {
							pst.close();
						}
						if (con != null) {
							con.close();
						}

					} catch (SQLException ex) {
						log.log(Level.WARNING, ex.getMessage(), ex);
					}
				}
			}

		});

	}

	/**
	 * Async uploads the social lists of a player (buddy and ignore)
	 * @param p player to upload the social lists for
	 */
	public static void upload_social_lists(Player p) {
		upload_social_lists(p.getUniqueId());
	}

	/**
	 * Thread blocking query for a player's buddy list
	 * @param p_uuid uuid to get buddy list for
	 * @return the player's buddy list in UUID format
	 */
	public static List<UUID> query_buddy_list(UUID p_uuid) {
		Connection con = null;
		PreparedStatement pst = null;
		List<UUID> lbuddy_list = new ArrayList<UUID>();

		try {
			pst = ConnectionPool.getConnection().prepareStatement("SELECT buddy_List FROM player_database WHERE p_name = '" + p_uuid.toString() + "'");

			pst.execute();
			ResultSet rs = pst.getResultSet();
			if (!rs.next()) {
				return null;
			}
			String result_set = rs.getString("friends");

			for (String s : result_set.split(",")) {
				if (s.length() > 0) {
					lbuddy_list.add(UUID.fromString(s));
				}
			}

		} catch (SQLException ex) {
			log.log(Level.SEVERE, ex.getMessage(), ex);

		} finally {
			try {
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				log.log(Level.WARNING, ex.getMessage(), ex);
			}
		}

		return lbuddy_list;
	}

	/**
	 * Checks if a player is on a certain player's buddy list
	 * @param p player's buddy list to check
	 * @param p_to_check player to check is on first player's buddy list
	 * @return if the player is on the first player's buddy list
	 */
	public static boolean isPlayerOnBuddyList(UUID p, UUID p_to_check){
		if (PlayerManager.getPlayerModel(p).getBuddyList() == null){
			return false;
		}

		for (UUID uuid : PlayerManager.getPlayerModel(p).getBuddyList()){
			if (uuid.equals(p_to_check)){
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks if a player is on a certain player's buddy list
	 * @param p player's buddy list to check
	 * @param p_to_check player to check is on first player's buddy list
	 * @return if the player is on the first player's buddy list
	 */
	public static boolean isPlayerOnBuddyList(OfflinePlayer p, OfflinePlayer p_to_check) {
		return isPlayerOnBuddyList(p.getUniqueId(), p_to_check.getUniqueId());
	}

	/**
	 * Checks if the player is on ignore list
	 * @param p_uuid uuid of the player's ignore list to check
	 * @param uuid_to_check uuid of the player to check if on ignore list
	 * @return if the player is on the first player's ignore list
	 */
	public static boolean isPlayerOnIgnoreList(UUID p_uuid, UUID uuid_to_check){
		if (PlayerManager.getPlayerModel(p_uuid).getIgnoreList() == null){
			return false;
		}

		for (UUID uuid : PlayerManager.getPlayerModel(p_uuid).getIgnoreList()){
			if (uuid.equals(uuid_to_check))
				return true;
		}

		return false;
	}

	/**
	 * Checks if the player is on ignore list
	 * @param p player's ignore list to check
	 * @param p_to_check player to check if on the ignore list
	 * @return if the player is on the first player's ignore list
	 */
	public static boolean isPlayerOnIgnoreList(OfflinePlayer p, OfflinePlayer p_to_check) {
		return isPlayerOnIgnoreList(p.getUniqueId(), p_to_check.getUniqueId());
	}

	/**
	 * Checks relation of remote player to local player based on metadata
	 * @param local_player local player to check
	 * @param remote_player remote player to check
	 * @param meta_data check_bud or check_foe
	 * @return if remote player has metadata in relation to local player
	 */
	public static boolean socialQuery(UUID local_player, UUID remote_player, String meta_data) {

		/*
		 * CHECK_BUD = Check if remote_player has local_player on their friends list. TRUE=yes, FALSE=no CHECK_FOE = Check if remote_player has local_player on
		 * their ignore list. TRUE=yes, FALSE=no
		 *
		 * ONLINE = Check if remote_player has local_player on their friends list AND if they do, tell them that local_player has logged in. TRUE=yes, FALSE=no
		 * OFFLINE = Check if remote_player has local_player on their friends list AND if they do, tell them that local_player has logged out. TRUE=yes,
		 * FALSE=no
		 */

		OfflinePlayer op = Bukkit.getOfflinePlayer(local_player);

		if (op.isOp() && meta_data.equalsIgnoreCase("CHECK_BUD")) {
			return false;
		}

		if (PlayerManager.getPlayerModel(local_player).getLocalConfirmedBuddies() != null && (meta_data.equalsIgnoreCase("CHECK_BUD"))) {
			if (PlayerManager.getPlayerModel(local_player).getLocalConfirmedBuddies().contains(remote_player)) {
				return true;
			}
		}

		if (PlayerManager.getPlayerModel(local_player).getLocalConfirmedIgnores() != null && (meta_data.equalsIgnoreCase("CHECK_FOE"))) {
			if (PlayerManager.getPlayerModel(local_player).getLocalConfirmedIgnores().contains(remote_player)) {
				return true;
			}
		}

		if (remote_player != null && Bukkit.getPlayer(remote_player) != null) {
			// They're on the same server.
			Player p_check = Bukkit.getPlayer(remote_player);
			if (p_check == null) {
				return false;
			}

			if (meta_data.equalsIgnoreCase("CHECK_BUD")) {
				if (PlayerManager.getPlayerModel(p_check).getBuddyList() == null) {
					return false;
				}
				for (UUID s : PlayerManager.getPlayerModel(p_check).getBuddyList()) {
					if (s.equals(local_player)) {
						List<UUID> confirmed_buds = PlayerManager.getPlayerModel(local_player).getLocalConfirmedBuddies();
						confirmed_buds.add(p_check.getUniqueId());
						PlayerManager.getPlayerModel(local_player).setLocalConfirmedBuddies(confirmed_buds);
						return true;
					}
				}
			}

			if (meta_data.equalsIgnoreCase("CHECK_FOE")) {
				if (PlayerManager.getPlayerModel(p_check).getIgnoreList() == null)
					return false;
				if (PlayerManager.getPlayerModel(p_check).getIgnoreList().contains(local_player)) {
					for (UUID s : PlayerManager.getPlayerModel(p_check).getIgnoreList()) {
						if (s.equals(local_player)) {
							List<UUID> confirmed_foes = PlayerManager.getPlayerModel(local_player).getLocalConfirmedIgnores();
							confirmed_foes.add(p_check.getUniqueId());
							PlayerManager.getPlayerModel(local_player).setLocalConfirmedBuddies(confirmed_foes);
							return true;
						}
					}
				}
			}

			// If it's ONLINE/OFFLINE query, then nothing needs to happen and
			// this can return false, because it would of already taken care of
			// the request on PlayerJoin().
			return false;
		}

		if (!(isPlayerOnline(remote_player))) {
			if (meta_data.equalsIgnoreCase("CHECK_BUD")) {
				return true;
			}
			if (meta_data.equalsIgnoreCase("CHECK_FOE")) {
				return false; // They don't need to be enemies if they're offline
			}
		}

		/*
		 * TODO: Add cross-server support for bud/foe CHECKING
		 */

		if (meta_data.equalsIgnoreCase("CHECK_BUD")) {
			return true;
		}
		if (meta_data.equalsIgnoreCase("CHECK_FOE")) {
			return false;
		}

		if (meta_data.equalsIgnoreCase("ONLINE")) {
			List<Object> query = new ArrayList<Object>();
			query.add("[sq_online]" + local_player.toString());
			query.add(remote_player.toString());

			query.add(false);
			social_query_list.put(local_player.toString(), query);
			// sendPacketCrossServer("[sq_online]" + local_player,
			// getPlayerServer(remote_player), false);
		}
		if (meta_data.equalsIgnoreCase("OFFLINE")) {
			List<Object> query = new ArrayList<Object>();
			query.add("[sq_offline]" + local_player.toString());
			query.add(remote_player.toString());
			query.add(false);
			social_query_list.put(local_player.toString(), query);
			// sendPacketCrossServer("[sq_offline]" + local_player,
			// getPlayerServer(remote_player), false);
		}
		
		return true;
	}

	/**
	 * Adds a buddy to the player's friends list
	 * @param p_uuid friends list to add to
	 * @param new_friend new friend
	 */
	public static void addBuddy(UUID p_uuid, UUID new_friend){
		List<UUID> cur_list = new ArrayList<UUID>();
		if (PlayerManager.getPlayerModel(p_uuid).getBuddyList() != null){
			cur_list = PlayerManager.getPlayerModel(p_uuid).getBuddyList();
		}

		cur_list.add(new_friend);
		PlayerManager.getPlayerModel(p_uuid).setBuddyList(cur_list);

		upload_social_lists(p_uuid);
	}

	/**
	 * Adds a buddy to the player's friends list
	 * @param player friends list to add to
	 * @param new_friend new friend
	 */
	public static void addBuddy(OfflinePlayer player, OfflinePlayer new_friend) {
		addBuddy(player.getUniqueId(), new_friend.getUniqueId());
	}

	/**
	 * Adds an ignore to the player's friends list
	 * @param host ignore list to add to
	 * @param new_ignore new ignored person
	 */
	public static void addIgnore(UUID host, UUID new_ignore){
		// boolean first_add = false;
		List<UUID> cur_list = new ArrayList<UUID>();
		if (PlayerManager.getPlayerModel(host).getIgnoreList() != null) {
			cur_list = PlayerManager.getPlayerModel(host).getIgnoreList();
		}

		cur_list.add(new_ignore);
		PlayerManager.getPlayerModel(host).setIgnoreList(cur_list);

		upload_social_lists(host);
	}

	/**
	 * Adds an ignore to the player's friends list
	 * @param host ignore list to add to
	 * @param new_ignore new ignored person
	 */
	public static void addIgnore(OfflinePlayer host, OfflinePlayer new_ignore) {
		addIgnore(host.getUniqueId(), new_ignore.getUniqueId());
	}

	/**
	 * Delete person from all lists
	 * @param host player list to delete from
	 * @param to_remove player to delete
	 */
	public static void deleteFromAllLists(UUID host, UUID to_remove){

		if (isPlayerOnBuddyList(host, to_remove)){
			List<UUID> cur_list = PlayerManager.getPlayerModel(host).getBuddyList();
			UUID save_s = null;
			for (UUID s : cur_list) {
				if (s.equals(to_remove)) {
					save_s = s;
					break;
				}
			}

			if (save_s != null) {
				cur_list.remove(save_s);
				PlayerManager.getPlayerModel(host).setBuddyList(cur_list);
				Bukkit.getPlayer(host).sendMessage(ChatColor.YELLOW + "" + save_s + ChatColor.YELLOW + " has been removed from your BUDDY list.");
				return; // We can return, if they were on buddy list they won't
				// be on ignore.
			}
		}

		if (isPlayerOnIgnoreList(host, to_remove)) {
			List<UUID> cur_list = PlayerManager.getPlayerModel(host).getIgnoreList();
			UUID save_s = null;
			for (UUID s : cur_list) {
				if (s.equals(to_remove)) {
					save_s = s;
					break;
				}
			}

			if (save_s != null) {
				cur_list.remove(save_s);
				PlayerManager.getPlayerModel(host).setIgnoreList(cur_list);
				Bukkit.getPlayer(host).sendMessage(ChatColor.YELLOW + "" + save_s + ChatColor.YELLOW + " has been removed from your IGNORE list.");
				return;
			}
		}

		//TODO is this necessary?
		if (Bukkit.getPlayer(host) != null){
			Player p = Bukkit.getPlayer(host);
			p.sendMessage(ChatColor.YELLOW + "" + to_remove + ChatColor.YELLOW + " is not on any of your social lists.");
			updateCommBook(p);
		}

	}

	/**
	 * Delete person from all lists
	 * @param host player list to delete from
	 * @param to_remove player to delete
	 */
	public static void deleteFromAllLists(OfflinePlayer host, OfflinePlayer to_remove) {
		deleteFromAllLists(host.getUniqueId(), to_remove.getUniqueId());
	}

	/**
	 * Gets the buddy list size
	 * @param p_uuid buddy list to get size of
	 * @return size of buddy list
	 */
	public static int getBuddyListLength(UUID p_uuid) {
		if (PlayerManager.getPlayerModel(p_uuid).getBuddyList() == null) {
			return 0;
		}
		return PlayerManager.getPlayerModel(p_uuid).getBuddyList().size();
	}

	/**
	 * Gets the buddy list size
	 * @param player buddy list to get the size of
	 * @return size of buddy list
	 */
	public static int getBuddyListLength(Player player){
		return getBuddyListLength(player.getUniqueId());
	}

	/**
	 * Handles friendly fire
	 * @param e Bukkit entity damage event
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamageByEntityEvent(EntityDamageEvent e) {
		if (e.getCause() == DamageCause.ENTITY_ATTACK && e instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) e;

			if (e.getEntity() instanceof Player && edbee.getDamager() instanceof Player) {
				Player attacker = (Player) edbee.getDamager();
				Player hurt = (Player) e.getEntity();

				if (isPlayerOnBuddyList(attacker, hurt)) {
					if (!PlayerManager.getPlayerModel(attacker).getToggleList().contains("ff")) {
						if (DuelMechanics.duel_map.containsKey(attacker.getUniqueId()) && DuelMechanics.duel_map.containsKey(hurt.getUniqueId())) {
							return;
						}
						e.setCancelled(true); // Friendly fire is OFF.
						e.setDamage(0);
					}
				}
			}
		}

		if (e.getCause() == DamageCause.PROJECTILE && e instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent) e;

			if (e.getEntity() instanceof Player && edbee.getDamager() instanceof Arrow) {
				Arrow a = (Arrow) edbee.getDamager();
				if (a.getShooter() instanceof Player) {
					Player shooter = (Player) a.getShooter();
					Player hurt = (Player) e.getEntity();

					if (isPlayerOnBuddyList(shooter, hurt)) {
						if (!PlayerManager.getPlayerModel(shooter).getToggleList().contains("ff")) {
							if (DuelMechanics.duel_map.containsKey(shooter.getUniqueId()) && DuelMechanics.duel_map.containsKey(hurt.getUniqueId())) {
								return;
							}
							e.setCancelled(true); // Friendly fire is OFF.
							e.setDamage(0);
						}
					}
				}
			}
		}
	}

	/**
	 * Gets the toggle description of specified toggle
	 * @param toggle toggle to get desc of
	 * @return the desc of the toggle
	 */
	public static String getToggleDescription(String toggle) {
		String desc = ChatColor.GRAY.toString();
		if (toggle.equalsIgnoreCase("toggledebug")) {
			desc += "Toggles displaying combat debug messages.";
		}
		if (toggle.equalsIgnoreCase("toggleff")) {
			desc += "Toggles friendly-fire between buddies.";
		}
		if (toggle.equalsIgnoreCase("toggletrade")) {
			desc += "Toggles trading requests.";
		}
		if (toggle.equalsIgnoreCase("toggleduel")) {
			desc += "Toggles dueling requests.";
		}
		if (toggle.equalsIgnoreCase("toggletells")) {
			desc += "Toggles recieving NON-BUD /tell.";
		}
		if (toggle.equalsIgnoreCase("toggleglobal")) {
			desc += "Toggles recieving <G>lobal chat.";
		}
		if (toggle.equalsIgnoreCase("togglefilter")) {
			desc += "Toggles the adult chat filter.";
		}
		if (toggle.equalsIgnoreCase("toggleparty")) {
			desc += "Toggles recieving NON-BUD /pinvite.";
		}
		if (toggle.equalsIgnoreCase("toggletradechat")) {
			desc += "Toggles recieving <T>rade chat.";
		}
		if (toggle.equalsIgnoreCase("togglechaos")) {
			desc += "Toggles killing blows on lawful players (anti-chaotic).";
		}
		if (toggle.equalsIgnoreCase("togglepvp")) {
			desc += "Toggles all outgoing PvP damage (anti-neutral).";
		}
		if (toggle.equalsIgnoreCase("toggletips")) {
			desc += "Toggles displaying noob-friendly server tips.";
		}
		if (toggle.equalsIgnoreCase("toggleprofile")) {
			desc += "Toggles displaying inventory and stats on website statistics.";
		}
		if (toggle.equalsIgnoreCase("togglestarterpack")) {
			desc += "Toggles recieving starter bread and potions on respawn.";
		}
		if (toggle.equalsIgnoreCase("toggleindicator")) {
			desc += "Toggles damage indicators when in combat.";
		}
		if (toggle.equalsIgnoreCase("toggleglobalchat")) {
			desc += "Toggles talking only in global chat.";
		}
		if (toggle.equalsIgnoreCase("toggletabchat")) {
			desc += "Toggles tabcomplete chat.";
		}
		if (toggle.equalsIgnoreCase("toggleguildchat")) {
			desc += "Toggles guild recruitment chat";
		}
		return desc;
	}

	/**
	 * Generates the toggle button for menus
	 * @param toggle the toggle feature to create the button for
	 * @param on if the button should be on or not (green = on)
	 * @return the generated itemstack
	 */
	public static ItemStack generateToggleButton(String toggle, boolean on) {
		ItemStack toggle_button = new ItemStack(Material.INK_SACK);
		ChatColor cc = null;

		if (on) {
			toggle_button.setDurability((short) 10);
			cc = ChatColor.GREEN;
		} else if (!on) {
			toggle_button.setDurability((short) 8);
			cc = ChatColor.RED;
		}

		ItemMeta im = toggle_button.getItemMeta();
		im.setDisplayName(cc + "/" + toggle);

		List<String> lore = new ArrayList<String>();
		lore.add(getToggleDescription(toggle));

		im.setLore(lore);
		toggle_button.setItemMeta(im);

		return toggle_button;
	}

	/**
	 * Checks if the itemstack is a toggle button
	 * @param is itemstack to check
	 * @return if the itemstack is a toggle button
	 */
	public boolean isToggleButton(ItemStack is) {
		if (is != null
				&& is.getType() == Material.INK_SACK
				&& (is.getDurability() == (short) 8 || is.getDurability() == (short) 10)
				&& is.hasItemMeta()
				&& is.getItemMeta().hasDisplayName()
				&& (is.getItemMeta().getDisplayName().startsWith(ChatColor.GREEN.toString() + "/toggle") || is.getItemMeta().getDisplayName()
						.startsWith(ChatColor.RED.toString() + "/toggle"))) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the socket for specified server
	 * @param server_num server number to get socket for
	 * @return the socket
	 */
	public static Socket getSocket(int server_num) {
		Socket return_socket = sock_list.get(server_num);
		int delay = 200;
		if (return_socket == null || return_socket.isClosed()) {
			try {
				/*
				 * if(server_num > 1000 && server_num < 3000){ delay = 3000; }
				 */
				String ipAndPort = server_list.get(server_num);
				String ipNoPort = ipAndPort.contains(":") ? server_list.get(server_num).split(":")[0]
						: ipAndPort;
				int port = ipAndPort.contains(":") ? Integer.parseInt(ipAndPort
						.split(":")[1]) : Config.transfer_port;
				Socket s = new Socket();
				// s.bind(new InetSocketAddress(Hive.local_IP,
				// Hive.transfer_port+1));
				s.connect(new InetSocketAddress(ipNoPort, port), delay);
				return s;
			} catch (IOException e) {
				// e.printStackTrace(); Worthless spam for dead servers.
			}
		}
		return return_socket;
	}

	// @server_num@p_name:server_num
	//TODO fix creating a socket each time
	/**
	 * Sends a packet cross server
	 * @param packet_data data to be sent
	 * @param server_num server number to send to
	 * @param all_servers if it should be sent to all servers or not
	 */
	public static void sendPacketCrossServer(String packet_data, int server_num, boolean all_servers) {
		Socket kkSocket = null;
		PrintWriter out = null;

		if (all_servers) {
			for (int sn : CommunityMechanics.server_list.keySet()) {
				String ipAndPort = CommunityMechanics.server_list.get(sn);
				String ipNoPort = ipAndPort.contains(":") ? CommunityMechanics.server_list.get(sn).split(":")[0]
						: ipAndPort;
				int port = ipAndPort.contains(":") && StringUtils.isNumeric(ipAndPort.split(":")[1]) ? Integer.parseInt(ipAndPort
						.split(":")[1]) : Config.transfer_port;
				if (sn == Main.getCommunityMechanics().getServerNum()) {
					continue; // Don't send to same server.
				}
				try {
					kkSocket = new Socket();
					// kkSocket.bind(new InetSocketAddress(Hive.local_IP,
					// Hive.transfer_port+1));
					kkSocket.connect(new InetSocketAddress(ipNoPort, port), 100);
					out = new PrintWriter(kkSocket.getOutputStream(), true);

					out.println(packet_data);

				} catch (IOException e) {
					if (out != null) {
						out.close();
					}
					continue;
				}

				if (out != null) {
					out.close();
				}
			}
		} else if (!all_servers) {
			try {
				String ipAndPort = CommunityMechanics.server_list.get(server_num);
				String ipNoPort = ipAndPort.contains(":") ? CommunityMechanics.server_list.get(server_num).split(":")[0]
						: ipAndPort;
				int port = ipAndPort.contains(":") && StringUtils.isNumeric(ipAndPort.split(":")[1]) ? Integer.parseInt(ipAndPort
						.split(":")[1]) : Config.transfer_port;
				kkSocket = new Socket();
				// kkSocket.bind(new InetSocketAddress(Hive.local_IP,
				// Hive.transfer_port+1));
				kkSocket.connect(new InetSocketAddress(ipNoPort, port), 100);
				out = new PrintWriter(kkSocket.getOutputStream(), true);

				out.println(packet_data);

			} catch (IOException e) {

			} finally {
				if (out != null) {
					out.close();
				}
			}

			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * Sends a packet cross server
	 * @param packet_data data to be sent
	 * @param ip ip to send the data to
	 */
	public static void sendPacketCrossServer(String packet_data, String ip) {
		Socket kkSocket = null;
		PrintWriter out = null;

		try {
			kkSocket = new Socket();
			// kkSocket.bind(new InetSocketAddress(Hive.local_IP,
			// Hive.transfer_port+1));
			String ipAndPort = ip;
			String ipNoPort = ipAndPort.contains(":") ? ip.split(":")[0]
					: ipAndPort;
			int port = ipAndPort.contains(":") && StringUtils.isNumeric(ipAndPort.split(":")[1]) ? Integer.parseInt(ipAndPort
					.split(":")[1]) : Config.transfer_port;
			kkSocket.connect(new InetSocketAddress(ipNoPort, port), 100);
			out = new PrintWriter(kkSocket.getOutputStream(), true);

			out.println(packet_data);
			kkSocket.close();
		} catch (IOException e) {

		} finally {
			if (out != null) {
				out.close();
			}
		}

		if (out != null) {
			out.close();
		}
	}

	/*
	 * public static void sendMessageCrossServer(String message, String server_ip, int server_num){ // message = to/from@US-0: Message contents here. //
	 * server_ip = IP.
	 *
	 * String p_name = message.substring(0, message.indexOf("/")); Socket kkSocket = null; PrintWriter out = null;
	 *
	 * try { try { out = new PrintWriter(getSocket(server_num).getOutputStream(), true); } catch (SocketException e) { if(out != null){ out.close(); } kkSocket
	 * = new Socket(); kkSocket.connect(new InetSocketAddress(server_ip, Hive.transfer_port), 100); out = new PrintWriter(kkSocket.getOutputStream(), true);
	 * e.printStackTrace(); log.info(Ansi.ansi().fg(Ansi.Color.RED).boldOff().toString() + "[CommunityMechanics] FAILED to send message to " + p_name +
	 * " on server " + server_ip + ". --> REROUTING..."); }
	 *
	 * out.println(message);
	 *
	 * } catch (IOException e) { if(out != null){ out.close(); } e.printStackTrace(); log.info(Ansi.ansi().fg(Ansi.Color.RED).boldOff().toString() +
	 * "[CommunityMechanics] FAILED to send message to " + p_name + " on server " + server_ip + "."); return; }
	 *
	 * if(out != null){ out.close(); } }
	 */

	/**
	 * Handles if the command is a private message and then cancels it so it will be 
	 * handled by the async PM thread
	 * @param e Bukkit pre command event
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onPlayerCommandPreProcess(PlayerCommandPreprocessEvent e) {
		String root_cmd = e.getMessage().split(" ")[0];
		Player p = e.getPlayer();
		if (root_cmd.equalsIgnoreCase("/r") || root_cmd.equalsIgnoreCase("/reply")) {
			String message = "@ " + e.getMessage().replaceAll(root_cmd + " ", "");
			Set<Player> plist = new HashSet<Player>();
			plist.add(p);
			AsyncPlayerChatEvent asce = new AsyncPlayerChatEvent(false, p, message, plist);
			Bukkit.getPluginManager().callEvent(asce); // Call chat event.
			e.setCancelled(true);
		}

		if (root_cmd.equalsIgnoreCase("/whisper") || root_cmd.equalsIgnoreCase("/m") || root_cmd.equalsIgnoreCase("/msg") || root_cmd.equalsIgnoreCase("/tell")
				|| root_cmd.equalsIgnoreCase("/t") || root_cmd.equalsIgnoreCase("/w") || root_cmd.equalsIgnoreCase("/to")
				|| root_cmd.equalsIgnoreCase("/message")) {
			if (!(e.getMessage().contains(" "))) {
				p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Incorrect syntax. " + root_cmd + " <PLAYER> <MESSAGE>");
				e.setCancelled(true);
				return;
			}
			String to_player = e.getMessage().split(" ")[1];
			String message = "@" + to_player + " " + e.getMessage().replaceAll(root_cmd + " " + e.getMessage().split(" ")[1] + " ", "");
			Set<Player> plist = new HashSet<Player>();
			plist.add(p);
			AsyncPlayerChatEvent asce = new AsyncPlayerChatEvent(false, p, message, plist);
			Bukkit.getPluginManager().callEvent(asce); // Call chat event.
			e.setCancelled(true);
		}

	}

	/**
	 * Handles messages that are meant to be PM's and puts them in the async PM thread
	 * @param e Bukkit async chat event
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerAsyncChatEvent(final AsyncPlayerChatEvent e) {
		if (!e.getMessage().startsWith("@")) {
			return;
		}
		e.setCancelled(true);

		if (PlayerManager.getPlayerModel(e.getPlayer()).getLastPMTime() > 0) {
			long last = PlayerManager.getPlayerModel(e.getPlayer()).getLastPMTime();
			if ((System.currentTimeMillis() - last) <= 500) {
				return; // Do
				// nothing,
				// been
				// too
				// short.
			}
		}

		async_pm.put(e.getPlayer().getUniqueId(), e.getMessage());

		// Depreciated, using Thread Pooling (processPMs())
		/*
		 * Player sent_from = e.getPlayer(); String sent_to_s = ""; if(e.getMessage().contains(" ")){ sent_to_s = e.getMessage().substring(1,
		 * e.getMessage().indexOf(" ")); } else if(!(e.getMessage().contains(" "))){ sent_to_s = e.getMessage().substring(1, e.getMessage().length()); }
		 *
		 * if(toggle_list.get(sent_from.getName()).contains("tells") && (!(isPlayerOnBuddyList(sent_from, sent_to_s)))){ // They have tells disabled and the
		 * reciever is NOT a bud, so they can't reply. sent_from.sendMessage(ChatColor.RED + "You currently have non-BUD private messages " + ChatColor.BOLD +
		 * "DISABLED." + ChatColor.RED + " Type '/toggletells' to re-enable."); return; }
		 *
		 * if(sent_to_s.equalsIgnoreCase("")){ if(!last_reply.containsKey(sent_from.getName())){ sent_from.sendMessage(ChatColor.RED + "" + ChatColor.BOLD +
		 * "ERROR: " + ChatColor.RED + "You have no conversation to respond to!"); return; } sent_to_s = last_reply.get(sent_from.getName()); }
		 *
		 * if(ignore_list.get(sent_from.getName()).contains(sent_to_s) || socialQuery(sent_from.getName(), sent_to_s, "CHECK_FOE")){
		 * sent_from.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + sent_to_s + ChatColor.RED + " is OFFLINE."); return; }
		 *
		 * int sent_to_server_data = getPlayerServer(sent_to_s, true); // Update.
		 *
		 * if(sent_to_server_data < 0){ if(sent_to_server_data == -1){ sent_from.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + sent_to_s + ChatColor.RED +
		 * " is OFFLINE."); } else if(sent_to_server_data == -2){ sent_from.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + sent_to_s + ChatColor.RED +
		 * " has " + ChatColor.UNDERLINE + "NEVER" + ChatColor.RED + " logged in to Dungeon Realms."); } return; }
		 *
		 * if(!(server_list.containsKey(sent_to_server_data))){ sent_from.sendMessage(ChatColor.RED +
		 * "Your message could not be delivered because the server ID " + sent_to_server_data + " is not defined."); return; }
		 *
		 * int o_sent_to_server = sent_to_server_data; String prefix = "US-";
		 *
		 * if(sent_to_server_data > 1000 && sent_to_server_data < 2000){ sent_to_server_data -= 1000; prefix = "EU-"; }
		 *
		 * if(sent_to_server_data > 2000 && sent_to_server_data < 3000){ sent_to_server_data -= 2000; prefix = "BR-"; }
		 *
		 *
		 * if(sent_to_server_data >= 3000){ sent_to_server_data -= 3000; prefix = "US-YT"; }
		 *
		 * String sent_to_server = prefix + sent_to_server_data; String local_server = Bukkit.getMotd().substring(0, Bukkit.getMotd().indexOf(" ")); String
		 * message = "";
		 *
		 * if(e.getMessage().contains(" ")){ message = e.getMessage().substring(e.getMessage().indexOf(" "), e.getMessage().length()); } else
		 * if(!(e.getMessage().contains(" "))){ message = ""; }
		 *
		 * if(Bukkit.getPlayer(sent_to_s) != null){ // Local msg'ing. Player sent_to_p = Bukkit.getPlayer(sent_to_s); sent_to_s = sent_to_p.getName();
		 *
		 * last_pm.put(e.getPlayer().getName(), System.currentTimeMillis());
		 *
		 * if((toggle_list.get(sent_to_p.getName()).contains("tells") && !(isPlayerOnBuddyList(sent_to_p, sent_from.getName()))) ||
		 * (toggle_list.get(sent_from.getName()).contains("tells") && !(isPlayerOnBuddyList(sent_from, sent_to_p.getName())))){
		 * if(toggle_list.get(sent_to_p.getName()).contains("tells")){ sent_from.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + sent_to_p.getName() +
		 * ChatColor.RED + " currently has private messaging " + ChatColor.UNDERLINE + "DISABLED."); return; } return; }
		 *
		 * log.info(sent_from.getName() + " --> " + sent_to_s + " " + e.getMessage());
		 *
		 * ChatColor st_c = ChatMechanics.getPlayerColor(sent_from, sent_to_p); ChatColor sf_c = ChatMechanics.getPlayerColor(sent_to_p, sent_from);
		 *
		 * String from_prefix = ChatMechanics.getPlayerPrefix(sent_from); String to_prefix = ChatMechanics.getPlayerPrefix(sent_to_p);
		 *
		 * String to_personal_msg = message; if(ChatMechanics.hasAdultFilter(sent_to_p.getName())){ to_personal_msg = ""; for(String s : message.split(" ")){
		 * for(String bad : ChatMechanics.bad_words){ if(s.contains(bad)){ s = s.replaceAll(bad, "****"); } } to_personal_msg += s + " "; } }
		 *
		 * String from_personal_msg = message; if(ChatMechanics.hasAdultFilter(sent_from.getName())){ from_personal_msg = ""; for(String s :
		 * message.split(" ")){ for(String bad : ChatMechanics.bad_words){ if(s.contains(bad)){ s = s.replaceAll(bad, "****"); } } from_personal_msg += s + " ";
		 * } }
		 *
		 * sent_to_p.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "FROM " + from_prefix + st_c + sent_from.getName() + ":" + ChatColor.WHITE +
		 * to_personal_msg); sent_from.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "TO " + to_prefix + sf_c + sent_to_p.getName() + ":" +
		 * ChatColor.WHITE + from_personal_msg); if(!(last_reply.containsKey(sent_to_p.getName())) || !last_reply.get(sent_to_p
		 * .getName()).equalsIgnoreCase(sent_from.getName())){ sent_to_p.playSound(sent_to_p.getLocation(), Sound.CHICKEN_EGG_POP, 2F, 1.2F);
		 * last_reply.put(sent_to_p.getName(), sent_from.getName()); } } else if(!(sent_to_server.equalsIgnoreCase(local_server))){ String message_to_send = "^"
		 * + sent_to_s + "/" + GuildMechanics.getGuildPrefix(sent_from.getName()) + "@" + sent_from.getName() + ";" + local_server + ":" + message;
		 * sendMessageCrossServer(message_to_send, server_list.get(o_sent_to_server), o_sent_to_server); }
		 */
	}

	// DEPRECIATED, Kept for backwards compatibility
	//public static void setIgnoreColor(final Player p_viewer, final Player p_edited) {
	//}

	/*
	 * What the actual fuck? Commenting out because useless?
	 * @param p_viewer
	 * @param p_edited
	 */
	/*public static void clearAllViewColor(final Player p_viewer, final Player p_edited) {
		//final String r_name = p_edited.getName();

		if (p_viewer.getName().equalsIgnoreCase(p_edited.getName())) {
			return;
		}

		Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(Main.plugin, new Runnable() {
			public void run() {
				// ChatColor c = ChatColor.WHITE;

				// test = (CraftPlayer)e_test.getBukkitEntity();

				EntityPlayer ent_p_edited = ((CraftPlayer) p_edited).getHandle();
				net.minecraft.server.v1_7_R2.ItemStack boots = null, legs = null, chest = null, head = null;

				if (ent_p_edited.getEquipment(1) != null) {
					boots = ent_p_edited.getEquipment(1);
				}
				if (ent_p_edited.getEquipment(2) != null) {
					legs = ent_p_edited.getEquipment(2);
				}
				if (ent_p_edited.getEquipment(3) != null) {
					chest = ent_p_edited.getEquipment(3);
				}
				if (ent_p_edited.getEquipment(4) != null) {
					head = ent_p_edited.getEquipment(4);
				}

				ent_p_edited.displayName = ChatColor.stripColor(r_name);

				((CraftPlayer) p_viewer).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(ent_p_edited));
				List<Packet> pack_list = new ArrayList<Packet>();
				if (boots != null) {
					pack_list.add(new PacketPlayOutEntityEquipment(ent_p_edited.getId(), 1, boots));
				}
				if (legs != null) {
					pack_list.add(new PacketPlayOutEntityEquipment(ent_p_edited.getId(), 2, legs));
				}
				if (chest != null) {
					pack_list.add(new PacketPlayOutEntityEquipment(ent_p_edited.getId(), 3, chest));
				}
				if (head != null) {
					pack_list.add(new PacketPlayOutEntityEquipment(ent_p_edited.getId(), 4, head));
				}

				for (Packet pa : pack_list) {
					((CraftPlayer) p_viewer).getHandle().playerConnection.sendPacket(pa);
				}

			}
		}, 2L);
	}*/

	/**
	 * Handles switching legacy trade toggle, sends toggle message, and sends cross server online message
	 * @param e Bukkit join event
	 */
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(final PlayerJoinEvent e) {
		Player p = e.getPlayer();

		PlayerManager.getPlayerModel(p).setLocalConfirmedBuddies(new ArrayList<UUID>());
		PlayerManager.getPlayerModel(p).setLocalConfirmedIgnores(new ArrayList<UUID>());

		Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(Main.plugin, new Runnable() {
			public void run() {

				Player p = e.getPlayer();

				if (p == null) {
					return;
				}

				if (PlayerManager.getPlayerModel(p).getToggleList() != null && PlayerManager.getPlayerModel(p).getToggleList().size() > 0) {
					if (PlayerManager.getPlayerModel(p).getToggleList().contains("tradechat")) {
						List<String> toggles = PlayerManager.getPlayerModel(p).getToggleList();
						toggles.remove("tradechat");
						toggles.add("tchat");
						PlayerManager.getPlayerModel(p).setToggleList(toggles);
					}

					p.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC.toString() + "     To manage your gameplay settings, use " + ChatColor.YELLOW
							+ ChatColor.UNDERLINE + "/toggles");
					p.sendMessage("");
				} else {
					PlayerManager.getPlayerModel(p).setToggleList(new ArrayList<String>());
				}

				updateCommBook(p);

				if (PlayerManager.getPlayerModel(p).getBuddyList() != null) {
					List<Object> data = new ArrayList<Object>();
					data.add("[sq_online]" + p.getName() + ":" + Bukkit.getServer().getMotd().substring(0, Bukkit.getServer().getMotd().indexOf(" ")));
					data.add(null);
					data.add(true);
					CommunityMechanics.social_query_list.put(p.getUniqueId().toString(), data);

					// sendPacketCrossServer("[sq_online]" + p.getName() + ":" +
					// getServer().getMotd().substring(0,
					// getServer().getMotd().indexOf(" ")), 0, true); // Will
					// tell all buddy's on all servers he's online.
					if (PlayerManager.getPlayerModel(p).getBuddyList() == null) {
						return; // They
						// logged
						// out.
						// Delay
						// from
						// sendPacketCrossServer.
					}
					
					for (UUID s : PlayerManager.getPlayerModel(p).getBuddyList()) {
						if (Bukkit.getPlayer(s) != null) {
							Player local_bud = Bukkit.getPlayer(s);
							if (socialQuery(p.getUniqueId(), local_bud.getUniqueId(), "CHECK_BUD")) {
								local_bud.sendMessage(ChatColor.YELLOW + p.getName() + " has joined this server.");
								local_bud.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2F, 1.2F);
								updateCommBook(local_bud);
							}
						}
					}
				}

			}
		}, 2L);

	}

	/**
	 * Updates the book in player's inventory
	 * @param p Player to update book for
	 */
	public static void updateCommBook(Player p) {
		int slot = -1;
		boolean book = false;
		for (ItemStack is : p.getInventory().getContents()) {
			slot++;
			is = p.getInventory().getItem(slot);
			if (is != null && isSocialBook(is)) {
				book = true;
				break; // Slot is what it should be.
			}
		}

		if (book == true) {
			p.getInventory().setItem(slot, generateCommBook(p));
		}

		if (book == false) {
			if (p.getInventory().getItem(8) == null || p.getInventory().getItem(8).getType() == Material.AIR) {
				p.getInventory().setItem(8, generateCommBook(p));
			} else {
				if (p.getInventory().firstEmpty() != -1) {
					p.getInventory().setItem(p.getInventory().firstEmpty(), generateCommBook(p));
				}
			}
		}
		p.updateInventory();
	}

	/**
	 * Handles sending offline packet and notifying local buddies of the quit event
	 * @param e Bukkit quit event
	 */
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		final Player p = e.getPlayer();
		if (PlayerManager.getPlayerModel(p).getBuddyList() != null) {
			final List<UUID> lbuddy_list = PlayerManager.getPlayerModel(p).getBuddyList();

			Bukkit.getScheduler().runTaskLaterAsynchronously(Main.plugin, new Runnable(){
				public void run() {
					List<Object> data = new ArrayList<Object>();
					data.add("[sq_offline]" + p.getName() + ":" + Bukkit.getServer().getMotd().substring(0, Bukkit.getServer().getMotd().indexOf(" ")));
					data.add(null);
					data.add(true);
					CommunityMechanics.social_query_list.put(p.getUniqueId().toString(), data);
					
					for (UUID s : lbuddy_list) {
						if (Bukkit.getPlayer(s) != null) {
							final Player local_bud = Bukkit.getPlayer(s);
							if (socialQuery(p.getUniqueId(), local_bud.getUniqueId(), "CHECK_BUD")) {
								local_bud.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2F, 0.5F);
								local_bud.sendMessage(ChatColor.YELLOW + p.getName() + " has logged out.");
								updateCommBook(local_bud);
							}
						}
					}
				}
			}, 5L);
		}
	}

	/**
	 * Cancels damage event when item in hand is a social book
	 * @param e Bukkit entity damage by entity event
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			Player damager = (Player) e.getDamager();
			if (isSocialBook(damager.getInventory().getItemInMainHand())) {
				e.setCancelled(true);
				e.setDamage(0);
			}
		}
	}

	/**
	 * Handle clicking on player with book and updating the book when it is opened
	 * @param e Bukkit interact event
	 */
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.hasItem() && e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			final Player p = e.getPlayer();
			if (isSocialBook(e.getItem())) {
				if (PlayerManager.getPlayerModel(p).getLastBookClick() == 0
						|| (System.currentTimeMillis() - PlayerManager.getPlayerModel(p).getLastBookClick()) > (2 * 1000)) {
					updateCombatPage(p);
					// Why close their inventory? The event is called before it so..
					// p.closeInventory();
					PlayerManager.getPlayerModel(p).setLastBookClick(System.currentTimeMillis());
				}
				p.playSound(p.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 1F, 1.2F);
				return;
			}
		}

		if (e.hasItem() && (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR)) {
			final Player p = e.getPlayer();
			if (isSocialBook(e.getItem()) && !(p.isSneaking())) {
				final Player new_bud = TradeMechanics.getTarget(p);
				if (new_bud != null) {

					if (new_bud.getPlayerListName().equalsIgnoreCase("") || new_bud.hasMetadata("NPC")) {
						return; // Not
						// a
						// real
						// player.
					}

					if (isPlayerOnIgnoreList(p, new_bud)) {
						p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + new_bud.getName() + ChatColor.YELLOW + " is currently on your IGNORE LIST.");
						p.sendMessage(ChatColor.GRAY + "Use " + ChatColor.BOLD + "/delete " + new_bud.getName() + ChatColor.GRAY
								+ " to remove them from your ignore list.");
						return;
					}

					if (PlayerManager.getPlayerModel(new_bud).getToggleList().contains("party")) {
						if (!CommunityMechanics.isPlayerOnBuddyList(new_bud, p)) {
							// They're not buddies and this player doesn't want
							// non-bud invites.
							p.sendMessage(ChatColor.RED + new_bud.getName() + " has Non-BUD party invites " + ChatColor.BOLD + "DISABLED");
							return;
						}
					}

					// PartyMechanics.inviteToParty(new_bud, p);
				}

				if (new_bud == null && !(p.isSneaking())) {
					p.sendMessage(ChatColor.GREEN + "To invite a player to your party, use the " + ChatColor.BOLD + "/pinvite" + ChatColor.GREEN
							+ " command OR " + ChatColor.UNDERLINE.toString() + "Left Click" + ChatColor.GREEN.toString()
							+ " the player with your book in hand.");
					return;
				}

			}
		}
	}

	/**
	 * Handles updating book on respawn
	 * @param e Bukkit respawn event
	 */
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		final Player p = e.getPlayer();

		new BukkitRunnable() {

			public void run() {
				if (!(Hive.server_swap.containsKey(p.getName())) && p != null) {
					updateCommBook(p);
				}
			}
		}.runTaskLaterAsynchronously(Main.plugin, 1L);

	}

	/**
	 * Handles the toggle buttons
	 * @param e Bukkit inventory click event
	 */
	@EventHandler
	public void onToggleMenuClick(InventoryClickEvent e) {
		if (e.getInventory().getTitle().equalsIgnoreCase("Toggle Menu")) {
			e.setCancelled(true);
			// No item exchange should ever happen.

			Player pl = (Player) e.getWhoClicked();
			if (isToggleButton(e.getCurrentItem())) {
				String toggle_cmd = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
				pl.performCommand(toggle_cmd.substring(1, toggle_cmd.length()));

				boolean on; // The -new- status.
				if (e.getCurrentItem().getItemMeta().getDisplayName().contains(ChatColor.RED.toString())) {
					on = true;
				} else {
					on = false;
				}

				e.setCurrentItem(generateToggleButton(toggle_cmd.substring(1, toggle_cmd.length()), on));
				pl.playSound(pl.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1F, 0.5F);
			}
			pl.updateInventory();
		}
	}

	/**
	 * Handles stopping the book from being dropped
	 * @param e Bukkit inventory click event
	 */
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (e.getSlotType() == SlotType.OUTSIDE && e.getCursor() == null) {
			return;
		}
		Player p = (Player) e.getWhoClicked();
		if (e.getCurrentItem() == null) {
			return;
		}

		if (isSocialBook(e.getCurrentItem()) && !p.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("container.crafting")) {
			e.setCancelled(true);
		}
		if (isSocialBook(e.getCursor()) && !p.getOpenInventory().getTopInventory().getName().equalsIgnoreCase("container.crafting")) {
			e.setCancelled(true);
		}
	}

	public int getServerNum() {
		return serverNum;
	}

	public void setServerNum(int serverNum) {
		this.serverNum = serverNum;
	}

}
