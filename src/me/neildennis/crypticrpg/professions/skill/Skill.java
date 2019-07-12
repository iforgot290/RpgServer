package me.neildennis.crypticrpg.professions.skill;

import me.neildennis.crypticrpg.cloud.data.SkillData;

public abstract class Skill {
	
	private SkillData data;
	private SkillType type;
	
	protected long experience;
	
	public Skill(SkillData data, SkillType type) {
		this.data = data;
		this.type = type;
	}
	
	public abstract int getLevel();
	public abstract long getExpToNextLevel();
	
	public SkillType getType() {
		return type;
	}
	
	public long getExperience() {
		return experience;
	}
	
	public void setExperience(long experience) {
		this.experience = experience >= 0 ? experience : 0;
	}
	
	public void addExperience(long experience) {
		this.experience += experience;
	}
	
	public static enum SkillType {
		MINING;
	}
}
