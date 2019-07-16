package me.neildennis.crypticrpg.professions;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.items.attribs.Tier;
import me.neildennis.crypticrpg.permission.Rank;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.professions.commands.CommandOre.OreSession;
import me.neildennis.crypticrpg.professions.events.OreMinedEvent;
import me.neildennis.crypticrpg.professions.events.ProfessionLevelEvent;
import me.neildennis.crypticrpg.professions.skill.Skill.SkillType;
import me.neildennis.crypticrpg.utils.Log;
import net.md_5.bungee.api.ChatColor;

public class MiningListener implements Listener{
	
	ProfessionManager profession;
	
	public MiningListener(ProfessionManager profession){
		this.profession = profession;
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event){
		
		Material breaker = event.getPlayer().getInventory().getItemInMainHand().getType();
		
		if (!breaker.equals(Material.DIAMOND_PICKAXE)
				&& !breaker.equals(Material.GOLDEN_PICKAXE)
				&& !breaker.equals(Material.IRON_PICKAXE)
				&& !breaker.equals(Material.STONE_PICKAXE)
				&& !breaker.equals(Material.WOODEN_PICKAXE)) return;
		
		boolean found = false;
		
		for (Tier tier : Tier.values())
			if (event.getBlock().getType().equals(tier.getOreType()))
				found = true;
		
		if (!found) return;
		
		OreCluster ore = profession.getOreLocations().get(event.getBlock().getLocation());
		
		if (ore == null) return;
		
		event.setCancelled(true);
		
		OreMinedEvent oreEvent = new OreMinedEvent(Cryptic.getCrypticPlayer(event.getPlayer()), ore, event.getBlock().getLocation(), 100);
		Cryptic.fireEvent(oreEvent);
		
		if (!oreEvent.isCancelled()) {
			profession.addOreRespawn(ore);
			
			CrypticPlayer pl = oreEvent.getCrypticPlayer();
			int oldLevel = pl.getExperience().getMiningSkill().getLevel();
			oreEvent.getCrypticPlayer().getExperience().getMiningSkill().addExperience(oreEvent.getExpGained());
			int newLevel = pl.getExperience().getMiningSkill().getLevel();
			
			if (oldLevel != newLevel) {
				ProfessionLevelEvent levelEvent = new ProfessionLevelEvent(pl, SkillType.MINING, newLevel);
				Cryptic.fireEvent(levelEvent);
			}
			
			long total = oreEvent.getCrypticPlayer().getExperience().getMiningSkill().getExperience();
			Log.debug(oreEvent.getCrypticPlayer().getPlayer().getName() + " has mined " + ore.getOreType().toString() + " for 100xp (Total: " + total + ")");
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onOreRemove(BlockBreakEvent event){
		CrypticPlayer pl = Cryptic.getCrypticPlayer(event.getPlayer());
		if (!pl.hasPermission(Rank.ADMIN)) return;
		
		OreSession session = profession.getOreSession(pl);
		if (session == null) return;
		
		event.setCancelled(true);
		
		OreCluster ore = profession.getOreLocations().get(event.getBlock().getLocation());
		if (ore == null) return;
		
		profession.unregisterOre(ore);
		event.setDropItems(false);
		event.setCancelled(false);
		
		pl.sendMessage(ChatColor.GREEN + "Removed an ore cluster");
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onOrePlace(BlockPlaceEvent event){
		CrypticPlayer pl = Cryptic.getCrypticPlayer(event.getPlayer());
		if (!pl.hasPermission(Rank.ADMIN)) return;
		
		OreSession session = profession.getOreSession(pl);
		if (session == null) return;
		
		event.setCancelled(true);
		
		OreCluster ore = profession.getOreLocations().get(event.getBlock().getLocation());
		if (ore != null) return;
		
		profession.registerOre(event.getBlock(), session);
		event.setCancelled(false);
		event.getBlock().setType(session.getTier().getOreType());
		
		pl.sendMessage(ChatColor.GREEN + "Registered an ore cluster");
	}

}
