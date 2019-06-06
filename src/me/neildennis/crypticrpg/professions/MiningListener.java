package me.neildennis.crypticrpg.professions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.permission.Rank;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;
import me.neildennis.crypticrpg.professions.commands.CommandOre.OreSession;
import me.neildennis.crypticrpg.utils.CustomFlags;
import me.neildennis.crypticrpg.utils.Log;
import me.neildennis.crypticrpg.zone.Region;

public class MiningListener implements Listener{
	
	RegionContainer regions;
	
	public MiningListener(){
		regions = WorldGuard.getInstance().getPlatform().getRegionContainer();
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event){
		ApplicableRegionSet set = regions.createQuery().getApplicableRegions(BukkitAdapter.adapt(event.getBlock().getLocation()));
		
		for (ProtectedRegion wgregion : set){
			Region region = ProfessionManager.getMiningRegion(wgregion.getId());
			
			if (region == null) continue;
			
			OreCluster mined = null;
			
			for (OreCluster cluster : region.getSpawnedOre()){
				if (cluster.containsLocation(event.getBlock().getLocation())){
					Log.debug("Ore mined");
					mined = cluster;
				}
			}
			
			if (mined == null) continue;
			
			final OreCluster finalMined = mined;
			
			Bukkit.getScheduler().runTask(Cryptic.getPlugin(), () -> {
				if (!finalMined.isSpawned()){
					Log.debug("Whole cluster mined");
					region.getSpawnedOre().remove(finalMined);
					region.getWaitingOre().add(finalMined);
					region.getOreQueue().offer(System.currentTimeMillis() + (region.getRegion().getFlag(CustomFlags.ORE_RESPAWN) * 1000));
				}
			});
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onOreRemove(BlockBreakEvent event){
		CrypticPlayer pl = PlayerManager.getCrypticPlayer(event.getPlayer());
		if (!pl.hasPermission(Rank.ADMIN)) return;
		
		OreSession session = ProfessionManager.getOreSession(pl);
		if (session == null) return;
		
		event.setCancelled(true);
		
		OreCluster cluster = session.getOreCluster();
		Location loc = event.getBlock().getLocation();
		
		if (cluster.containsLocation(event.getBlock().getLocation())){
			cluster.removeLocation(loc);
			event.getBlock().setType(Material.AIR);
			pl.sendMessage(ChatColor.GREEN + "Removed ore in cluster");
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onOrePlace(BlockPlaceEvent event){
		if (event.getBlock().getType() != Material.STONE) return;
		
		CrypticPlayer pl = PlayerManager.getCrypticPlayer(event.getPlayer());
		if (!pl.hasPermission(Rank.ADMIN)) return;
		
		OreSession session = ProfessionManager.getOreSession(pl);
		if (session == null) return;
		
		event.setCancelled(true);

		OreCluster cluster = session.getOreCluster();
		cluster.addLocation(event.getBlock().getLocation());
		Bukkit.getScheduler().runTask(Cryptic.getPlugin(), () -> cluster.spawn());
		pl.sendMessage(ChatColor.GREEN + "Added ore in cluster");
	}

}
