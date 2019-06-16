package me.neildennis.crypticrpg.cloud.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonObject;

import me.neildennis.crypticrpg.cloud.CloudManager;
import me.neildennis.crypticrpg.permission.Rank;
import me.neildennis.crypticrpg.player.CrypticPlayer;

public class PlayerData implements CloudData {
	
	private CrypticPlayer pl;
	
	private Rank rank = Rank.NORMAL;
	private double health = 50.0D;
	
	private boolean firstJoin = false;
	
	public PlayerData(CrypticPlayer pl) {
		this.pl = pl;
	}
	
	/**
	 * Loads player data from the SQL server<br><br>
	 * This method is blocking
	 */
	public void load() throws SQLException {
		ResultSet result = CloudManager.sendQuery("SELECT * FROM player_db WHERE player_id = '" + pl.getId().toString() + "'");
		
		// If player isn't found then save default data to database
		if (!result.next()) {
			firstJoin = true;
			save();
			return;
		}
		
		String strRank = result.getString("rank");
		if (strRank == null || strRank.equalsIgnoreCase("")) rank = Rank.NORMAL;
		else rank = Rank.valueOf(strRank);
		
		health = result.getDouble("current_health");
	}
	
	public void update() {
		rank = pl.getRank();
	}
	
	public void save() throws SQLException {
		CloudManager.sendQuery("");
	}
	
	/**
	 * Gets the saved rank of a player as loaded from the SQL server. This value is only valid upon player logging in.
	 * @return Saved player rank
	 */
	public Rank getRank() {
		return rank;
	}
	
	/**
	 * Gets the saved health of a player as loaded from the SQL server. This value is only valid upon player logging in.
	 * @return Saved player health
	 */
	public double getHealth() {
		return health;
	}
	
	/**
	 * Returns whether or not this player is joining for the first time or not.
	 * @return true if this is the player's first join, false otherwise
	 */
	public boolean isFirstJoin() {
		return firstJoin;
	}

	public static void savePlayerData(CrypticPlayer pl){
		double hp = pl.getPlayer().getHealth();



		String query = "UPDATE player_db SET current_health = '" + hp + "', inventory = '" + getInventoryString(pl)
			+ "' WHERE player_id = '" + pl.getId().toString() + "'";
		CloudManager.sendStatementAsync(query);
	}
	
	@Deprecated
	public static void savePlayerRank(CrypticPlayer pl){
		String query = "UPDATE player_db SET rank = '" + pl.getRankData().getRank().name() + "' WHERE player_id = '"
				+ pl.getId().toString() + "'";
		CloudManager.sendStatementAsync(query);
	}
	
	public static void setRank(UUID pl, Rank rank) throws SQLException{
		CloudManager.sendStatement("INSERT INTO player_db (player_id, rank) VALUES ('" + pl.toString() + "', '" + rank.name()
									+ "') ON DUPLICATE KEY UPDATE rank = '" + rank.name() + "'");
	}
	
	public static Rank getRank(UUID pl) throws SQLException{
		ResultSet result = CloudManager.sendQuery("SELECT rank FROM player_db WHERE player_id = '" + pl.toString() + "'");
		
		if (!result.next()) return Rank.NORMAL;
		
		return Rank.valueOf(result.getString("rank"));
	}
	
	public static void banPlayer(UUID toban, String tobanName, UUID banner, String bannerName, String reason) throws SQLException{
		
		CloudManager.sendStatement("INSERT INTO bans (player_uuid, player_name, ban_time, enforcer_uuid, enforcer_name, reason) VALUES ('" + toban.toString() + "', '"
				+ tobanName + "', '" + System.currentTimeMillis() + "', '" + (banner == null ? "" : banner.toString()) + "', '" + bannerName + "', '" + reason + "')");
	}
	
	public static ResultSet getBans(UUID banned) throws SQLException{
		return CloudManager.sendQuery("SELECT * FROM bans WHERE player_uuid = '" + banned.toString() + "'");
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
