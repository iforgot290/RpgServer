package me.vilsol.betanpc.items.spawnmenu;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import me.vilsol.betanpc.enums.ItemTier;
import me.vilsol.betanpc.enums.ItemType;
import me.vilsol.betanpc.enums.SpawnStage;
import me.vilsol.betanpc.workers.ItemSpawnWorker;
import me.vilsol.menuengine.engine.BonusItem;
import me.vilsol.menuengine.engine.DynamicMenuModel;
import me.vilsol.menuengine.engine.MenuItem;
import me.vilsol.menuengine.utils.Builder;

public class Chestplate implements MenuItem, BonusItem<ItemTier> {

	private ItemTier tier;
	
	@Override
	public void setBonusData(ItemTier t) {
		tier = t;
	}

	@Override
	public void registerItem() {
		MenuItem.items.put(this.getClass(), this);
	}

	@Override
	public void execute(Player plr, ClickType click) {
		((ItemSpawnWorker) DynamicMenuModel.getMenu(plr)).type = ItemType.CHESTPLATE;
		((ItemSpawnWorker) DynamicMenuModel.getMenu(plr)).stage = SpawnStage.RARITY_CHOICE;
		DynamicMenuModel.getMenu(plr).showToPlayer(plr);
	}

	@Override
	public ItemStack getItem() {
		return new Builder(tier.getMaterialFromType(ItemType.CHESTPLATE)).setName(tier.getTierColor() + "Get Random " + tier.name() + " Chestplate").setLore(Arrays.asList(ChatColor.GRAY + "Spawn Random " + tier.name() + " Chestplate")).getItem();
	}
	
}
