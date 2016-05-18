package me.neildennis.crypticrpg.zone;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.Manager;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;
import me.neildennis.crypticrpg.utils.Log;

public class ZoneManager extends Manager implements Listener{

	public ZoneManager(){
		Bukkit.getPluginManager().registerEvents(this, Cryptic.getPlugin());
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

	public void checkZone(CrypticPlayer pl, Location to){
		ZoneState tostate = getZoneState(WGBukkit.getRegionManager(to.getWorld()).getApplicableRegions(to));

		if (pl.getZoneState() != tostate){
			Log.debug(pl.getPlayer().getName() + " has changed zone states");
			pl.setZoneState(tostate);
		}
	}

	public ZoneState getZoneState(ApplicableRegionSet regionset){

		if (regionset.queryState(null, DefaultFlag.PVP) == State.ALLOW) return ZoneState.PVP;
		if (regionset.queryState(null, DefaultFlag.MOB_DAMAGE) == State.ALLOW) return ZoneState.MONSTERS;

		return ZoneState.PEACEFUL;
	}

	public enum ZoneState{
		PEACEFUL("PEACEFUL"), MONSTERS("NEUTRAL"), PVP("CHAOTIC");

		private String display;

		ZoneState(String display){
			this.display = display;
		}

		public String getDisplay(){
			return display;
		}
	}

}
