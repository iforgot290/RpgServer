package me.neildennis.crypticrpg.menu;

import me.neildennis.crypticrpg.CrypticCommand;
import me.neildennis.crypticrpg.menu.options.MenuOption;
import me.neildennis.crypticrpg.menu.options.PageOption;
import me.neildennis.crypticrpg.player.CrypticPlayer;

public class MenuCommand extends CrypticCommand {

	@Override
	protected boolean sendUsage() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean command(CrypticPlayer pl) {
		ItemMenu menu = new ItemMenu();
		menu.setName("Test Menu 1000").setSlots(45).setVerticalPadding(2).addOption(new PageOption()).addOption(new PageOption()).addOption(new PageOption());
		pl.getPlayer().openInventory(menu.generatePage(0));
		return false;
	}

	@Override
	public boolean console() {
		// TODO Auto-generated method stub
		return false;
	}

}
