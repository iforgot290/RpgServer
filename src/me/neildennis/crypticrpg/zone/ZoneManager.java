package me.neildennis.crypticrpg.zone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

//import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.Manager;
import me.neildennis.crypticrpg.cloud.CloudManager;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;
import me.neildennis.crypticrpg.utils.Log;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class ZoneManager extends Manager implements Listener{
	
	private static HashMap<String, Region> regions = new HashMap<String, Region>();
	
	@Override
	public void onEnable(){
		loadRegions();
		Cryptic.registerEvents(this);
		Cryptic.registerCommand("zone", new ZoneCommand());
		registerTasks();
	}
	
	@Override
	public void onDisable()	{
		
	}
	
	private void loadRegions(){
		try {
			ResultSet data = CloudManager.sendQuery("SELECT * FROM regions");
			RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
			RegionManager manager = container.get(BukkitAdapter.adapt(Cryptic.getMainWorld()));
			while (data.next()){
				String id = data.getString("region_id");
				ProtectedRegion region = manager.getRegion(id);
				
				if (region == null){
					Log.warning("Error loading region \"" + id + "\" from database: No such region exists");
					continue;
				}
				
				String title = data.getString("title");
				String subtitle = data.getString("subtitle");
				JsonArray monsters = (JsonArray) new JsonParser().parse(data.getString("monsters"));
				
				if (title == null)
					regions.put(id, new Region(region, monsters));
				else
					regions.put(id, new Region(region, monsters, title, subtitle));
				
				Log.info("Loaded region: " + id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void registerTasks() {
		tasks.add(Bukkit.getScheduler().runTaskTimer(Cryptic.getPlugin(),
				() -> {for (Region region : regions.values()) region.tickSpawns();}, 20L, 20L));
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event){
		CrypticPlayer moved = Cryptic.getCrypticPlayer(event.getPlayer());
		if (moved == null) return;

		checkZone(moved, event.getTo());
	}

	@EventHandler
	public void onTeleport(PlayerTeleportEvent event){
		CrypticPlayer moved = Cryptic.getCrypticPlayer(event.getPlayer());
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
		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		ApplicableRegionSet regionset = container.createQuery().getApplicableRegions(BukkitAdapter.adapt(to));
		ZoneState tostate = getZoneState(regionset);

		if (pl.getZoneState() != tostate){
			Log.debug(pl.getPlayer().getName() + " has changed zone states");
			pl.setZoneState(tostate);
			pl.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(tostate.getDisplay()));
		}
		
		Region current = pl.getTown();
		Region toregion = null;
		
		for (ProtectedRegion region : regionset){
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
			pl.getPlayer().sendTitle(toregion.getAnnounce(), toregion.getSubtitle(), 10, 40, 10);
			pl.setTown(toregion);
		}
	}

	public static ZoneState getZoneState(ApplicableRegionSet regionset){

		if (regionset.queryState(null, Flags.PVP) == State.ALLOW) return ZoneState.PVP;
		if (regionset.queryState(null, Flags.MOB_DAMAGE) == State.ALLOW) return ZoneState.MONSTERS;

		return ZoneState.PEACEFUL;
	}

	public enum ZoneState{
		PEACEFUL(ChatColor.GREEN + "** Entering " + ChatColor.BOLD + "SAFE" + ChatColor.GREEN + " zone **"),
		MONSTERS(ChatColor.YELLOW + "** Entering " + ChatColor.BOLD + "WILDERNESS" + ChatColor.YELLOW + " zone **"),
		PVP(ChatColor.RED + "** Entering " + ChatColor.BOLD + "PVP" + ChatColor.RED + " zone **");

		private String display;

		ZoneState(String display){
			this.display = display;
		}

		public String getDisplay(){
			return display;
		}
	}

}
