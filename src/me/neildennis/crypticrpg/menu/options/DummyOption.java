package me.neildennis.crypticrpg.menu.options;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.neildennis.crypticrpg.player.CrypticPlayer;

public class DummyOption extends MenuOption {

	@Override
	public void activate(CrypticPlayer pl) {
		pl.sendMessage("Debug Message");
	}

	@Override
	public ItemStack getItem() {
		return new ItemStack(Material.GRASS_BLOCK);
	}

}
