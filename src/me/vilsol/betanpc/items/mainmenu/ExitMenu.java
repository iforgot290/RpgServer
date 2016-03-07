package me.vilsol.betanpc.items.mainmenu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import me.vilsol.menuengine.engine.MenuItem;
import me.vilsol.menuengine.utils.Builder;

public class ExitMenu implements MenuItem {

	@Override
	public void registerItem() {
		MenuItem.items.put(this.getClass(), this);
	}

	@Override
	public void execute(Player plr, ClickType click) {
		plr.closeInventory();
	}

	@Override
	public ItemStack getItem() {
		return new Builder(Material.ARROW).setName(ChatColor.RED + "Exit Menu").getItem();
	}

}
