package me.neildennis.crypticrpg.monsters.spawnblock;

import org.bukkit.ChatColor;
import me.neildennis.crypticrpg.menu.Menu;
import me.neildennis.crypticrpg.monsters.MobType;
import me.neildennis.crypticrpg.monsters.SpawnBlock;
import me.neildennis.crypticrpg.monsters.templates.SpawnTemplate;
import me.neildennis.crypticrpg.player.CrypticPlayer;

public class SpawnBlockMenu extends Menu{

	private State state = State.MAIN;
	private SpawnBlock blk;
	private SpawnTemplate mob;
	
	public SpawnBlockMenu(CrypticPlayer pl, SpawnBlock blk) {
		super(pl);
		this.blk = blk;
	}
	
	@Override
	public void display(){
		pl.sendMessage("");
		
		switch (state){
		
		case LIST_MONSTER:
			pl.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Enter monster number");
			for (int i = 0; i < blk.getSpawns().size(); i++){
				SpawnTemplate spawn = blk.getSpawns().get(i);
				pl.sendMessage(ChatColor.GRAY + "(" + i + ") " + spawn.getType().name()
						+ " {elite=" + spawn.isElite() + "},{respawn=" + spawn.getRespawnDelay() + "}");
			}
			break;
			
		case ADD_MONSTER:
			pl.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Enter monster type to add");
			pl.sendMessage(MobType.values().toString());
			break;
			
		case MOD_MONSTER:
			pl.sendMessage(ChatColor.YELLOW + "Editing monster: " + mob.getType().name());
			pl.sendMessage("");
			pl.sendMessage(ChatColor.GRAY + "elite=" + mob.isElite());
			pl.sendMessage(ChatColor.GRAY + "respawn=" + mob.getRespawnDelay());
			pl.sendMessage(ChatColor.GRAY + "name=" + mob.getName());
			break;
			
		case RANGE:
			pl.sendMessage(ChatColor.YELLOW + "Enter new range");
			break;
			
		case LEVEL:
			pl.sendMessage(ChatColor.YELLOW + "Enter new level range");
			break;
		
		default:
			pl.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Options:");
			pl.sendMessage(ChatColor.YELLOW + "RANGE - change spawner range");
			pl.sendMessage(ChatColor.YELLOW + "LEVEL - change spawner level range");
			pl.sendMessage(ChatColor.YELLOW + "MONSTER - add a monster");
			pl.sendMessage(ChatColor.YELLOW + "MOD - modify a monster");
			break;
		
		}
	}

	@Override
	public void input(String str) {
		switch (state){
		
		case RANGE:
			try {
				blk.setRange(Integer.parseInt(str));
				state = State.MAIN;
				pl.sendMessage(ChatColor.GREEN + "Success");
			} catch (Exception e){
				pl.sendMessage(ChatColor.RED + "Invalid number");
			}
			break;
			
		case LEVEL:
			String[] args = str.split("-");
			if (args.length != 2){
				pl.sendMessage(ChatColor.RED + "Error: format is 'xx-yy'");
				break;
			}
			
			try {
				blk.setLevel(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
				state = State.MAIN;
				pl.sendMessage(ChatColor.GREEN + "Success");
			} catch (Exception e){
				pl.sendMessage(ChatColor.RED + "Error: format is 'xx-yy'");
			}
			
			break;
			
		case LIST_MONSTER:
			if (str.equalsIgnoreCase("exit")){
				state = State.MAIN;
				break;
			}
			try {
				int i = Integer.parseInt(str);
				mob = blk.getSpawns().get(i);
				
				if (mob == null){
					pl.sendMessage(ChatColor.RED + "Invalid mob");
				} else {
					blk.getSpawns().remove(i);
					state = State.MOD_MONSTER;
				}
			} catch (Exception e){
				pl.sendMessage(ChatColor.RED + "Invalid number");
			}
			break;
			
		case ADD_MONSTER:
			MobType type = MobType.valueOf(str);
			
			if (type == null){
				pl.sendMessage(ChatColor.RED + "Invalid mob type");
				break;
			}
			
			try {
				mob = type.getHandleClass().newInstance();
				state = State.MOD_MONSTER;
			} catch (InstantiationException | IllegalAccessException e) {
				pl.sendMessage(ChatColor.RED + "Error getting class instance");
				e.printStackTrace();
			}
			
			break;
			
		case MOD_MONSTER:
			if (str.equalsIgnoreCase("exit")){
				if (mob != null) blk.getSpawns().add(mob);
				state = State.MAIN;
				break;
			} else if (str.equalsIgnoreCase("delete")){
				mob = null;
				state = State.MAIN;
				break;
			}
			
			if (str.startsWith("name=")) mob.setName(str.split("=")[1]);
			else if (str.startsWith("elite=")) mob.setElite(Boolean.getBoolean(str.split("=")[1]));
			else if (str.startsWith("respawn=")) mob.setRespawnDelay(Long.valueOf(str.split("=")[1]));
			
			break;
		
		default:
			switch (str.toLowerCase()){
			
			case "range":
				state = State.RANGE;
				break;
			case "level":
				state = State.LEVEL;
				break;
			case "monster":
				state = State.ADD_MONSTER;
				break;
			case "mod":
				state = State.LIST_MONSTER;
				break;
			case "exit":
				pl.sendMessage(ChatColor.GREEN + "Exiting...");
				pl.clearMenu();
				return;
			case "save":
				pl.sendMessage("not implemented");
				break;
				
			default:
				pl.sendMessage(ChatColor.RED + "Invalid menu option");
				return;
			
			}
			break;
		
		}
		
		display();
	}
	
	private enum State{
		MAIN, ADD_MONSTER, MOD_MONSTER, LIST_MONSTER, RANGE, LEVEL;
	}

}
