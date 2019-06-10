package me.neildennis.crypticrpg.professions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.Manager;
import me.neildennis.crypticrpg.cloud.CloudManager;
import me.neildennis.crypticrpg.items.attribs.Tier;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.professions.commands.CommandOre;
import me.neildennis.crypticrpg.professions.commands.CommandOre.OreSession;
import me.neildennis.crypticrpg.utils.Log;

public class ProfessionManager extends Manager{

	private static CommandOre oreCmd;
	
	private HashMap<Location, OreCluster> oreLocs;
	private PriorityQueue<OreCluster> oreRespawn;

	@Override
	public void onEnable() {
		
		oreLocs = new HashMap<Location, OreCluster>();
		int oreAmt = 11;
		
		try {
			oreAmt = loadOres();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		oreRespawn = new PriorityQueue<OreCluster>(oreAmt, (OreCluster c1, OreCluster c2) -> {return (int) (c1.getRespawnTime() - c2.getRespawnTime());});
		registerTasks();
		
		Cryptic.registerEvents(new MiningListener(this));
		Cryptic.registerCommand("ore", (oreCmd = new CommandOre()));
	}
	
	@Override
	public void onDisable() {
		
	}

	@Override
	public void registerTasks() {
		tasks.add(Bukkit.getScheduler().runTaskTimer(Cryptic.getPlugin(), 
				() -> tickOres(), 25, 20));
	}
	
	public HashMap<Location, OreCluster> getOreLocations() {
		return oreLocs;
	}

	public int loadOres() throws SQLException{
		ResultSet data = CloudManager.sendQuery("SELECT * FROM ore_locations");
		
		int loaded = 0;
		int total = data.getFetchSize();

		
		while (data.next()){

			UUID id = UUID.fromString(data.getString("loc_uuid"));
			Tier tier = Tier.fromInt(data.getInt("tier"));
			Location loc = new Location(Bukkit.getWorld(data.getString("world")), data.getInt("x"), data.getInt("y"), data.getInt("z"));
			Material destroyed = Material.valueOf(data.getString("destroyed"));

			OreCluster cluster = new OreCluster(id, tier, loc, destroyed);

			cluster.spawn();
			oreLocs.put(loc, cluster);
			
			loaded++;
		}

		Log.info("Loaded " + loaded + "/" + total + " ore clusters");
		return loaded > 1 ? loaded : 1;
	}

	public void tickOres(){
		while (oreRespawn.peek() != null && oreRespawn.peek().getRespawnTime() < System.currentTimeMillis()) {
			OreCluster ore = oreRespawn.poll();
			ore.spawn();
		}
	}
	
	public ArrayList<OreSession> getOreSessions(){
		return oreCmd.getOreSessions();
	}
	
	public OreSession getOreSession(CrypticPlayer pl){
		return oreCmd.getOreSession(pl);
	}
	
	public void addOreRespawn(OreCluster ore) {
		ore.despawn();
		ore.setRespawnTime(System.currentTimeMillis() + 10000);
		oreRespawn.add(ore);
	}
	
	public void unregisterOre(OreCluster ore) {
		oreRespawn.remove(ore);
		oreLocs.remove(ore.getLocation());
		
		CloudManager.sendStatementAsync("DELETE FROM `ore_locations` WHERE `loc_uuid` = '" + ore.getUUID().toString() + "'");
	}
	
	public void registerOre(Block block, OreSession session) {
		OreCluster cluster = new OreCluster(UUID.randomUUID(), session.getTier(), block.getLocation(), block.getType());
		oreLocs.put(block.getLocation(), cluster);
		
		CloudManager.sendStatementAsync("INSERT INTO `ore_locations` (`loc_uuid`, `tier`, `destroyed`, `world`, `x`, `y`, `z`) "
				+ "VALUES ('" + cluster.getUUID().toString() + "', '" + session.getTier().getRawInteger()
				+ "', '" + cluster.getDestroyedType().toString() + "', '" + block.getLocation().getWorld().getName()
				+ "', '" + block.getLocation().getBlockX() + "', '" + block.getLocation().getBlockY() + "', '" + block.getLocation().getBlockZ() + "')");
	}

}
