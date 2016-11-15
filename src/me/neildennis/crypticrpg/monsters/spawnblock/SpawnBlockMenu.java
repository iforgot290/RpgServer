package me.neildennis.crypticrpg.monsters.spawnblock;

import org.bukkit.ChatColor;

import me.neildennis.crypticrpg.items.attribs.Rarity;
import me.neildennis.crypticrpg.items.type.CrypticItemType;
import me.neildennis.crypticrpg.menu.Menu;
import me.neildennis.crypticrpg.monsters.MobType;
import me.neildennis.crypticrpg.monsters.generator.MonsterGenerator;
import me.neildennis.crypticrpg.player.CrypticPlayer;

public class SpawnBlockMenu extends Menu{

	private State state = State.MAIN;
	private SpawnBlock blk;
	private SpawnBlockMonster mob;
	private MonsterGenerator gen;
	
	public SpawnBlockMenu(CrypticPlayer pl, SpawnBlock blk) {
		super(pl);
		this.blk = blk;
	}
	
	@Override
	public void display(){
		pl.getPlayer().sendMessage(new String[] {"", "", "", "", ""});
		
		switch (state){
		
		case LIST_MONSTER:
			pl.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Enter monster number");
			for (int i = 0; i < blk.getSpawns().size(); i++){
				SpawnBlockMonster spawn = blk.getSpawns().get(i);
				MonsterGenerator gen = spawn.getMonsterGen();
				pl.sendMessage(ChatColor.GRAY + "(" + i + ") " + gen.getType().name()
						+ " {elite=" + gen.isElite() + "},{respawn=" + spawn.getRespawnDelay() + "},{minlvl="
						+ gen.getMinLvl() + "},{maxlvl=" + gen.getMaxLvl() + "}");
			}
			break;
			
		case ADD_MONSTER:
			pl.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Enter monster type to add");
			StringBuilder sb = new StringBuilder();
			MobType[] types = MobType.values();
			for (int i = 0; i < types.length; i++){
				if (i < types.length - 1) sb.append(types[i].name() + ", ");
				else sb.append(types[i].name());
			}
			pl.sendMessage(sb.toString());
			break;
			
		case MOD_MONSTER:
			pl.sendMessage(ChatColor.YELLOW + "Editing monster: " + gen.getType().name());
			pl.sendMessage("");
			pl.sendMessage(ChatColor.GRAY + "elite=" + gen.isElite());
			pl.sendMessage(ChatColor.GRAY + "respawn=" + mob.getRespawnDelay());
			pl.sendMessage(ChatColor.GRAY + "name=" + gen.getName());
			pl.sendMessage(ChatColor.GRAY + "minlvl=" + gen.getMinLvl());
			pl.sendMessage(ChatColor.GRAY + "maxlvl=" + gen.getMaxLvl());
			pl.sendMessage(ChatColor.GRAY + "weapon=" + gen.getWeaponType());
			pl.sendMessage(ChatColor.GRAY + "armordrop=" + gen.getArmorDropChance(null));
			pl.sendMessage(ChatColor.GRAY + "rarity=" + gen.getGearRarity());
			break;
			
		case RANGE:
			pl.sendMessage(ChatColor.YELLOW + "Enter new range");
			break;
		
		default:
			pl.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Options:");
			pl.sendMessage(ChatColor.YELLOW + "ADD - add a monster");
			pl.sendMessage(ChatColor.YELLOW + "MOD - modify a monster");
			pl.sendMessage(ChatColor.YELLOW + "RANGE - change spawner range");
			pl.sendMessage(ChatColor.YELLOW + "EXIT - exit this menu");
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
			if (str.equalsIgnoreCase("exit")) {
				state = State.MAIN;
				break;
			}
			
			MobType type;
			try {
				type = MobType.valueOf(str.toUpperCase());
			} catch (IllegalArgumentException e){
				pl.sendMessage(ChatColor.RED + "Invalid mob type");
				return;
			}

			mob = new SpawnBlockMonster(new MonsterGenerator(type), 1000L);
			gen = mob.getMonsterGen();
			state = State.MOD_MONSTER;

			break;
			
		case MOD_MONSTER:
			if (str.equalsIgnoreCase("exit") || str.equalsIgnoreCase("done")){
				if (mob != null) blk.getSpawns().add(mob);
				state = State.MAIN;
				break;
			} else if (str.equalsIgnoreCase("delete")){
				mob = null;
				gen = null;
				state = State.MAIN;
				break;
			}
			
			String option = null;
			if (!str.contains("=")) return;
			option = str.split("=")[1];
			if (option == "\"\"") option = null;
			
			if (str.startsWith("name=")) gen.setName(option);
			else if (str.startsWith("elite=")) gen.setElite(Boolean.getBoolean(option));
			else if (str.startsWith("respawn=")) mob.setRespawnDelay(Long.valueOf(option));
			else if (str.startsWith("minlvl=")) gen.setLvlRange(Integer.parseInt(option), gen.getMaxLvl());
			else if (str.startsWith("maxlvl=")) gen.setLvlRange(gen.getMinLvl(), Integer.parseInt(option));
			else if (str.startsWith("weapon=")) gen.setWeaponType(CrypticItemType.valueOf(option.toUpperCase()));
			else if (str.startsWith("armordrop=")) gen.setArmorDropChance(Float.valueOf(option));
			else if (str.startsWith("rarity=")) gen.setGearRarity(Rarity.valueOf(option.toUpperCase()));
			else return;
			
			break;
		
		default:
			switch (str.toLowerCase()){
			
			case "range":
				state = State.RANGE;
				break;
			case "add":
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
		MAIN, ADD_MONSTER, MOD_MONSTER, LIST_MONSTER, RANGE;
	}

}
