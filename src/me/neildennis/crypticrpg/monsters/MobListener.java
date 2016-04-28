package me.neildennis.crypticrpg.monsters;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.items.custom.CrypticWeapon;
import me.neildennis.crypticrpg.items.metadata.ItemModifier.ModifierType;
import me.neildennis.crypticrpg.monsters.templates.SpawnTemplate;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;

public class MobListener implements Listener {
	
	@EventHandler
	public void onMobSpawn(CreatureSpawnEvent event){
		if (event.getSpawnReason() != SpawnReason.CUSTOM) event.setCancelled(true);
	}
	
	@EventHandler
	public void onMonsterHit(EntityDamageByEntityEvent event){
		if (event.getCause() != DamageCause.ENTITY_ATTACK) return;
		if (event.getDamager().getType() == EntityType.PLAYER) return;
		if (event.getEntity().getType() != EntityType.PLAYER) return;
		
		SpawnTemplate temp = MobManager.getCrypticEntity(event.getDamager());
		if (temp == null) return;
		
		final CrypticPlayer pl = PlayerManager.getCrypticPlayer((Player)event.getEntity());
		CrypticWeapon wep = temp.getWeapon();
		
		int damage = wep.getAttribute(ModifierType.DAMAGE).getValue();
		pl.getHealthData().damage();
		event.setDamage(damage);
		Bukkit.getScheduler().runTask(Cryptic.getPlugin(), new Runnable(){

			@Override
			public void run() {
				pl.getHealthData().updateOverheadHP();
			}
			
		});
	}
	
	@EventHandler
	public void onMonsterHitByPlayer(EntityDamageByEntityEvent event){
		if (event.getCause() != DamageCause.ENTITY_ATTACK) return;
		if (!(event.getEntity() instanceof LivingEntity)) return;
		if (event.getEntityType() == EntityType.PLAYER) return;
		if (event.getDamager().getType() != EntityType.PLAYER) return;
		
		final SpawnTemplate hit = MobManager.getCrypticEntity(event.getEntity());
		if (hit == null) return;
		
		//CrypticPlayer damager = PlayerManager.getCrypticPlayer((Player)event.getDamager());
		//TODO calc damage
		
		Bukkit.getScheduler().runTask(Cryptic.getPlugin(), new Runnable(){

			@Override
			public void run() {
				hit.updateBar();
			}
			
		});
	}

}
