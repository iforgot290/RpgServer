package me.neildennis.crypticrpg.professions.skill;

import me.neildennis.crypticrpg.cloud.data.SkillData;
import me.neildennis.crypticrpg.player.CrypticPlayer;

public class MiningSkill extends Skill {

	public MiningSkill(CrypticPlayer pl, SkillData data) {
		super(data, SkillType.MINING);
	}

	@Override
	public int getLevel() {
		return (int) (this.experience / 1000);
	}

	@Override
	public long getExpToNextLevel() {
		return this.experience % 1000L;
	}
	
	public long getExpRequired(int level) {
		return level * 100;
	}

}
