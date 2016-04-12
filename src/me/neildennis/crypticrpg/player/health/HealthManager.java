package me.neildennis.crypticrpg.player.health;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.scheduler.BukkitTask;

import me.neildennis.crypticrpg.player.CrypticPlayer;
import minecade.dungeonrealms.Main;
import minecade.dungeonrealms.LevelMechanics.LevelMechanics;
import minecade.dungeonrealms.LevelMechanics.PlayerLevel;

public class HealthManager {
	
	private CrypticPlayer cp;
	
	private BossBar bar;
	
	private static long combatCooldown = 10 * 1000;
	private long lasthit = 0;
	
	private int currentHP = 50;
	private int maxHP = 50;
	private int regen = 0;
	
	private boolean god = false;
	
	public HealthManager(CrypticPlayer cp){
		this.cp = cp;
	}
	
	public void registerTasks(){
		BukkitTask healthRegen = Bukkit.getScheduler().runTaskTimer(Main.plugin, new Runnable(){
			
			@Override
			public void run(){
				if (getCurrentHP() <= 0 || cp.getPlayer().getHealth() <= 0) return;
				if (isStarving()) return;
				
				if (!isInCombat()){
					int max = getMaxHP();
					int current = getCurrentHP();
					int toheal = 5 + getRegenAmount();
					
					if (current >= max) return;
					
					if (current + toheal >= max){
						cp.getPlayer().setHealth(20);
						setCurrentHP(max);
					}
					
					else {
						setCurrentHP(current + toheal);
						int hearts = (current * 20) / max;
						if (hearts == 0) hearts++;
						cp.getPlayer().setHealth(hearts);
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
	
	public int getMaxHP(){
		return maxHP;
	}
	
	public void setCurrentHP(int hp){
		this.currentHP = hp;
	}
	
	public int getCurrentHP(){
		return currentHP;
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
	
	public void updateOverheadHP(){
		if (!cp.isOnline()) return;
		
		if (bar == null){
			bar = Bukkit.createBossBar("Loading", BarColor.PURPLE, BarStyle.SOLID);
			bar.addPlayer(cp.getPlayer());
		}
		
		double percent = ((double) getCurrentHP() / (double) getMaxHP());
		if (percent > 1.0){
			percent = 1.0;
		}
		
		PlayerLevel lvl = LevelMechanics.getPlayerData(cp.getPlayer());
		if (lvl != null){
			String lvldata = ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "LVL "
					+ ChatColor.AQUA.toString() + lvl.getLevel();
			
			int xplvl = (int) Math.floor(((lvl.getXP() * 1D) / (lvl.getEXPNeeded(lvl.getLevel()) * 1D)) * 100);
			String xpdata = ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "XP "
					+ ChatColor.GREEN.toString() + xplvl + "%";
			
			String dash = ChatColor.BLACK.toString() + ChatColor.BOLD + " - ";
			
			bar.setTitle(lvldata + dash + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "HP "
								+ ChatColor.LIGHT_PURPLE + getCurrentHP() + ChatColor.BOLD.toString() + " / "
								+ ChatColor.LIGHT_PURPLE.toString() + getMaxHP() + dash + xpdata);
			bar.setProgress((float) (percent * 100F));
		}
		
		else {
			bar.setTitle(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "HP " + ChatColor.LIGHT_PURPLE
						+ getCurrentHP() + ChatColor.BOLD.toString() + " / "
						+ ChatColor.LIGHT_PURPLE.toString() + getMaxHP());
			bar.setProgress((float) (percent * 100F));
		}
	}
	
	public String getPipeHealthBar(){
		int max_bar = 30;
		int current = getCurrentHP();
		int max = getMaxHP();

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
