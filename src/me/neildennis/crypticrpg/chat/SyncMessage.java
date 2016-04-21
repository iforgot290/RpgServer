package me.neildennis.crypticrpg.chat;

import org.bukkit.entity.Player;

public class SyncMessage {
	
	private Player pl;
	private String msg;
	
	public SyncMessage(Player pl, String msg){
		this.pl = pl;
		this.msg = msg;
	}
	
	public Player getPlayer(){
		return pl;
	}
	
	public String getMessage(){
		return msg;
	}

}
