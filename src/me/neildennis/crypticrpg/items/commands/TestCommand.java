package me.neildennis.crypticrpg.items.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.neildennis.crypticrpg.items.custom.CrypticGear;
import me.neildennis.crypticrpg.items.generator.ItemGenerator;
import me.neildennis.crypticrpg.items.metadata.ItemType;
import me.neildennis.crypticrpg.items.metadata.Rarity;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.player.PlayerManager;

public class TestCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player)) return true;

		Player pl = (Player) sender;
		CrypticPlayer cp = PlayerManager.getCrypticPlayer(pl);

		if (pl.getInventory().getItemInMainHand().getType() == Material.AIR){
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
		}

		return true;
	}

}
