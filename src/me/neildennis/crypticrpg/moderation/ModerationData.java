package me.neildennis.crypticrpg.moderation;

import java.sql.ResultSet;
import java.util.UUID;

import me.neildennis.crypticrpg.player.CrypticPlayer;

public class ModerationData {
	
	@SuppressWarnings("unused")
	private CrypticPlayer pl;
	
	private boolean banned = false;
	private String banned_reason;
	private UUID banned_by;
	private String banned_by_name;
	
	private long unmuteTime = 0L;
	
	public ModerationData(CrypticPlayer pl, ResultSet data){
		this.pl = pl;
		
		try {
			banned = data.getBoolean("banned");
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean isBanned(){
		return banned;
	}
	
	public String getBannedReason(){
		return banned_reason;
	}
	
	public UUID getBannedById(){
		return banned_by;
	}
	
	public String getBannedByName(){
		return banned_by_name;
	}
	
	protected void setMuted(long unmuteTime){
		this.unmuteTime = unmuteTime;
	}
	
	public boolean isMuted(){
		return System.currentTimeMillis() >= unmuteTime;
	}

}
