package me.neildennis.crypticrpg.items.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.items.ItemManager;
import me.neildennis.crypticrpg.items.custom.TeleportBook;
import me.neildennis.crypticrpg.items.metadata.Attribute;
import me.neildennis.crypticrpg.items.metadata.Attribute.AttributeType;

public class TestCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!(sender instanceof Player)) return true;
		
		Player pl = (Player) sender;
		
		if (pl.getInventory().getItemInMainHand().getType() == Material.AIR){
			List<String> lore = new ArrayList<String>();
			
			List<Attribute> attribs = new ArrayList<Attribute>();
			attribs.add(new Attribute(AttributeType.DAMAGE, 20, 10));
			
			//CrypticItem item = new CrypticItem(Material.DIAMOND_SWORD, "Test Sword", lore, attribs, 4, Rarity.RARE, 3);
			TeleportBook item = new TeleportBook("Test Book", lore, true, new Location(Cryptic.getMainWorld(), 1, 1, 1));
			pl.getInventory().setItemInMainHand(item.getItemStack());
			pl.sendMessage("Spawned sword");
			ItemManager.trackItem(item);
			return true;
		}
		
		if (ItemManager.getCrypticItem(pl.getInventory().getItemInMainHand()) != null){
			pl.sendMessage("Item is in map");
		}
		
		else {
			pl.sendMessage("Item is not in map");
		}
		
		return true;
	}

}
