package minecade.dungeonrealms.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import minecade.dungeonrealms.models.PlayerModel;

public class PlayerManager implements Listener {

	private static HashMap<UUID, PlayerModel> models = new HashMap<UUID, PlayerModel>();

	/**
	 * Gets the player model associated with that UUID
	 * @param uuid ID to get player model for
	 * @return player model
	 */
	public static PlayerModel getPlayerModel(UUID uuid){
		if(!models.containsKey(uuid)) models.put(uuid, new PlayerModel(uuid));
		return models.get(uuid);
	}
	
	/**
	 * Gets the player model associated with that player
	 * @param player player to get player model for
	 * @return player model
	 */
	public static PlayerModel getPlayerModel(Player player){
		return getPlayerModel(player.getUniqueId());
	}
	
	/**
	 * Gets all player models
	 * @return all player models
	 */
	public static List<PlayerModel> getPlayerModels() {
		return new ArrayList<PlayerModel>(models.values());
	}

}
