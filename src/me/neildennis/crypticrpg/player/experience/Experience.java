package me.neildennis.crypticrpg.player.experience;

import me.neildennis.crypticrpg.cloud.data.SkillData;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.professions.skill.MiningSkill;

public class Experience {
	
	private MiningSkill mining;
	
	public Experience(CrypticPlayer pl, SkillData data) {
		mining = new MiningSkill(pl, data);
	}
	
	public MiningSkill getMiningSkill() {
		return mining;
	}

}
