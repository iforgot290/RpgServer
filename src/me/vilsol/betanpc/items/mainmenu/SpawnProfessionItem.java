package me.vilsol.betanpc.items.mainmenu;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import me.vilsol.betanpc.menus.ProfessionMenu;
import me.vilsol.menuengine.engine.MenuItem;
import me.vilsol.menuengine.engine.MenuModel;
import me.vilsol.menuengine.utils.Builder;

public class SpawnProfessionItem implements MenuItem {

	@Override
	public void registerItem() {
		MenuItem.items.put(this.getClass(), this);
	}

	@Override
	public void execute(Player plr, ClickType click) {
		MenuModel.getMenu(ProfessionMenu.class).getMenu().showToPlayer(plr);
	}

	@Override
	public ItemStack getItem() {
		return new Builder(Material.FISHING_ROD).setName(ChatColor.AQUA + "Spawn Profession Item").setLore(Arrays.asList(ChatColor.GRAY + "Spawn Pickaxes/Fishing rods", ChatColor.GRAY + "with a custom level/enchants")).getItem();
	}
	
}
