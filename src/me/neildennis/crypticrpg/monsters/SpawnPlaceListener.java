package me.neildennis.crypticrpg.monsters;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.neildennis.crypticrpg.menu.Menu;
import me.neildennis.crypticrpg.monsters.templates.SpawnTemplate;
import me.neildennis.crypticrpg.permission.Rank;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;
import me.neildennis.crypticrpg.utils.FancyMessage;
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
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onBlockInteract(PlayerInteractEvent event){
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if (event.getClickedBlock().getType() != Material.MOB_SPAWNER) return;
		CrypticPlayer pl = PlayerManager.getCrypticPlayer(event.getPlayer());
		if (pl.getRank() != Rank.OWNER) return;
		
		SpawnBlock blk = MobManager.getSpawnBlock(event.getClickedBlock().getLocation());
		if (blk == null) return;
		
		pl.setMenu(new MainSpawnMenu(pl.getPlayer(), blk));
	}
	
	public class MainSpawnMenu extends Menu{
		
		private SpawnBlock blk;

		public MainSpawnMenu(Player pl, SpawnBlock blk) {
			super(pl);
			this.blk = blk;
		}
		
		private int tick = 0;
		
		public void updateMenu(){
			text.clear();
			options.clear();
			
			String animation = "";
			
			for (int i = tick; i < 5; i++){
				animation += "+";
			}
			
			tick++;
			if (tick == 5) tick = 0;
			
			text.add(new FancyMessage(animation));
			text.add(new FancyMessage(""));
			
			text.add(new FancyMessage(ChatColor.GRAY + "Current mobs:"));
			for (SpawnTemplate spawn : blk.getSpawns()){
				FancyMessage msg = new FancyMessage("    ");
				msg.then(ChatColor.GREEN + "[" + spawn.getLevel() + "] " + ChatColor.GRAY + spawn.getType().toString() +
						" " + spawn.getName());
				text.add(msg);
			}
			
			options.add(new FancyMessage(ChatColor.GRAY + "Range: " + ChatColor.RED + blk.getRange()).tooltip("test"));
		}
		
		public SpawnBlock getSpawnBlock(){
			return blk;
		}
		
	}

}
