package me.neildennis.crypticrpg.professions;

import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.neildennis.crypticrpg.professions.events.ProfessionLevelEvent;

public class ProfessionListener implements Listener {
	
	@EventHandler
	public void onPlayerLevel(ProfessionLevelEvent event) {
		Player player = event.getPlayer().getPlayer();
		player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1.1F);
		player.spawnParticle(Particle.SPELL_WITCH, player.getLocation(), 25);
		player.sendMessage(String.format(ChatColor.translateAlternateColorCodes('&', "&e&n&lLEVEL UP!&a Your skill in &e%s &ahas increased to &e%d&a!"), event.getSkillType().toString(), event.getNewLevel()));
	}

}
