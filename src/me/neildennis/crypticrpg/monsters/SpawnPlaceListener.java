package me.neildennis.crypticrpg.monsters;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import me.neildennis.crypticrpg.permission.Rank;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;

public class SpawnPlaceListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPlace(BlockPlaceEvent event){
		if (event.getBlock().getType() != Material.MOB_SPAWNER) return;
		
		CrypticPlayer pl = PlayerManager.getCrypticPlayer(event.getPlayer());
		
		if (pl.getRank().getPriority() < Rank.ADMIN.getPriority()){
			event.setCancelled(true);
		}
	}

}
