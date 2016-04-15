package me.neildennis.crypticrpg.cloud.data;

import me.neildennis.crypticrpg.cloud.Cloud;
import me.neildennis.crypticrpg.player.CrypticPlayer;

public class PlayerData {

	public static void savePlayerData(CrypticPlayer pl){
		int hp = pl.getHealthData().getCurrentHP();



		String query = "UPDATE player_db SET current_health = '" + hp + "' WHERE player_id = '"
				+ pl.getId().toString() + "'";
		Cloud.sendStatementAsync(query);
	}

	public static void saveInventory(CrypticPlayer pl){

	}

}
