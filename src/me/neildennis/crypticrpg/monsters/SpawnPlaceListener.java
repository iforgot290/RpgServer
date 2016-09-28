package me.neildennis.crypticrpg.monsters;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import me.neildennis.crypticrpg.monsters.spawnblock.SpawnBlockMenu;
import me.neildennis.crypticrpg.monsters.templates.SpawnTemplate;
import me.neildennis.crypticrpg.permission.Rank;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;
import me.neildennis.crypticrpg.utils.Log;

public class SpawnPlaceListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPlace(BlockPlaceEvent event){
		if (event.getBlock().getType() != Material.MOB_SPAWNER) return;
		
		CrypticPlayer pl = PlayerManager.getCrypticPlayer(event.getPlayer());
		
		if (pl.getRank().getPriority() < Rank.ADMIN.getPriority()){
			event.setCancelled(true);
		}
		
		SpawnBlock blk = MobManager.createNewSpawnBlock(event.getBlock().getLocation(), 5, 1, 10);
		
		pl.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "You have registered a new spawn block");
		pl.sendMessage(ChatColor.YELLOW + blk.getLocation().toString());
		pl.sendMessage(ChatColor.YELLOW + "Range: " + blk.getRange() + "   LVL " + blk.getMinLvl() + "-" + blk.getMaxLvl());
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onBlockInteract(PlayerInteractEvent event){
		if (event.getHand() != EquipmentSlot.HAND) return;
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if (event.getClickedBlock().getType() != Material.MOB_SPAWNER) return;
		CrypticPlayer pl = PlayerManager.getCrypticPlayer(event.getPlayer());
		if (pl.getRank().getPriority() < Rank.ADMIN.getPriority()) return;
		
		SpawnBlock blk = MobManager.getSpawnBlock(event.getClickedBlock().getLocation());
		if (blk == null) return;
		
		event.setCancelled(true);
		
		pl.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Spawn Block " + ChatColor.YELLOW + "@ " + blk.getLocation().toString());
		pl.sendMessage(ChatColor.YELLOW + "Range: " + blk.getRange() + "   LVL " + blk.getMinLvl() + "-" + blk.getMaxLvl());
		pl.sendMessage(ChatColor.YELLOW + "Mobs:" + (blk.getSpawns().size() == 0 ? " None" : ""));
		for (SpawnTemplate spawn : blk.getSpawns())
			pl.sendMessage(ChatColor.GRAY.toString() + spawn.getTier() + " " + spawn.getType().name() + " (" + spawn.getRespawnDelay() + "ms)");
		
		if (pl.getPlayer().isSneaking()){
			SpawnBlockMenu menu = new SpawnBlockMenu(pl, blk);
			
			pl.sendMessage("");
			pl.sendMessage(ChatColor.YELLOW + "Entering spawn-block menu");
			
			pl.setMenu(menu);
			menu.display();
		} else {
			pl.sendMessage("");
			pl.sendMessage(ChatColor.YELLOW + "Shift right-click to enter editing mode");
		}
		
	}

}
