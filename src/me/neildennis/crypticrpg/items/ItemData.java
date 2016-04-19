package me.neildennis.crypticrpg.items;

import java.sql.ResultSet;

import me.neildennis.crypticrpg.player.CrypticPlayer;

public class ItemData {
	
	private CrypticPlayer pl;
	
	public ItemData(CrypticPlayer pl, ResultSet data){
		this.pl = pl;
	}

}
