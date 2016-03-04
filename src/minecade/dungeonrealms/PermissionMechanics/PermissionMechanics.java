package minecade.dungeonrealms.PermissionMechanics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import minecade.dungeonrealms.Main;
import minecade.dungeonrealms.CommunityMechanics.CommunityMechanics;
import minecade.dungeonrealms.Hive.Hive;
import minecade.dungeonrealms.PermissionMechanics.commands.CommandGMHelp;
import minecade.dungeonrealms.PermissionMechanics.commands.CommandPMHelp;
import minecade.dungeonrealms.PermissionMechanics.commands.CommandSetRank;
import minecade.dungeonrealms.database.ConnectionPool;

public class PermissionMechanics implements Listener {
	static Logger log = Logger.getLogger("Minecraft");
	
	/**
	 * Rank map UUID mapped to rank
	 */
	public static HashMap<UUID, String> rank_map = new HashMap<UUID, String>();
	
	public static HashMap<String, Integer> rank_forumgroup = new HashMap<String, Integer>();
	
	// Rank name, Forum group ID.
	
	public void onEnable() {
		Main.plugin.getServer().getPluginManager().registerEvents(this, Main.plugin);
		
		Main.plugin.getCommand("setrank").setExecutor(new CommandSetRank());
		Main.plugin.getCommand("gmhelp").setExecutor(new CommandGMHelp());
		Main.plugin.getCommand("pmhelp").setExecutor(new CommandPMHelp());
		
		rank_forumgroup.put("default", 2);
		rank_forumgroup.put("pmod", 11);
		rank_forumgroup.put("sub", 75);
		rank_forumgroup.put("yt", 75);
		rank_forumgroup.put("sub+", 76);
		rank_forumgroup.put("sub++", 79);
		rank_forumgroup.put("gm", 72);
		rank_forumgroup.put("wd", 72);
		
		log.info("[PermissionMechanics] has been enabled.");
	}
	
