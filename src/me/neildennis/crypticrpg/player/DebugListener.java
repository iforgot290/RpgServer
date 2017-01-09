package me.neildennis.crypticrpg.player;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.neildennis.crypticrpg.monsters.MobManager;
import me.neildennis.crypticrpg.monsters.type.CrypticMonster;

public class DebugListener implements Listener{

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerDamagedByMonster(EntityDamageByEntityEvent event){
		if (event.isCancelled()) return;
		if (event.getCause() != DamageCause.ENTITY_ATTACK) return;
		if (event.getDamager().getType() == EntityType.PLAYER) return;
		if (event.getEntity().getType() != EntityType.PLAYER) return;

		CrypticMonster monster = MobManager.getMonster(event.getDamager());
		if (monster == null) return;

		CrypticPlayer pl = PlayerManager.getCrypticPlayer((Player)event.getEntity());

		int damage = (int) event.getDamage();
		int health = (int) pl.getPlayer().getHealth();
		int maxhealth = (int) pl.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		
		pl.sendMessage(ChatColor.RED + "      -" + damage + " HEALTH " + ChatColor.RESET + "from " + monster.getName()
				+ " to " + pl.getPlayer().getName() + ChatColor.GREEN + " (" + (health-damage) + "/" + maxhealth + " HP)");

	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMonsterDamagedByPlayer(EntityDamageByEntityEvent event){
		if (event.isCancelled()) return;
		if (event.getCause() != DamageCause.ENTITY_ATTACK) return;
		if (!(event.getEntity() instanceof LivingEntity)) return;
		if (event.getEntityType() == EntityType.PLAYER) return;
		if (event.getDamager().getType() != EntityType.PLAYER) return;

		CrypticMonster hit = MobManager.getMonster(event.getEntity());
		if (hit == null) return;

		Player pl = (Player) event.getDamager();

		int damage = (int) event.getDamage();
		int health = (int) hit.getHealth();
		int maxhealth = (int) hit.getMaxHealth();
		int newhealth = health - damage >= 0 ? health - damage : 0;
		
		pl.sendMessage(ChatColor.RED + "      " + damage + " DMG " + ChatColor.RESET + "from " + ChatColor.ITALIC + pl.getName()
			+ ChatColor.RESET + " to " + ChatColor.ITALIC + hit.getName() + ChatColor.GREEN + " (" + newhealth + "/" + maxhealth + " HP)");
	}

}
