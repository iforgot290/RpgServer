package me.neildennis.crypticrpg.health;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.neildennis.crypticrpg.player.PlayerManager;

public class HealthListener implements Listener{
	
	@EventHandler
	public void onPlayerDeathEvent(PlayerDeathEvent event){
		event.getEntity().setExp(0);
		event.getEntity().setLevel(0);
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event){
		PlayerManager.getCrypticPlayer(event.getPlayer()).getHealthData().setLastHit(0);
	}
	
	@EventHandler
	public void onPlayerRegen(EntityRegainHealthEvent event){
		if (event.getEntityType() != EntityType.PLAYER) return;
		if (event.getRegainReason() == RegainReason.SATIATED) event.setCancelled(true);
	}

}
