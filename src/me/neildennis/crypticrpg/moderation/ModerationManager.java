package me.neildennis.crypticrpg.moderation;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.Manager;
import me.neildennis.crypticrpg.chat.ChatManager;
import me.neildennis.crypticrpg.cloud.CloudManager;
import me.neildennis.crypticrpg.moderation.ModerationData.Ban;
import me.neildennis.crypticrpg.moderation.commands.CommandBan;
import me.neildennis.crypticrpg.moderation.commands.CommandBanInfo;

public class ModerationManager extends Manager {

	@Override
	public void onEnable() {
		Cryptic.registerEvents(new ModerationListener(this));
		
		Cryptic.registerCommand("ban", new CommandBan());
		Cryptic.registerCommand("baninfo", new CommandBanInfo());
	}
	
	@Override
	public void onDisable() {
		
	}
	
	@Override
	public void registerTasks() {
		
	}
	
	/**
	 * Gets the current ban for a player based on UUID. This method is blocking.
	 * @param id UUID of player to retrieve ban for
	 * @return Current ban for given id or null if no ban is found
	 * @throws SQLException
	 */
	public Ban getCurrentBan(UUID id) throws SQLException {
		
		ResultSet set = CloudManager.sendQuery("SELECT * FROM bans WHERE `player_uuid` = '" + id.toString() + "' AND `valid` = '1'");
		
		if (!set.next()) return null;
		
		UUID enforcerId = UUID.fromString(set.getString("enforcer_uuid"));
		String reason = set.getString("reason");
		boolean banned = set.getBoolean("valid");
		long banTime = set.getLong("ban_time");
		long unbanTime = set.getLong("unban_time");
		
		return new Ban(enforcerId, reason, banned, banTime, unbanTime);
	}
	
	/**
	 * Bans the player specified by inserting the information into the SQL database. This method is blocking.
	 * @param player Player to ban
	 * @param reason Reason for banning player
	 * @param enforcer Person banning the given player
	 * @throws SQLException
	 */
	public void banPlayer(OfflinePlayer player, String reason, OfflinePlayer enforcer) throws SQLException {
		PreparedStatement statement = CloudManager.getPreparedStatement("INSERT INTO `bans`"
				+ "(`id`, `player_uuid`, `player_name`, `valid`, `ban_time`, `enforcer_uuid`, `enforcer_name`, `unban_time`, `reason`) "
				+ "VALUES (NULL, '?', '?', '1', '?', '?', '?', '0', '?');");
		
		statement.setString(1, player.getUniqueId().toString());
		statement.setString(2, player.getName());
		statement.setLong(3, System.currentTimeMillis());
		statement.setString(4, enforcer.getUniqueId().toString());
		statement.setString(5, enforcer.getName());
		statement.setString(6, reason);
		
		statement.execute();
	}

	/**
	 * Returns a formatted message to display when a player is banned
	 * @param reason Reason given for ban
	 * @return Formatted message
	 */
	public String getKickedBannedMessage(String reason){
		return "Banned: " + reason;
	}

	public String getKickedMessage(String reason){
		return "Kicked: " + reason;
	}

	public void kickPlayer(Player player, String reason){

	}

}
