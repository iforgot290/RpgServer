package me.neildennis.crypticrpg.zone;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.connorlinfoot.titleapi.TitleAPI;
import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.Manager;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;
import me.neildennis.crypticrpg.utils.Log;

public class ZoneManager extends Manager implements Listener{
	
	private static HashMap<String, Region> regions = new HashMap<String, Region>();
	
	public ZoneManager(){
		loadRegions();
		Bukkit.getPluginManager().registerEvents(this, Cryptic.getPlugin());
		Cryptic.registerCommand("zone", new ZoneCommand());
	}
	
	private void loadRegions(){
		File folder = new File(Cryptic.getPlugin().getDataFolder() + "/regions/");
		folder.mkdirs();
		
		for (File file : folder.listFiles()){
			if (!file.getName().endsWith(".yml")) continue;
			String name = file.getName().replaceAll(".yml", "");
			
			ProtectedRegion region = WGBukkit.getRegionManager(Cryptic.getMainWorld()).getRegion(name);
			if (region == null) continue;
			
			YamlConfiguration config = new YamlConfiguration();
			try {
				config.load(file);
				regions.put(name, new Region(config, region));
				Log.debug("Loaded region \"" + name + "\"");
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void registerTasks() {
		
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event){
		CrypticPlayer moved = PlayerManager.getCrypticPlayer(event.getPlayer());
		if (moved == null) return;

		checkZone(moved, event.getTo());
	}

	@EventHandler
	public void onTeleport(PlayerTeleportEvent event){
		CrypticPlayer moved = PlayerManager.getCrypticPlayer(event.getPlayer());
		if (moved == null) return;
		
		checkZone(moved, event.getTo());
	}
	
	public static Collection<Region> getRegions(){
		return regions.values();
	}
	
	public static Region getRegion(String id){
		return regions.get(id);
	}

	public static void checkZone(CrypticPlayer pl, Location to){
		RegionManager manager = WGBukkit.getRegionManager(to.getWorld());
		ZoneState tostate = getZoneState(manager.getApplicableRegions(to));

		if (pl.getZoneState() != tostate){
			Log.debug(pl.getPlayer().getName() + " has changed zone states");
			pl.setZoneState(tostate);
			ActionBarAPI.sendActionBar(pl.getPlayer(), tostate.getDisplay());
		}
		
		Region current = pl.getTown();
		Region toregion = null;
		
		for (ProtectedRegion region : manager.getApplicableRegions(to)){
			Region check = regions.get(region.getId());
			if (check != null && check.isTown()){
				toregion = check;
			}
		}
		
		if (current == null && toregion == null) return;
		
		if (current != null && toregion == null) {
			pl.setTown(null);
			return;
		}
		
		if ((current == null && toregion != null) || current != toregion){
			TitleAPI.sendTitle(pl.getPlayer(), 10, 40, 10, toregion.getAnnounce(), toregion.getSubtitle());
			pl.setTown(toregion);
		}
	}

	public static ZoneState getZoneState(ApplicableRegionSet regionset){

		if (regionset.queryState(null, DefaultFlag.PVP) == State.ALLOW) return ZoneState.PVP;
		if (regionset.queryState(null, DefaultFlag.MOB_DAMAGE) == State.ALLOW) return ZoneState.MONSTERS;

		return ZoneState.PEACEFUL;
	}

	public enum ZoneState{
		PEACEFUL(ChatColor.GREEN + "** Entering " + ChatColor.BOLD + "PEACEFUL" + ChatColor.GREEN + " zone **"),
		MONSTERS(ChatColor.YELLOW + "** Entering " + ChatColor.BOLD + "NEUTRAL" + ChatColor.YELLOW + " zone **"),
		PVP(ChatColor.RED + "** Entering " + ChatColor.BOLD + "CHAOTIC" + ChatColor.RED + " zone **");

		private String display;

		ZoneState(String display){
			this.display = display;
		}

		public String getDisplay(){
			return display;
		}
	}

}
