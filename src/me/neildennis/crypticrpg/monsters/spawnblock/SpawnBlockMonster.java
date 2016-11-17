package me.neildennis.crypticrpg.monsters.spawnblock;

import me.neildennis.crypticrpg.moderation.MonsterSpawner;
import me.neildennis.crypticrpg.monsters.MonsterContainer;
import me.neildennis.crypticrpg.monsters.SpawnType;
import me.neildennis.crypticrpg.monsters.generator.MonsterGenerator;

public class SpawnBlockMonster extends MonsterContainer{

	public SpawnBlockMonster(MonsterGenerator gen, long respawnDelay, MonsterSpawner spawner) {
		super(gen, respawnDelay, SpawnType.SPAWNBLOCK, spawner);
	}

}
