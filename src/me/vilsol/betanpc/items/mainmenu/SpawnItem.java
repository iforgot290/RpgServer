package me.vilsol.betanpc.items.mainmenu;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import me.vilsol.betanpc.menus.TierMenu;
import me.vilsol.menuengine.engine.MenuItem;
import me.vilsol.menuengine.engine.MenuModel;
import me.vilsol.menuengine.utils.Builder;

public class SpawnItem implements MenuItem {

	@Override
	public void registerItem() {
		MenuItem.items.put(this.getClass(), this);
	}

	@Override
	public void execute(Player plr, ClickType click) {
		MenuModel.getMenu(TierMenu.class).getMenu().showToPlayer(plr);
	}

	@Override
	public ItemStack getItem() {
		return new Builder(Material.DIAMOND_CHESTPLATE).setName(ChatColor.LIGHT_PURPLE + "Spawn Items")
				.setLore(Arrays.asList(ChatColor.GRAY + "Spawn in " + ChatColor.BOLD + "Armor/Weapons " + ChatColor.GRAY
						+ " of any Tier."))
				.getItem();
	}

}
