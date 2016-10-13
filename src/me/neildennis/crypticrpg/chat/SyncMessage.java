package me.neildennis.crypticrpg.chat;

import org.bukkit.command.CommandSender;

public class SyncMessage {
	
	private CommandSender pl;
	private Object msg;
	
	public SyncMessage(CommandSender pl, Object msg){
		this.pl = pl;
		this.msg = msg;
	}
	
	public CommandSender getPlayer(){
		return pl;
	}
	
	public Object getMessage(){
		return msg;
	}

}
