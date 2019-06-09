package me.neildennis.crypticrpg;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.neildennis.crypticrpg.chat.ChatManager;
import me.neildennis.crypticrpg.cloud.CloudManager;
import me.neildennis.crypticrpg.health.HealthListener;
import me.neildennis.crypticrpg.items.ItemManager;
import me.neildennis.crypticrpg.items.commands.TestCommand;
import me.neildennis.crypticrpg.menu.MenuManager;
import me.neildennis.crypticrpg.moderation.ModerationManager;
import me.neildennis.crypticrpg.monsters.MobManager;
import me.neildennis.crypticrpg.permission.RankManager;
import me.neildennis.crypticrpg.player.PlayerManager;
import me.neildennis.crypticrpg.professions.ProfessionManager;
import me.neildennis.crypticrpg.utils.CustomFlags;
import me.neildennis.crypticrpg.utils.Log;
import me.neildennis.crypticrpg.zone.ZoneManager;

public class Cryptic extends JavaPlugin {
	
	private static Cryptic instance;
	
	private ArrayList<Manager> managers;
	
	private CloudManager cloud;
	private PlayerManager playerManager;
	private ItemManager itemManager;
	private ModerationManager modManager;
	private ChatManager chatManager;
	private MobManager mobManager;
	private MenuManager menuManager;
	public static RankManager rankManager;
	private ZoneManager zoneManager;
	private ProfessionManager professionManager;
	
	private static World mainworld;
	private static boolean enabled = false;
	
	public void onLoad(){
		CustomFlags.loadFlags();
	}
	
	public void onEnable(){
		instance = this;
		managers = new ArrayList<Manager>();
		mainworld = Bukkit.getWorld("Dungeonrealms");
		
		managers.add(cloud = new CloudManager());
		managers.add(playerManager = new PlayerManager());
		managers.add(itemManager = new ItemManager());
		managers.add(modManager = new ModerationManager());
		managers.add(chatManager = new ChatManager());
		managers.add(mobManager = new MobManager());
		managers.add(menuManager = new MenuManager());
		managers.add(rankManager = new RankManager());
		managers.add(zoneManager = new ZoneManager());
		managers.add(professionManager = new ProfessionManager());
		
		for (Manager manager : managers) {
			try {
				manager.onEnable();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		registerEvents(new HealthListener());
		registerCommand("item", new TestCommand());
		
		enabled = true;
	}
	
	public void onDisable(){
		
		for (Manager manager : managers) {
			manager.onDisable();
		}
		
		managers.clear();
		managers = null;
		
		enabled = false;
	}
	
	public static void registerCommand(String label, CrypticCommand cmd){
		Log.debug("Registering new command: /" + label);
		getPlugin().getCommand(label).setExecutor(cmd);
	}
	
	public static void registerEvents(Listener listener){
		Log.debug("Registering events: " + listener.getClass().getCanonicalName());
		Bukkit.getPluginManager().registerEvents(listener, getPlugin());
	}
	
	public static boolean rpgEnabled(){
		return enabled;
	}
	
	public CloudManager getCloud(){
		return cloud;
	}
	
	public PlayerManager getPlayerManager(){
		return playerManager;
	}
	
	public ItemManager getItemManager(){
		return itemManager;
	}
	
	public ModerationManager getModerationManager(){
		return modManager;
	}
	
	public ChatManager getChatManager(){
		return chatManager;
	}
	
	public MobManager getMobManager(){
		return mobManager;
	}
	
	public MenuManager getMenuManager(){
		return menuManager;
	}
	
	public ZoneManager getZoneManager(){
		return zoneManager;
	}
	
	public ProfessionManager getProfessionManager(){
		return professionManager;
	}

	public static World getMainWorld() {
		return mainworld;
	}
	
	public static Cryptic getPlugin(){
		return instance;
	}

}
