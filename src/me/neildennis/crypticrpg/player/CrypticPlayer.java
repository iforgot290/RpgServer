package me.neildennis.crypticrpg.player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.cloud.Cloud;
import me.neildennis.crypticrpg.health.CachedStats;
import me.neildennis.crypticrpg.health.HealthData;
import me.neildennis.crypticrpg.items.attribs.Attribute;
import me.neildennis.crypticrpg.items.attribs.AttributeType;
import me.neildennis.crypticrpg.menu.Menu;
import me.neildennis.crypticrpg.moderation.ModerationData;
import me.neildennis.crypticrpg.permission.Rank;
import me.neildennis.crypticrpg.permission.RankData;
import me.neildennis.crypticrpg.zone.Region;
import me.neildennis.crypticrpg.zone.ZoneManager;
import me.neildennis.crypticrpg.zone.ZoneManager.ZoneState;

public class CrypticPlayer {

	private UUID id;
	private OfflinePlayer player;

	private ArrayList<BukkitTask> tasks;
	private ArrayList<PlayerBuff> buffs;

	private HealthData healthData;
	private ModerationData moderationData;
	private RankData rankData;
	private CachedStats stats;

	private ZoneState zonestate;
	private Region region;

	private Menu menu;

	/**
	 * Loads asynchronously
	 * @param id ID of player logging on
	 */
	public CrypticPlayer(UUID id, ModerationData moddata){
		this.id = id;
		player = Bukkit.getOfflinePlayer(id);
		tasks = new ArrayList<BukkitTask>();
		buffs = new ArrayList<PlayerBuff>();

		try {
			Cloud.sendStatement("INSERT IGNORE INTO player_db (player_id, rank) VALUES ('" + id.toString() + "', 'NORMAL')");
			ResultSet data = Cloud.sendQuery("SELECT * FROM player_db WHERE player_id = '" + id.toString() + "'");
			data.next();

			healthData = new HealthData(this, data);
			stats = new CachedStats(this);
			rankData = new RankData(this, data);
			moderationData = moddata;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void online(Player pl){
		stats.online();
		healthData.online(pl);
		ZoneManager.checkZone(this, pl.getLocation());
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
	
	public void addMaxHealth(double health){
		getPlayer().setMaxHealth(getPlayer().getMaxHealth() - health);
	}
	
	public void subtractMaxHealth(double health){
		getPlayer().setMaxHealth(getPlayer().getMaxHealth() - health);
	}
	
	public ItemStack getHand(){
		return getPlayer().getInventory().getItemInMainHand();
	}

	public void sendMessage(String str){
		if (getPlayer() != null){
			getPlayer().sendMessage(str);
		}
	}

	public void cancelBuffs(){
		buffs.clear();
	}
	
	public void setTown(Region region){
		this.region = region;
	}
	
	public Region getTown(){
		return region;
	}

	public Menu getCurrentMenu(){
		return menu;
	}

	public void setMenu(Menu menu){
		this.menu = menu;
	}

	public void clearMenu(){
		this.menu = null;
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

	public ZoneState getZoneState(){
		return zonestate;
	}

	public void setZoneState(ZoneState zonestate){
		this.zonestate = zonestate;
	}

	public HealthData getHealthData(){
		return healthData;
	}

	public ModerationData getModerationData(){
		return moderationData;
	}

	public RankData getRankData(){
		return rankData;
	}

	public Rank getRank(){
		return rankData.getRank();
	}
	
	public boolean hasPermission(Rank rank){
		return getRank().getPriority() >= rank.getPriority();
	}
	
	public CachedStats getStats(){
		return stats;
	}
	
	public Attribute getAttribute(AttributeType type){
		return stats.getAttribute(type);
	}

}
