package me.neildennis.crypticrpg.cloud.data;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import me.neildennis.crypticrpg.cloud.Cloud;
import me.neildennis.crypticrpg.items.custom.CrypticItem;
import me.neildennis.crypticrpg.player.CrypticPlayer;

public class PlayerData {

	public static void savePlayerData(CrypticPlayer pl){
		double hp = pl.getPlayer().getHealth();



		String query = "UPDATE player_db SET current_health = '" + hp + "', inventory = '" + getInventoryString(pl)
			+ "' WHERE player_id = '" + pl.getId().toString() + "'";
		Cloud.sendStatementAsync(query);
	}
	
	public static void savePlayerRank(CrypticPlayer pl){
		String query = "UPDATE player_db SET rank = '" + pl.getRankData().getRank().name() + "' WHERE player_id = '"
				+ pl.getId().toString() + "'";
		Cloud.sendStatementAsync(query);
	}

	private static String getInventoryString(CrypticPlayer pl){
		JsonArray array = new JsonArray();
		Inventory inv = pl.getPlayer().getInventory();
		
		for (int x = 0; x < inv.getSize(); x++){
			ItemStack item = inv.getItem(x);
			if (item == null) continue;
			
			CrypticItem cr = pl.getItemData().getCrypticItem(item);
			JsonObject obj;
			if (cr == null) {
				obj = getDefaultSaveObject(item);
			} else {
				obj = cr.saveToJson();
				obj.addProperty("type", cr.getType().name());
			}
			
			
			obj.addProperty("slot", x);
			array.add(obj);
		}
		
		return array.toString();
	}
	
	private static JsonObject getDefaultSaveObject(ItemStack item){
		JsonObject obj = new JsonObject();
		
		obj.addProperty("type", "vanilla");
		obj.addProperty("material", item.getType().name());
		obj.addProperty("amount", item.getAmount());
		obj.addProperty("durability", item.getDurability());
		
		return obj;
	}

}
