package me.neildennis.crypticrpg.bank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.neildennis.crypticrpg.cloud.Cloud;
import me.neildennis.crypticrpg.player.CrypticPlayer;

public class Bank {
	
	private CrypticPlayer cp;
	private Inventory inv;
	private int money;
	private int level;
	
	public Bank(CrypticPlayer cp){
		this.cp = cp;
		load();
	}

	public void save(){
		
	}
	
	public void load(){
		ResultSet data = Cloud.sendQuery("SELECT * FROM banks WHERE player_id = '" + cp.getOfflinePlayer().getUniqueId() + "'");
		
		try {
			if (!data.next()) return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
