package minecade.dungeonrealms.DonationMechanics;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Listener;

import minecade.dungeonrealms.Main;
import minecade.dungeonrealms.DonationMechanics.commands.CommandAddEC;
import minecade.dungeonrealms.DonationMechanics.commands.CommandAddPet;
import minecade.dungeonrealms.DonationMechanics.commands.CommandAddShards;
import minecade.dungeonrealms.DonationMechanics.commands.CommandAddSubDaysToAll;
import minecade.dungeonrealms.DonationMechanics.commands.CommandBank;
import minecade.dungeonrealms.DonationMechanics.commands.CommandGiveSub;
import minecade.dungeonrealms.DonationMechanics.commands.CommandGiveSubLife;
import minecade.dungeonrealms.DonationMechanics.commands.CommandGiveSubPlus;
import minecade.dungeonrealms.DonationMechanics.commands.CommandRemoveSub;
import minecade.dungeonrealms.DonationMechanics.commands.CommandRemoveSubPlus;
import minecade.dungeonrealms.DonationMechanics.commands.CommandRewardSubLife;
import minecade.dungeonrealms.database.ConnectionPool;

public class DonationMechanics implements Listener {
	
	static Logger log = Logger.getLogger("Minecraft");
	
	/**
	 * UUID mapped to rank, async thread pool for setRank()
	 */
	public static volatile ConcurrentHashMap<UUID, String> async_set_rank = new ConcurrentHashMap<UUID, String>();
	
	Thread RankThread;
	
	// TODO: Fix sub++ expiring when sub/sub+ expires, prevent sub++ from showing days left in sub.
	
	public void onEnable() {
		
		Main.plugin.getCommand("bank").setExecutor(new CommandBank());
		Main.plugin.getCommand("addshards").setExecutor(new CommandAddShards());
		Main.plugin.getCommand("addec").setExecutor(new CommandAddEC());
		Main.plugin.getCommand("addpet").setExecutor(new CommandAddPet());
		Main.plugin.getCommand("givesub").setExecutor(new CommandGiveSub());
		Main.plugin.getCommand("givesublife").setExecutor(new CommandGiveSubLife());
		Main.plugin.getCommand("givesubplus").setExecutor(new CommandGiveSubPlus());
		Main.plugin.getCommand("removesub").setExecutor(new CommandRemoveSub());
		Main.plugin.getCommand("removesubplus").setExecutor(new CommandRemoveSubPlus());
		Main.plugin.getCommand("rewardsublife").setExecutor(new CommandRewardSubLife());
		Main.plugin.getCommand("addsubdaystoall").setExecutor(new CommandAddSubDaysToAll());
		
		RankThread = new RankThread();
		RankThread.start();
		
		//server_list.put(0, "69.69.69.69:696969");
		//server_list.put(1, "127.0.0.1:25645");
        //server_list.put(2, "127.0.0.1:25646");
		/*server_list.put(2, "69.69.69.69:6969");
		server_list.put(3, "69.69.69.69:6969");
		server_list.put(4, "69.69.69.69:6969"); 
		server_list.put(5, "69.69.69.69:6969");
		server_list.put(100, "69.69.69.69:6969");*/
		
		/* Do NOT uncomment!  The timer is now handled by a separate donation-dedicated server!
		if (Bukkit.getMotd().contains("US-0")) {
			Calendar nextUpdate = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
			nextUpdate.set(nextUpdate.get(Calendar.YEAR), nextUpdate.get(Calendar.MONTH), nextUpdate.get(Calendar.DATE) + 1, 0, 0);
			Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
			Long msToUp = nextUpdate.getTimeInMillis() - now.getTimeInMillis();
			Long secToUp = TimeUnit.MILLISECONDS.toSeconds(msToUp);
			log.info("[DonationMechanics] Scheduled daily donation duties to run in "
					+ String.format(
							"%d hours, %d minutes, %d seconds",
							TimeUnit.MILLISECONDS.toHours(msToUp),
							TimeUnit.MILLISECONDS.toMinutes(msToUp)
									- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(msToUp)), secToUp
									- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(msToUp))));

			new BukkitRunnable() {

				
				public void run() {
					log.info("[DonationMechanics] Time is 12:00:00 AM ES/DT.  Running daily donation duties.");
					tickSubscriberDays();
					log.info("[DonationMechanics] Ticked all user's subscriber days forward by ONE.");
					
					tickFreeEcash();
					log.info("[DonationMechanics] Reset all 'Free E-cash' users, login for more e-cash!");
					
					tickLifetimeSubEcash();
					log.info("[DonationMechanics] 999 E-CASH has been given to all sub++ users.");
				}
				
			}.runTaskLaterAsynchronously(Main.plugin, secToUp * 20L);
			
		}
		*/
			
		if(new File("plugins/Votifier.jar").exists()) {
			log.info("[DonationMechanics] Votifier.jar detected, registering listener.");
			Bukkit.getServer().getPluginManager().registerEvents(new CustomEventListener(this), Main.plugin);
		}
		
	}
	
