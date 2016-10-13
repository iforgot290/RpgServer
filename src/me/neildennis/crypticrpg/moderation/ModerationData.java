package me.neildennis.crypticrpg.moderation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import me.neildennis.crypticrpg.cloud.data.PlayerData;

public class ModerationData {
	
	private long unmuteTime = 0L;
	
	private ArrayList<Ban> bans;
	
	public ModerationData(UUID id) throws SQLException{
		this(id, PlayerData.getBans(id));
	}
	
	public ModerationData(UUID id, ResultSet data) throws SQLException{
		bans = new ArrayList<Ban>();
		
		while (data.next()){
			UUID enforcer = UUID.fromString(data.getString("enforcer_uuid"));
			String reason = data.getString("reason");
			boolean banned = data.getBoolean("valid");
			long banTime = data.getLong("ban_time");
			long unbanTime = data.getLong("unban_time");
			
			bans.add(new Ban(enforcer, reason, banned, banTime, unbanTime));
		}
	}
	
	public Ban getCurrentBan(){
		for (Ban ban : bans)
			if (ban.isBanned()) return ban;
		return null;
	}
	
	public ArrayList<Ban> getBans(){
		return bans;
	}
	
	protected void setMuted(long unmuteTime){
		this.unmuteTime = unmuteTime;
	}
	
	public boolean isMuted(){
		return System.currentTimeMillis() <= unmuteTime;
	}
	
	public class Ban{
		
		private OfflinePlayer banner;
		private UUID bannerid;
		private String reason;
		private boolean banned;
		
		private long banTime;
		private long unbanTime;
		
		public Ban(UUID bannerid, String reason, boolean banned, long banTime, long unbanTime){
			this.bannerid = bannerid;
			this.reason = reason;
			this.banned = banned;
			
			this.banTime = banTime;
			this.unbanTime = unbanTime;
		}
		
		public OfflinePlayer getBanner(){
			if (banner != null) return banner;
			
			return (banner = Bukkit.getOfflinePlayer(bannerid));
		}
		
		public String getBannerName(){
			return getBanner().getName();
		}
		
		public String getReason(){
			return reason;
		}
		
		public long getBanTime(){
			return banTime;
		}
		
		public long getUnbanTime(){
			return unbanTime;
		}
		
		public long getTimeRemaining(){
			long timeleft = unbanTime - System.currentTimeMillis();
			return timeleft > 0 ? timeleft : 0;
		}
		
		public boolean isBanned(){
			return isTempBan() ? System.currentTimeMillis() > unbanTime : banned;
		}
		
		public boolean isTempBan(){
			return unbanTime != 0;
		}
		
	}

}
