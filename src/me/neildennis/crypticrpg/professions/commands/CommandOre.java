package me.neildennis.crypticrpg.professions.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;

import me.neildennis.crypticrpg.CrypticCommand;
import me.neildennis.crypticrpg.items.attribs.Tier;
import me.neildennis.crypticrpg.permission.Rank;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.professions.OreCluster;
import me.neildennis.crypticrpg.professions.ProfessionManager;
import me.neildennis.crypticrpg.zone.Region;

public class CommandOre extends CrypticCommand{

	private ArrayList<OreSession> sessions;
	
	public CommandOre(){
		sessions = new ArrayList<OreSession>();
	}
	
	@Override
	protected boolean sendUsage() {
		sender.sendMessage(ChatColor.RED + "Usage: /" + label + " new <region> <tier>");
		sender.sendMessage(ChatColor.RED + "Usage: /" + label + " edit");
		sender.sendMessage(ChatColor.RED + "Usage: /" + label + " save");
		return true;
	}

	@Override
	public boolean command(CrypticPlayer pl) {
		if (args.length < 1) return sendUsage();
		if (!pl.hasPermission(Rank.ADMIN)) return true;
		
		if (args[0].equalsIgnoreCase("new")){
			newOre(pl);
		}
		
		else if (args[0].equalsIgnoreCase("save")){
			saveOre(pl);
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
	
	public OreSession getOreSession(CrypticPlayer pl){
		for (OreSession session : sessions)
			if (session.getPlayer() == pl)
				return session;
		return null;
	}
	
	private boolean newOre(CrypticPlayer pl){
		if (args.length < 3) return sendUsage();
		if (getOreSession(pl) != null){
			pl.sendMessage(ChatColor.RED + "Error: You are already editing another ore cluster");
			return true;
		}
		
		Region region = ProfessionManager.getMiningRegion(args[1]);
		if (region == null){
			pl.sendMessage(ChatColor.RED + "Error: Region \"" + args[1] + "\" not found");
			return true;
		}
		
		int rawtier = Integer.parseInt(args[2]);
		if (rawtier < 1 || rawtier > 5){
			pl.sendMessage(ChatColor.RED + "Error: Invalid tier");
			return true;
		}
		
		Tier tier = Tier.fromInt(Integer.parseInt(args[2]));
		
		pl.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Creating a new T" + rawtier + " Ore Cluster");
		pl.sendMessage(ChatColor.GRAY + "Place stone brick to add to the cluster or break blocks to remove.");
		
		OreCluster cluster = new OreCluster();
		cluster.setTier(tier);
		cluster.setRegion(region);
		
		OreSession session = new OreSession(pl);
		session.setOreCluster(cluster);
		
		sessions.add(session);
		
		return true;
	}
	
	private boolean saveOre(CrypticPlayer pl){
		OreSession session = getOreSession(pl);
		if (session == null) {
			pl.sendMessage(ChatColor.RED + "You are not currently editing an ore cluster");
			return true;
		}
		
		sessions.remove(session);
		session.getOreCluster().getRegion().getWaitingOre().add(session.getOreCluster());
		//TODO actually save it
		pl.sendMessage(ChatColor.GREEN + "Saved your current ore cluster");
		return true;
	}
	
	public class OreSession{
		
		private CrypticPlayer pl;
		private OreCluster ore;
		
		public OreSession(CrypticPlayer pl){
			this.pl = pl;
		}
		
		public CrypticPlayer getPlayer(){
			return pl;
		}
		
		public void setOreCluster(OreCluster ore){
			this.ore = ore;
		}
		
		public OreCluster getOreCluster(){
			return ore;
		}
		
	}

}
