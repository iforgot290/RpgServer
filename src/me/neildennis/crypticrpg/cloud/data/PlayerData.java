package me.neildennis.crypticrpg.cloud.data;

import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

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
	
	public static String getItemString(ItemStack stack){
		JsonArray main = new JsonArray();
		return null;
	}

}
