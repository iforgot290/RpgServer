package me.neildennis.crypticrpg.items.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neildennis.crypticrpg.items.CrypticItem;
import me.neildennis.crypticrpg.items.metadata.Attribute;
import me.neildennis.crypticrpg.items.metadata.Rarity;
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
			
			CrypticItem item = new CrypticItem(Material.DIAMOND_SWORD, "Test Sword", lore, attribs, 4, Rarity.RARE, 3);
			pl.getInventory().setItemInMainHand(item);
			pl.sendMessage("Spawned sword");
			return true;
		}
		
		if (pl.getInventory().getItemInMainHand() instanceof CrypticItem){
			pl.sendMessage("Item is cryptic");
		}
		
		else {
			pl.sendMessage("Item is not cryptic");
		}
		
		return true;
	}

}
