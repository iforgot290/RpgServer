package me.neildennis.crypticrpg.cloud.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonObject;

import me.neildennis.crypticrpg.cloud.Cloud;
import me.neildennis.crypticrpg.permission.Rank;
import me.neildennis.crypticrpg.player.CrypticPlayer;

public class PlayerData {

	public static void savePlayerData(CrypticPlayer pl){
		double hp = pl.getPlayer().getHealth();



		String query = "UPDATE player_db SET current_health = '" + hp + "', inventory = '" + getInventoryString(pl)
			+ "' WHERE player_id = '" + pl.getId().toString() + "'";
		Cloud.sendStatementAsync(query);
	}
	
	@Deprecated
	public static void savePlayerRank(CrypticPlayer pl){
		String query = "UPDATE player_db SET rank = '" + pl.getRankData().getRank().name() + "' WHERE player_id = '"
				+ pl.getId().toString() + "'";
		Cloud.sendStatementAsync(query);
	}
	
	public static void setRank(UUID pl, Rank rank) throws SQLException{
		Cloud.sendStatement("INSERT INTO player_db (player_id, rank) VALUES ('" + pl.toString() + "', '" + rank.name()
									+ "') ON DUPLICATE KEY UPDATE rank = '" + rank.name() + "'");
	}
	
	public static Rank getRank(UUID pl) throws SQLException{
		ResultSet result = Cloud.sendQuery("SELECT rank FROM player_db WHERE player_id = '" + pl.toString() + "'");
		
		if (!result.next()) return Rank.NORMAL;
		
		return Rank.valueOf(result.getString("rank"));
	}
	
	public static void banPlayer(UUID toban, String tobanName, UUID banner, String bannerName, String reason) throws SQLException{
		
		Cloud.sendStatement("INSERT INTO bans (banned_uuid, banned_name, banned_by_uuid, banned_by_name, reason) VALUES ('" + toban.toString() + "', '"
				+ tobanName + "', '" + (banner == null ? "" : banner.toString()) + "', '" + bannerName + "', '" + reason + "')");
	}

	private static String getInventoryString(CrypticPlayer pl){
		/*JsonArray array = new JsonArray();
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
		
		return array.toString();*/
		return "";
	}
	
	@SuppressWarnings("unused")
	private static JsonObject getDefaultSaveObject(ItemStack item){
		JsonObject obj = new JsonObject();
		
		obj.addProperty("type", "vanilla");
		obj.addProperty("material", item.getType().name());
		obj.addProperty("amount", item.getAmount());
		obj.addProperty("durability", item.getDurability());
		
		return obj;
	}

}
