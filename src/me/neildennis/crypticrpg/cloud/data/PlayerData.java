package me.neildennis.crypticrpg.cloud.data;

import me.neildennis.crypticrpg.player.CrypticPlayer;

public class PlayerData {

	public static void savePlayerData(CrypticPlayer pl){
		int hp = pl.getHealthManager().getCurrentHP();



		String query = "UPDATE player_db SET current_health = '" + hp + "' WHERE player_id = '"
				+ pl.getId().toString() + "'";
	}

	public static void saveInventory(CrypticPlayer pl){

	}

}
