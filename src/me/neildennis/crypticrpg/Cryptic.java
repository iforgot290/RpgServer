package me.neildennis.crypticrpg;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import me.neildennis.crypticrpg.chat.ChatManager;
import me.neildennis.crypticrpg.cloud.Cloud;
import me.neildennis.crypticrpg.health.HealthListener;
import me.neildennis.crypticrpg.items.ItemManager;
import me.neildennis.crypticrpg.items.commands.TestCommand;
import me.neildennis.crypticrpg.menu.MenuManager;
import me.neildennis.crypticrpg.moderation.ModerationManager;
import me.neildennis.crypticrpg.monsters.MobManager;
import me.neildennis.crypticrpg.permission.RankManager;
import me.neildennis.crypticrpg.player.PlayerManager;

public class Cryptic extends JavaPlugin{
	
	private static Cryptic instance;
	
	private Cloud cloud;
	private PlayerManager playerManager;
	private ItemManager itemManager;
	private ModerationManager modManager;
	private ChatManager chatManager;
	private MobManager mobManager;
	private MenuManager menuManager;
	public static RankManager rankManager;
	
	private static World mainworld;
	
	public void onEnable(){
		instance = this;
		
		cloud = new Cloud();
		playerManager = new PlayerManager();
		itemManager = new ItemManager();
		modManager = new ModerationManager();
		chatManager = new ChatManager();
		mobManager = new MobManager();
		menuManager = new MenuManager();
		rankManager = new RankManager();
		
		
		mainworld = Bukkit.getWorld("Dungeonrealms");
		
		Bukkit.getPluginManager().registerEvents(new HealthListener(), getPlugin());
		getPlugin().getCommand("itemtest").setExecutor(new TestCommand());
	}
	
	public Cloud getCloud(){
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

	public static World getMainWorld() {
		return mainworld;
	}
	
	public static Cryptic getPlugin(){
		return instance;
	}

}
