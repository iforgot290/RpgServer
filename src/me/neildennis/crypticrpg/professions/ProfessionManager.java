package me.neildennis.crypticrpg.professions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.Manager;
import me.neildennis.crypticrpg.cloud.Cloud;
import me.neildennis.crypticrpg.items.attribs.Tier;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.professions.commands.CommandOre;
import me.neildennis.crypticrpg.professions.commands.CommandOre.OreSession;
import me.neildennis.crypticrpg.utils.Log;
import me.neildennis.crypticrpg.utils.Utils;
import me.neildennis.crypticrpg.zone.Region;
import me.neildennis.crypticrpg.zone.ZoneManager;

public class ProfessionManager extends Manager{

	private static ArrayList<Region> miningzones;
	private static CommandOre oreCmd;

	public ProfessionManager(){
		miningzones = new ArrayList<Region>();

		for (Region region : ZoneManager.getRegions()){
			if (region.getMaxOreSpawns() > 0){
				miningzones.add(region);
			}
		}
		
		try {
			loadOres();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		tickOres();
		registerTasks();
		
		Cryptic.registerEvents(new MiningListener());
		Cryptic.registerCommand("ore", (oreCmd = new CommandOre()));
	}

	@Override
	public void registerTasks() {
		tasks.add(Bukkit.getScheduler().runTaskTimer(Cryptic.getPlugin(), 
				() -> tickOres(), 25, 20));
	}

	public void loadOres() throws SQLException{
		ResultSet data = Cloud.sendQuery("SELECT * FROM ore_spawns");
		int loaded = 0;

		while (data.next()){
			Region region = getMiningRegion(data.getString("region"));
			if (region == null) continue;

			UUID id = UUID.fromString(data.getString("uuid"));
			Tier tier = Tier.fromLevel((data.getInt("tier") * 20) - 1);
			JsonArray locations = (JsonArray) new JsonParser().parse(data.getString("locations"));

			OreCluster cluster = new OreCluster();
			cluster.setUUID(id);
			cluster.setTier(tier);
			cluster.setRegion(region);

			for (JsonElement loc : locations){
				cluster.addLocation(Utils.getLocFromString(loc.getAsString()));
			}

			region.getWaitingOre().add(cluster);
			loaded++;
		}

		Log.info("Loaded " + loaded + " ore clusters");
	}

	public void tickOres(){
		for (Region region : miningzones){
			if (region.getSpawnedOre().size() >= region.getMaxOreSpawns()){
				if (!region.getOreQueue().isEmpty()) region.getOreQueue().clear();
				continue;
			}

			Random rand = new Random();

			if (region.getOreQueue().isEmpty()){
				int needs = region.getMaxOreSpawns() - region.getSpawnedOre().size();
				for (int i = 0; i < needs; i++){
					OreCluster cluster = region.getWaitingOre().get(rand.nextInt(region.getWaitingOre().size()));
					cluster.spawn();
					region.getSpawnedOre().add(cluster);
					region.getWaitingOre().remove(cluster);
				}
			} else {
				long nextspawn = region.getOreQueue().peek();
				
				if (nextspawn <= System.currentTimeMillis()){
					nextspawn = region.getOreQueue().poll();
					
					OreCluster cluster = region.getWaitingOre().get(rand.nextInt(region.getWaitingOre().size()));
					cluster.spawn();
					region.getSpawnedOre().add(cluster);
					region.getWaitingOre().remove(cluster);
				}
			}

		}
	}
	
	public static ArrayList<OreSession> getOreSessions(){
		return oreCmd.getOreSessions();
	}
	
	public static OreSession getOreSession(CrypticPlayer pl){
		return oreCmd.getOreSession(pl);
	}

	public static Region getMiningRegion(String name){
		for (Region region : miningzones)
			if (region.getRegion().getId().equalsIgnoreCase(name))
				return region;
		return null;
	}

}
