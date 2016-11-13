package me.neildennis.crypticrpg.health;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.items.attribs.AttributeType;
import me.neildennis.crypticrpg.player.CrypticPlayer;

public class HealthData {

	private CrypticPlayer cp;
	private Player p;

	private BossBar bar;

	private static long combatCooldown = 10 * 1000;
	private long lasthit = 0;
	private int regen = 0;
	private int lastHP = 50;

	private boolean god = false;

	public HealthData(CrypticPlayer cp, ResultSet data){
		this.cp = cp;

		try {
			lastHP = data.getInt("current_health");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void online(Player pl){
		p = pl;
		p.setHealth(lastHP);
		updateOverheadHP();
		registerTasks();
	}

	private void registerTasks(){
		BukkitTask healthRegen = Bukkit.getScheduler().runTaskTimer(Cryptic.getPlugin(), new Runnable(){

			@Override
			public void run(){
				if (p.getHealth() <= 0) return;
				if (isStarving()) return;

				if (!isInCombat()){
					double max = p.getMaxHealth();
					double current = p.getHealth();
					int toheal = 5 + getRegenAmount();

					if (current >= max) return;

					if (current + toheal >= max){
						p.setHealth(max);
					} else {
						p.setHealth(current + toheal);
					}

					updateOverheadHP();
				}
			}

		}, 20L, 20L);

		cp.addTask(healthRegen);
	}

	public boolean isStarving(){
		return cp.getPlayer().getFoodLevel() <= 0;
	}

	public int getRegenAmount(){
		return regen;
	}

	public void damage(){
		lasthit = System.currentTimeMillis();
	}

	public boolean isInCombat(){
		return System.currentTimeMillis() - lasthit <= combatCooldown;
	}

	public void setLastHit(long time){
		this.lasthit = time;
	}

	public boolean isGodMode(){
		return god;
	}

	public void setGodMode(boolean god){
		this.god = god;
	}
	
	public void updateHealth(){
		p.setMaxHealth(cp.getAttribute(AttributeType.HEALTH).genValue());
	}

	public void updateOverheadHP(){
		if (!cp.isOnline()) return;

		if (bar == null){
			bar = Bukkit.createBossBar("Loading", BarColor.PURPLE, BarStyle.SOLID);
			bar.addPlayer(cp.getPlayer());
		}

		double percent = (p.getHealth() / p.getMaxHealth());
		if (percent > 1.0){
			percent = 1.0;
		}

		bar.setTitle(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Health " + ChatColor.LIGHT_PURPLE
				+ (int) p.getHealth() + ChatColor.BOLD.toString() + " / "
				+ ChatColor.LIGHT_PURPLE.toString() + (int) p.getMaxHealth());
		bar.setProgress(percent);
	}

	public String getPipeHealthBar(){
		int max_bar = 30;
		double current = p.getHealth();
		double max = p.getMaxHealth();

		ChatColor cc = null;

		DecimalFormat df = new DecimalFormat("##.#");
		double percent_hp = (double) (Math.round(100.0D * Double.parseDouble((df.format((current / max))))));

		if (percent_hp <= 0 && current > 0) {
			percent_hp = 1;
		}

		double percent_interval = (100.0D / max_bar);
		int bar_count = 0;

		cc = ChatColor.GREEN;
		if (percent_hp <= 40) {
			cc = ChatColor.YELLOW;
		}
		if (percent_hp <= 15) {
			cc = ChatColor.RED;
		}

		String return_string = cc.toString() + "";

		while (percent_hp > 0 && bar_count < max_bar) {
			percent_hp -= percent_interval;
			bar_count++;
			return_string += "|";
		}

		return_string += ChatColor.BLACK.toString();

		while (bar_count < max_bar) {
			return_string += "|";
			bar_count++;
		}

		return return_string;
	}

}
