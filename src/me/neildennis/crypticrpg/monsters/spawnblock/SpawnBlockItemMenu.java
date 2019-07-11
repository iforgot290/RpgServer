package me.neildennis.crypticrpg.monsters.spawnblock;

import org.bukkit.inventory.ItemStack;

import me.neildennis.crypticrpg.menu.ItemMenu;
import me.neildennis.crypticrpg.menu.options.CommandOption;
import me.neildennis.crypticrpg.menu.options.MenuOption;
import me.neildennis.crypticrpg.monsters.MonsterContainer;
import me.neildennis.crypticrpg.player.CrypticPlayer;

public class SpawnBlockItemMenu extends ItemMenu {
	
	private SpawnBlock block;
	
	public SpawnBlockItemMenu(SpawnBlock block) {
		this.block = block;
		
		this.setName("Spawner Menu").setSlots(45).setVerticalPadding(1);
		
		for (MonsterContainer monster : block.getAllSpawns()) {
			this.addOption(new MonsterOption(monster));
		}
		
		this.addOption(new CommandOption("ping", "Ping", "test command"));
	}
	
	private static class MonsterOption extends MenuOption {

		private MonsterContainer monster;
		
		private MonsterOption(MonsterContainer monster) {
			this.monster = monster;
		}
		
		@Override
		public void activate(CrypticPlayer pl) {
			
		}

		@Override
		public ItemStack getItem() {
			return new ItemStack(monster.getMonsterGen().getType().getIcon());
		}
		
	}

}
