package me.neildennis.crypticrpg.professions.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;

import me.neildennis.crypticrpg.CrypticCommand;
import me.neildennis.crypticrpg.items.attribs.Tier;
import me.neildennis.crypticrpg.permission.Rank;
import me.neildennis.crypticrpg.player.CrypticPlayer;

public class CommandOre extends CrypticCommand{

	private ArrayList<OreSession> sessions;
	
	public CommandOre(){
		sessions = new ArrayList<OreSession>();
	}
	
	@Override
	protected boolean sendUsage() {
		sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <tier>");
		sender.sendMessage(ChatColor.RED + "Usage: /" + label + " remove");
		sender.sendMessage(ChatColor.RED + "Usage: /" + label + " off");
		return true;
	}

	@Override
	public boolean command(CrypticPlayer pl) {
		if (!pl.hasPermission(Rank.ADMIN)) return true;
		if (args.length != 1) return sendUsage();
		
		if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")) {
			removeOreSession(pl);
			Tier tier = Tier.ONE;
			OreSession session = new OreSession(pl, tier);
			sessions.add(session);
			pl.sendMessage(ChatColor.GREEN + "Now removing ore");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("off")) {
			
			for (OreSession session : sessions) {
				if (session.getPlayer() == pl) {
					sessions.remove(session);
					pl.sendMessage(ChatColor.GREEN + "No longer placing/removing ore");
					return true;
				}
			}
			
			pl.sendMessage(ChatColor.RED + "You weren't placing ore");
			return true;
		}
		
		try {
			removeOreSession(pl);
			Tier tier = Tier.fromInt(Integer.parseInt(args[0]));
			OreSession session = new OreSession(pl, tier);
			sessions.add(session);
			pl.sendMessage(ChatColor.GREEN + "Now placing Tier " + ChatColor.BLUE + tier.toString() + ChatColor.GREEN + " ore");
		} catch (Exception e) {
			return sendUsage();
		}
		
		return true;
	}

	@Override
	public boolean console() {
		return true;
	}
	
	public ArrayList<OreSession> getOreSessions() {
		return sessions;
	}
	
	private boolean removeOreSession(CrypticPlayer pl) {
		for (OreSession session : sessions) {
			if (session.getPlayer() == pl) {
				sessions.remove(session);
				return true;
			}
		}
		
		return false;
	}
	
	public OreSession getOreSession(CrypticPlayer pl){
		for (OreSession session : sessions)
			if (session.getPlayer() == pl)
				return session;
		return null;
	}
	
	public class OreSession{
		
		private CrypticPlayer pl;
		private Tier tier;
		
		public OreSession(CrypticPlayer pl, Tier tier){
			this.pl = pl;
			this.tier = tier;
		}
		
		public CrypticPlayer getPlayer(){
			return pl;
		}
		
		public void setTier(Tier tier) {
			this.tier = tier;
		}
		
		public Tier getTier() {
			return tier;
		}
		
	}

}
