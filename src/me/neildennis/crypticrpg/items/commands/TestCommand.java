package me.neildennis.crypticrpg.items.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.monsters.MobManager;
import me.neildennis.crypticrpg.monsters.SpawnBlock;
import me.neildennis.crypticrpg.monsters.templates.SpawnTemplate;
import me.neildennis.crypticrpg.monsters.templates.ZombieTemplate;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;
import me.neildennis.crypticrpg.utils.Log;

public class TestCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) return true;

		Player pl = (Player) sender;
		CrypticPlayer cp = PlayerManager.getCrypticPlayer(pl);

		/*if (pl.getInventory().getItemInMainHand().getType() == Material.AIR){
			List<String> lore = new ArrayList<String>();
			lore.add("Teleport book to test m8");

			int slot = 0;
			for (ItemStack is : pl.getInventory()){
				CrypticGear gear = new ItemGenerator(ItemType.SWORD, 5).setName("Test").setRarity(Rarity.UNIQUE).generate();
				pl.getInventory().setItem(slot, gear.getItemStack());
				pl.sendMessage("Spawned");
				cp.getItemData().trackItem(gear);
				slot++;
			}
			return true;
		}

		if (cp.getItemData().getCrypticItem(pl.getInventory().getItemInMainHand()) != null){
			pl.sendMessage("Item is in map");
		}

		else {
			pl.sendMessage("Item is not in map");
		}*/
		
		/*ArrayList<SpawnTemplate> spawns = new ArrayList<SpawnTemplate>();
		spawns.add(new ZombieTemplate("Faggot", false, 10000));
		final SpawnBlock blk = MobManager.createNewSpawnBlock(pl.getLocation(), 10, 3, 9, spawns);

		Bukkit.getScheduler().runTaskAsynchronously(Cryptic.getPlugin(), new Runnable(){

			@Override
			public void run() {
				blk.save();
				Log.debug("Saved new spawner");
			}
			
		});*/
		
		CrypticPlayer cpl = PlayerManager.getCrypticPlayer(pl);
		cpl.setMenu(null);
		
		Log.debug("Removed menu");
		
		return true;
	}

}
