package me.neildennis.crypticrpg.menu.options;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.neildennis.crypticrpg.player.CrypticPlayer;

public class CommandOption extends MenuOption {

	private String cmd;
	private String name;
	private String desc;
	
	private ItemStack item;
	
	public CommandOption(String cmd, String name, String desc) {
		this.cmd = cmd;
		this.name = name;
		this.desc = desc;
		
		setup();
	}
	
	private void setup() {
		item = new ItemStack(Material.NAME_TAG);
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RESET + name);
		meta.setLore(Arrays.asList(ChatColor.GRAY + desc));
		item.setItemMeta(meta);
	}
	
	@Override
	public void activate(CrypticPlayer pl) {
		pl.getPlayer().performCommand(cmd);
	}

	@Override
	public ItemStack getItem() {
		return item;
	}

}
