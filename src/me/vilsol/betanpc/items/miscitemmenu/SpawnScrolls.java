package me.vilsol.betanpc.items.miscitemmenu;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import me.vilsol.betanpc.menus.ScrollMenu;
import me.vilsol.menuengine.engine.MenuItem;
import me.vilsol.menuengine.engine.MenuModel;
import me.vilsol.menuengine.utils.Builder;

public class SpawnScrolls implements MenuItem {

	@Override
	public void registerItem() {
		MenuItem.items.put(this.getClass(), this);
	}

	@Override
	public void execute(Player plr, ClickType click) {
		MenuModel.getMenu(ScrollMenu.class).getMenu().showToPlayer(plr);
	}

	@Override
	public ItemStack getItem() {
		return new Builder(Material.EMPTY_MAP)
				.setName(ChatColor.RED + "Spawn Scrolls").setLore(Arrays.asList(ChatColor.GRAY + "Spawn in "
						+ ChatColor.UNDERLINE + "Enchants/Protection Scrolls " + ChatColor.GRAY + "of any Tier."))
				.getItem();
	}

}