	/**
	 * Gets player rank from sql server
	 * @param id id to get rank for
	 * @return the player's rank
	 */
	public static String getRank(UUID id) {
		
		Connection con = null;
		PreparedStatement pst = null;
		
		try {
			con = ConnectionPool.getConnection();
			pst = con.prepareStatement("SELECT rank FROM player_database WHERE p_name = '" + id.toString() + "'");
			
			pst.execute();
			ResultSet rs = pst.getResultSet();
			
			if(!rs.next()) { return "default"; }
			
			final String rank = rs.getString("rank");
			
			if(rank == null || rank.equalsIgnoreCase("null")) { return "default"; }
			
			return rank;
			
		} catch(SQLException ex) {
			log.log(Level.SEVERE, ex.getMessage(), ex);
			return "default";
			
		} finally {
			try {
				if(pst != null) {
					pst.close();
				}
				
			} catch(SQLException ex) {
				log.log(Level.WARNING, ex.getMessage(), ex);
				return "default";
			}
		}
	}
	
	/**
	 * Gets player rank from sql server
	 * @param player player to get rank for
	 * @return the player's rank
	 */
	public static String getRank(OfflinePlayer player){
		return getRank(player.getUniqueId());
	}
	
	/**
	 * Adds sub days to all players who have sub
	 * @param number number of days to add
	 */
	public static void addSubscriberDaysToAll(int number) {
		Connection con = null;
		PreparedStatement pst = null;
		
		try {
			con = ConnectionPool.getConnection();
			pst = con.prepareStatement("UPDATE player_database SET player_database.sdays_left = player_database.sdays_left + ? WHERE player_database.sdays_left IS NOT NULL");
			pst.setInt(1, number);
			pst.executeUpdate();
			
		} catch(SQLException ex) {
			log.log(Level.SEVERE, ex.getMessage(), ex);
			
		} finally {
			try {
				if(pst != null) {
					pst.close();
				}
				if(con != null) {
					con.close();
				}
				
			} catch(SQLException ex) {
				log.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
	}
	
	/**
	 * Ticks down everyone's sub days
	 */
	public static void tickSubscriberDays() {
		Connection con = null;
		PreparedStatement pst = null;
		
		try {
			con = ConnectionPool.getConnection();
			pst = con.prepareStatement("UPDATE player_database SET player_database.sdays_left=player_database.sdays_left-1 WHERE player_database.sdays_left IS NOT NULL");
			
			pst.executeUpdate();
			
		} catch(SQLException ex) {
			log.log(Level.SEVERE, ex.getMessage(), ex);
			
		} finally {
			try {
				if(pst != null) {
					pst.close();
				}
				if(con != null) {
					con.close();
				}
				
			} catch(SQLException ex) {
				log.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
	}
	
	/**
	 * Gives lifetime subs their ecash
	 */
	public static void tickLifetimeSubEcash() {
		Connection con = null;
		PreparedStatement pst = null;
		
		try {
			con = ConnectionPool.getConnection();
			pst = con.prepareStatement("UPDATE player_database SET player_database.ecash=player_database.ecash+999 WHERE player_database.rank='sub++'");
			
			pst.executeUpdate();
			
		} catch(SQLException ex) {
			log.log(Level.SEVERE, ex.getMessage(), ex);
			
		} finally {
			try {
				if(pst != null) {
					pst.close();
				}
				if(con != null) {
					con.close();
				}
				
			} catch(SQLException ex) {
				log.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
	}

	/**
	 * Resets login bonus
	 */
	public static void tickFreeEcash() {
		Connection con = null;
		PreparedStatement pst = null;
		
		try {
			con = ConnectionPool.getConnection();
			pst = con.prepareStatement("UPDATE player_database SET player_database.online_today=0 WHERE player_database.online_today=1");
			
			pst.executeUpdate();
			
		} catch(SQLException ex) {
			log.log(Level.SEVERE, ex.getMessage(), ex);
			
		} finally {
			try {
				if(pst != null) {
					pst.close();
				}
				if(con != null) {
					con.close();
				}
				
			} catch(SQLException ex) {
				log.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
	}

	/**
	 * Adds a pet to player
	 * @param id id of player to add pet to
	 * @param pet pet to add
	 */
	public static void addPetToPlayer(UUID id, String pet) {
		List<String> pet_list = downloadPetData(id);
		String pet_string = "";
		
		for(String s : pet_list) {
			pet_string = pet_string + s + ",";
		}
		
		if(pet_list.contains(pet)) { return; // Already in list.
		}
		
		pet_string = pet_string + pet + ",";
		if(pet_string.endsWith(",")) {
			pet_string = pet_string.substring(0, pet_string.length() - 1);
		}
		pet_string = pet_string.replaceAll(",,", ",");
		
		try {
			Connection con = ConnectionPool.getConnection();
			PreparedStatement pst = con.prepareStatement("INSERT INTO player_database (p_name, pets)" + " VALUES" + "('" + id.toString() + "', '" + pet_string + "') ON DUPLICATE KEY UPDATE pets = '" + pet_string + "'");
			
			pst.executeUpdate();
			
			if(pst != null) {
				pst.close();
			}
			
			if(con != null) {
				pst.close();
			}
			
		} catch(SQLException ex) {
			log.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}
	
	/**
	 * Adds a pet to player
	 * @param player player to add pet to
	 * @param pet pet to add
	 */
	public static void addPetToPlayer(OfflinePlayer player, String pet){
		addPetToPlayer(player.getUniqueId(), pet);
	}
	
	/**
	 * Get sub days for player
	 * @param id id of player to get days for
	 * @return number of days left in subscription
	 */
	public static int getSubscriberDays(UUID id) {
		Connection con = null;
		PreparedStatement pst = null;
		
		try {
			con = ConnectionPool.getConnection();
			pst = con.prepareStatement("SELECT sdays_left FROM player_database WHERE p_name = '" + id.toString() + "'");
			
			pst.execute();
			ResultSet rs = pst.getResultSet();
			if(!rs.next()) { return 0; }
			int days_left = rs.getInt("sdays_left");
			return days_left;
			
		} catch(SQLException ex) {
			log.log(Level.SEVERE, ex.getMessage(), ex);
			
		} finally {
			try {
				if(pst != null) {
					pst.close();
				}
				
			} catch(SQLException ex) {
				log.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		
		return 0;
	}

	/**
	 * Get sub days for player
	 * @param player player to get days for
	 * @return number of days left in subscription
	 */
	public static int getSubscriberDays(OfflinePlayer player){
		return getSubscriberDays(player.getUniqueId());
	}
	
	/**
	 * Add sub days to a player
	 * @param id id of player to add days to
	 * @param days_to_add number of days to add
	 * @param set true to overwrite, false to add to current days
	 */
	public static void addSubscriberDays(UUID id, int days_to_add, boolean set) {
		Connection con = null;
		PreparedStatement pst = null;
		
		if(!(set)) {
			int current_days = getSubscriberDays(id);
			days_to_add += current_days;
		}
		
		try {
			con = ConnectionPool.getConnection();
			pst = con.prepareStatement("INSERT INTO player_database (p_name, sdays_left)" + " VALUES" + "('" + id.toString() + "', '" + days_to_add + "') ON DUPLICATE KEY UPDATE sdays_left ='" + days_to_add + "'");
			
			pst.executeUpdate();
			log.info("[DonationMechanics] Set " + id.toString() + "'s REMAINING SUBSCRIBER DAYS to " + days_to_add);
			
		} catch(SQLException ex) {
			log.log(Level.SEVERE, ex.getMessage(), ex);
			
		} finally {
			try {
				if(pst != null) {
					pst.close();
				}
				if(con != null) {
					con.close();
				}
				
			} catch(SQLException ex) {
				log.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
	}
	
	/**
	 * Add sub days to a player
	 * @param player player to add days to
	 * @param days_to_add number of days to add
	 * @param set true to overwrite, false to add to current days
	 */
	public static void addSubscriberDays(OfflinePlayer player, int days_to_add, boolean set){
		addSubscriberDays(player.getUniqueId(), days_to_add, set);
	}
	
	/**
	 * Downloads the pet data for a player
	 * @param id uuid of the player to download pet data for
	 * @return pet data for specified player
	 */
	public static List<String> downloadPetData(UUID id) {
		Connection con = null;
		PreparedStatement pst = null;
		List<String> pet_data = new ArrayList<String>();
		
		try {
			con = ConnectionPool.getConnection();
			pst = con.prepareStatement("SELECT pets FROM player_database WHERE p_name = '" + id.toString() + "'");
			
			pst.execute();
			ResultSet rs = pst.getResultSet();
			if(!rs.next()) { return pet_data; }
			String pet_list = rs.getString("pets");
			if(pet_list != null && pet_list.contains(",")) {
				for(String s : pet_list.split(",")) {
					pet_data.add(s);
				}
			} else if(pet_list != null) {
				if(pet_list.length() > 0) {
					pet_data.add(pet_list);
				}
			}
			return pet_data;
			
		} catch(SQLException ex) {
			log.log(Level.SEVERE, ex.getMessage(), ex);
			
		} finally {
			try {
				if(pst != null) {
					pst.close();
				}
				
			} catch(SQLException ex) {
				log.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		
		return pet_data;
	}

	/**
	 * Downloads the pet data for a player
	 * @param player player to download pet data for
	 * @return pet data for specified player
	 */
	public static List<String> downloadPetData(OfflinePlayer player){
		return downloadPetData(player.getUniqueId());
	}
	
	/**
	 * Sets a players rank, adds it to the async pool to be updated
	 * @param id id of player to set rank for
	 * @param rank rank to set player to
	 */
	public static void setRank(UUID id, String rank) {
		async_set_rank.put(id, rank);
	}
	
	/**
	 * Sets a players rank, adds it to the async pool to be updated
	 * @param player player to set rank for
	 * @param rank rank to set player to
	 */
	public static void setRank(OfflinePlayer player, String rank){
		setRank(player.getUniqueId(), rank);
	}
	
	/**
	 * Downloads ecash for a player
	 * @param id uuid of player to get ecash for
	 * @return amount of ecash the player has
	 */
	public static int downloadECASH(UUID id){
		Connection con = null;
		PreparedStatement pst = null;
		
		try {
			con = ConnectionPool.getConnection();
			pst = con.prepareStatement("SELECT ecash FROM player_database WHERE p_name = '" + id.toString() + "'");
			
			pst.execute();
			ResultSet rs = pst.getResultSet();
			if(!rs.next()) { return 0; }
			int amount = rs.getInt("ecash");
			return amount;
			
		} catch(SQLException ex) {
			log.log(Level.SEVERE, ex.getMessage(), ex);
			
		} finally {
			try {
				if(pst != null) {
					pst.close();
				}
				
			} catch(SQLException ex) {
				log.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
		
		return 0;
	}
	
	/**
	 * Downloads ecash for a player
	 * @param player player to get ecash for
	 * @return amount of ecash the player has
	 */
	public static int downloadECASH(OfflinePlayer player) {
		return downloadECASH(player.getUniqueId());
	}
	
	/**
	 * Set player ecash on sql server
	 * @param id id of player to set ecash for
	 * @param amount amount of ecash to set
	 */
	public static void setECASH_SQL(UUID id, int amount){
		Connection con = null;
		PreparedStatement pst = null;
		
		try {
			con = ConnectionPool.getConnection();
			pst = con.prepareStatement("INSERT INTO player_database (p_name, ecash)" + " VALUES" + "('" + id.toString() + "', '" + amount + "') ON DUPLICATE KEY UPDATE ecash ='" + amount + "'");
			
			pst.executeUpdate();
			log.info("[DonationMechanics] Set " + id.toString() + "'s ECASH to " + amount);
			
		} catch(SQLException ex) {
			log.log(Level.SEVERE, ex.getMessage(), ex);
			
		} finally {
			try {
				if(pst != null) {
					pst.close();
				}
				if(con != null) {
					con.close();
				}
				
			} catch(SQLException ex) {
				log.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
	}

	/**
	 * Set player ecash on sql server
	 * @param player player to set ecash for
	 * @param amount amount of ecash to set
	 */
	public static void setECASH_SQL(OfflinePlayer player, int amount) {
		setECASH_SQL(player.getUniqueId(), amount);
	}
	
	/*public static void sendPacketCrossServer(String packet_data, int server_num, boolean all_servers) {
		Socket kkSocket = null;
		PrintWriter out = null;
		
		if(all_servers) {
			for(int sn : server_list.keySet()) {
			    String ipAndPort = CommunityMechanics.server_list.get(sn);
                String ipNoPort = ipAndPort.contains(":") ? CommunityMechanics.server_list.get(sn).split(":")[0]
                        : ipAndPort;
                int port = ipAndPort.contains(":") && StringUtils.isNumeric(ipAndPort.split(":")[1]) ? Integer.parseInt(ipAndPort
                        .split(":")[1]) : Config.transfer_port;
				// Send it anyway, fix for not getting insta-ecash.
				//if(server_ip.equalsIgnoreCase(local_ip)){
				//	continue; // Don't send to same server.
				//}
				try {
					kkSocket = new Socket();
					//kkSocket.bind(new InetSocketAddress(local_IP, transfer_port+1));
					kkSocket.connect(new InetSocketAddress(ipNoPort, port), 250);
					out = new PrintWriter(kkSocket.getOutputStream(), true);
					
					out.println(packet_data);
					
				} catch(IOException e) {
					if(out != null) {
						out.close();
					}
					continue;
				}
				
				if(out != null) {
					out.close();
				}
			}
		} else if(!all_servers) {
			try {
			    String ipAndPort = CommunityMechanics.server_list.get(server_num);
                String ipNoPort = ipAndPort.contains(":") ? CommunityMechanics.server_list.get(server_num).split(":")[0]
                        : ipAndPort;
                int port = ipAndPort.contains(":") && StringUtils.isNumeric(ipAndPort.split(":")[1]) ? Integer.parseInt(ipAndPort
                        .split(":")[1]) : Config.transfer_port;
				
				kkSocket = new Socket();
				//kkSocket.bind(new InetSocketAddress(local_IP, transfer_port+1));
				kkSocket.connect(new InetSocketAddress(ipNoPort, port), 250);
				out = new PrintWriter(kkSocket.getOutputStream(), true);
				
				out.println(packet_data);
				
			} catch(IOException e) {
				
			} finally {
				if(out != null) {
					out.close();
				}
			}
			
			if(out != null) {
				out.close();
			}
		}
		
	}*/
	
}