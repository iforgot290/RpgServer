package me.vilsol.foodvendor;

import org.bukkit.entity.Player;

import me.vilsol.menuengine.engine.DynamicMenu;
import me.vilsol.menuengine.engine.DynamicMenuModel;

public class DynamicFoodVendor extends DynamicMenu {

	public DynamicFoodVendor(int size, DynamicMenuModel parent, Player owner) {
		super(size, parent, owner);
		this.name = "Food Vendor";
	}

}