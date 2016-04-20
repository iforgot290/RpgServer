package me.neildennis.crypticrpg.player;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.cloud.Cloud;
import me.neildennis.crypticrpg.health.HealthData;
import me.neildennis.crypticrpg.items.ItemData;
import me.neildennis.crypticrpg.moderation.ModerationData;
import me.neildennis.crypticrpg.permission.RankData;

public class CrypticPlayer {

	private UUID id;
	private OfflinePlayer player;

	private ArrayList<BukkitTask> tasks;
	private ArrayList<PlayerBuff> buffs;

	private HealthData healthData;
	private ModerationData moderationData;
	private ItemData itemData;
	private RankData rankData;

	public CrypticPlayer(UUID id){
		this.id = id;
		player = Bukkit.getOfflinePlayer(id);
		tasks = new ArrayList<BukkitTask>();
		buffs = new ArrayList<PlayerBuff>();

		Cloud.sendStatement("INSERT IGNORE INTO player_db (player_id) VALUES ('" + id.toString() + "')");

		ResultSet data = Cloud.sendQuery("SELECT * FROM player_db WHERE player_id = '" + id.toString() + "'");

		healthData = new HealthData(this, data);
		itemData = new ItemData(this, data);
		rankData = new RankData(this, data);
	}
	
	public void online(Player pl){
		healthData.online();
		itemData.online(pl);
		registerTasks();
	}

	private void registerTasks(){

		BukkitTask tickBuff = Bukkit.getScheduler().runTaskTimer(Cryptic.getPlugin(), new Runnable(){

			public void run(){
				for (PlayerBuff buff : buffs){
					buff.tickBuff();
					if (buff.isExpired()) buffs.remove(buff);
				}
			}

		}, 5L, 1L);

		tasks.add(tickBuff);
	}

	public void cancelTasks(){
		for (BukkitTask task : tasks){
			task.cancel();
		}

		tasks.clear();
	}

	public void cancelBuffs(){
		buffs.clear();
	}

	public UUID getId(){
		return id;
	}

	public Player getPlayer(){
		if (player.isOnline()){
			return player.getPlayer();
		} else {
			return null;
		}
	}

	public OfflinePlayer getOfflinePlayer(){
		return player;
	}

	public boolean isOnline(){
		return player.isOnline();
	}

	public void addTask(BukkitTask task){
		tasks.add(task);
	}

	public void addBuff(PlayerBuff buff){
		buffs.add(buff);
	}

	public HealthData getHealthData(){
		return healthData;
	}

	public ModerationData getModerationData(){
		return moderationData;
	}

	public ItemData getItemData(){
		return itemData;
	}
	
	public RankData getRankData(){
		return rankData;
	}

}
