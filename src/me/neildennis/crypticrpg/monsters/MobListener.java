package me.neildennis.crypticrpg.monsters;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.items.attribs.AttributeType;
import me.neildennis.crypticrpg.monsters.type.CrypticMonster;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;
import me.neildennis.crypticrpg.utils.Log;

public class MobListener implements Listener {

	@EventHandler
	public void onMobSpawn(CreatureSpawnEvent event){
		if (event.getSpawnReason() == SpawnReason.CHUNK_GEN) Log.debug("chunk gen");
		if (event.getSpawnReason() != SpawnReason.CUSTOM) event.setCancelled(true);
	}

	/**
	 * When a monster hits a player
	 * @param event damage event
	 */
	@EventHandler
	public void onMonsterHit(EntityDamageByEntityEvent event){
		if (event.getCause() != DamageCause.ENTITY_ATTACK) return;
		if (event.getDamager().getType() == EntityType.PLAYER) return;
		if (event.getEntity().getType() != EntityType.PLAYER) return;
		
		CrypticMonster monster = MobManager.getMonster(event.getDamager());
		if (monster == null) return;

		final CrypticPlayer pl = PlayerManager.getCrypticPlayer((Player)event.getEntity());

		pl.getHealthData().damage();
		event.setDamage(monster.getStats().getAttribute(AttributeType.DAMAGE).genValue());
		Bukkit.getScheduler().runTask(Cryptic.getPlugin(), () -> pl.getHealthData().updateOverheadHP());
	}

	@EventHandler
	public void onMonsterDamage(EntityCombustEvent event){
		if (!(event instanceof EntityCombustByEntityEvent)){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onMonsterHitByPlayer(EntityDamageByEntityEvent event){
		if (event.getCause() != DamageCause.ENTITY_ATTACK) return;
		if (!(event.getEntity() instanceof LivingEntity)) return;
		if (event.getEntityType() == EntityType.PLAYER) return;
		if (event.getDamager().getType() != EntityType.PLAYER) return;

		final CrypticMonster hit = MobManager.getMonster(event.getEntity());
		if (hit == null) return;

		CrypticPlayer damager = PlayerManager.getCrypticPlayer((Player)event.getDamager());
		event.setDamage(damager.getStats().getAttribute(AttributeType.DAMAGE).genValue());

		Bukkit.getScheduler().runTask(Cryptic.getPlugin(), () -> hit.updateNameplate());
	}

	@EventHandler
	public void onMobDeath(EntityDeathEvent event){
		if (event.getEntityType() == EntityType.PLAYER) return;
		if (!(event.getEntity() instanceof LivingEntity)) return;

		MonsterContainer dead = MobManager.getMonsterContainer(event.getEntity());
		if (dead == null) return;
		
		dead.setDeath();
		MobManager.unregisterMonster(dead);
	}

	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event){
		if (event.getWorld() == Cryptic.getMainWorld())
			if (event.getChunk().getEntities().length > 0)
				for (Entity ent : event.getChunk().getEntities())
					Log.debug(ent.getType() + " unloaded in a chunk");
	}
	
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event){
		if (event.getWorld() == Cryptic.getMainWorld())
			if (event.getChunk().getEntities().length > 0)
				for (Entity ent : event.getChunk().getEntities())
					Log.debug(ent.getType() + " loaded in a chunk");
	}

}
