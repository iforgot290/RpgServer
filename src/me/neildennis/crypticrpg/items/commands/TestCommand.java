package me.neildennis.crypticrpg.items.commands;

import org.bukkit.Bukkit;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.CrypticCommand;
import me.neildennis.crypticrpg.items.attribs.Rarity;
import me.neildennis.crypticrpg.items.attribs.Tier;
import me.neildennis.crypticrpg.items.generator.ItemGenerator;
import me.neildennis.crypticrpg.items.type.CrypticItem;
import me.neildennis.crypticrpg.items.type.CrypticItemType;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.utils.Log;

public class TestCommand extends CrypticCommand{

	@Override
	protected boolean sendUsage() {
		return true;
	}

	@Override
	public boolean command(CrypticPlayer pl) {
		Cryptic.getPlugin().getItemManager().loadMods();
		CrypticItem item = new ItemGenerator(CrypticItemType.SWORD).setRarity(Rarity.COMMON).setTier(Tier.ONE).generate();
		pl.getPlayer().getInventory().setItemInMainHand(item.generateItemStack());
		Bukkit.broadcastMessage("test");
		return true;
		
	}

	@Override
	public boolean console() {
		Log.info("Cannot use from console");
		return true;
	}

}