	public void onDisable() {
		log.info("[PermissionMechanics] has been disabled.");
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(final PlayerJoinEvent e) {
		Main.plugin.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			public void run() {
				
				final Player p = e.getPlayer();
				String p_name = p.getName();
				if(p_name.length() > 13) {
					p_name = p_name.substring(0, 13);
				}
				if(!(rank_map.containsKey(p.getUniqueId()))) { return; }
				final String rank = getRank(p.getUniqueId());
				boolean set = false;
				
				if(rank.equalsIgnoreCase("pmod")) {
					p.setPlayerListName(ChatColor.WHITE.toString() + p_name);
					set = true;
				}
				if(p.isOp() || rank.equalsIgnoreCase("gm")) {
					p.setPlayerListName(ChatColor.AQUA.toString() + p_name);
					set = true;
				}
				if(set == false) {
					p.setPlayerListName(ChatColor.GRAY.toString() + p_name);
				}
			}
		}, 10L);
	}
	
	public static void downloadRank(UUID p_uuid) {
		Connection con = null;
		PreparedStatement pst = null;
		
		try {
			pst = ConnectionPool.getConnection().prepareStatement("SELECT rank FROM player_database WHERE p_name = '" + p_uuid.toString() + "'");
			
			pst.execute();
			ResultSet rs = pst.getResultSet();
			if(!rs.next()) {
				setRank(p_uuid, "default", true);
				return;
			}
			String rank = rs.getString("rank");
			setRank(p_uuid, rank, false);
			
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
	
	public static void uploadRank(final UUID p_uuid) {
		
		String rank = getRank(p_uuid);
		
		Hive.sql_query.add("INSERT INTO player_database (p_name, rank)" + " VALUES" + "('" + p_uuid + "', '" + rank + "') ON DUPLICATE KEY UPDATE rank ='" + rank + "'");
		
	}

	public static void setRank(UUID p_uuid, String rank, boolean upload_sql) {
		Player p = null;
		if(Bukkit.getPlayer(p_uuid) != null && Bukkit.getPlayer(p_uuid).isOnline()) {
			p = Bukkit.getPlayer(p_uuid);
		}
		
		if(rank == null) {
			rank = "default";
		}
		
		if(rank_map.containsKey(p_uuid)) {
			String current_rank = rank_map.get(p_uuid);
			if(current_rank == null) {
				current_rank = "default";
			}
			if(current_rank != null) {
				if((current_rank.equalsIgnoreCase("pmod") && !rank.equalsIgnoreCase("gm") && !rank.equalsIgnoreCase("default")) || current_rank.equalsIgnoreCase("gm") && !rank.equalsIgnoreCase("default")) {
					// Not demote, not promote, ignore sub.
					return;
				} else {
					rank_map.put(p_uuid, rank);
				}
			}
		} else {
			rank_map.put(p_uuid, rank);
		}
		
		if(upload_sql) {
			uploadRank(p_uuid);
		}
		
		if(p != null) {
			String format_rank = rank;
			if(rank.equalsIgnoreCase("sub")) {
				format_rank = ChatColor.GREEN + "Subscriber";
			}
			if(rank.equalsIgnoreCase("sub+")) {
				format_rank = ChatColor.GOLD + "Subscriber+";
			}
			if(rank.equalsIgnoreCase("sub++")) {
				format_rank = ChatColor.DARK_AQUA + "Subscriber++";
			}
			if(rank.equalsIgnoreCase("pmod")) {
				format_rank = ChatColor.WHITE + "Player Moderator";
			}
			if(rank.equalsIgnoreCase("gm")) {
				format_rank = ChatColor.AQUA + "Game Master";
			}
			if(rank.equalsIgnoreCase("wd")) {
				format_rank = ChatColor.AQUA + "World Designer";
			}
			if(rank.equalsIgnoreCase("yt")) {
				format_rank = ChatColor.WHITE + "" +ChatColor.BOLD + "You" + ChatColor.RED + ChatColor.BOLD + "Tube";
			}
			
			p.sendMessage("");
			p.sendMessage(ChatColor.YELLOW + "" + "Your Dungeon Realms rank has changed to '" + ChatColor.BOLD + format_rank + ChatColor.YELLOW + "'");
			p.sendMessage("");
		}
		
		log.info("[PermissionMechanics] Set " + p_uuid + "'s RANK to " + rank);
	}
	
	public static String getRank(OfflinePlayer player){
		return getRank(player.getUniqueId());
	}
	
	public static String getRank(UUID p_uuid) {
		
		if(!(rank_map.containsKey(p_uuid))) {
			PreparedStatement pst = null;
			
			try {
				pst = ConnectionPool.getConnection().prepareStatement("SELECT rank FROM player_database WHERE p_name = '" + p_uuid + "'");
				
				pst.execute();
				ResultSet rs = pst.getResultSet();
				
				if(!rs.next()) { return "default"; }
				
				final String rank = rs.getString("rank");
				final UUID fp_uuid = p_uuid;
				
				if(rank == null || rank.equalsIgnoreCase("null")) { return "default"; }
				
				if(rank != null) {
					// Send rank cross-server so it's locally saved.
					List<Object> qdata = new ArrayList<Object>();
					qdata.add("[rank_map]" + fp_uuid.toString() + ":" + rank);
					qdata.add(null);
					qdata.add(true);
					CommunityMechanics.social_query_list.put(p_uuid.toString(), qdata);
					//CommunityMechanics.sendPacketCrossServer("[rank_map]" + fp_name + ":" + rank, -1, true);
				}
				
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
		
		return rank_map.get(p_uuid);
	}
	
	public static boolean isStaff(Player pl) {
		String rank = getRank(pl.getUniqueId());
		if (!pl.isOp() && !rank.equalsIgnoreCase("gm") && !rank.equalsIgnoreCase("pmod")) {
			return false;
		}
		return true;
 	}
	
	public boolean isPMOD(Player pl) {
		String rank = getRank(pl.getUniqueId());
		return rank.equalsIgnoreCase("GM") || rank.equalsIgnoreCase("PMOD") ? true : false;
	}
	
	public static boolean isGM(Player pl) {
		String rank = getRank(pl.getUniqueId());
		return rank.equalsIgnoreCase("GM");
	}
	
}
