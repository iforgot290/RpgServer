package me.neildennis.crypticrpg.professions.skill;

import me.neildennis.crypticrpg.cloud.data.SkillData;
import me.neildennis.crypticrpg.player.CrypticPlayer;

public class MiningSkill extends Skill {

	public MiningSkill(CrypticPlayer pl, SkillData data) {
		super(data, SkillType.MINING);
	}

	@Override
	public int getLevel() {
		return (int) (this.experience / 100);
	}

	@Override
	public long getExpToNextLevel() {
		return this.experience % 100L;
	}

}
