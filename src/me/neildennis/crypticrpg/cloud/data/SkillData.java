package me.neildennis.crypticrpg.cloud.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.professions.skill.Skill.SkillType;

public class SkillData implements CloudData {

	private CrypticPlayer pl;
	
	private long experience;
	
	public SkillData(CrypticPlayer pl) throws SQLException {
		this.pl = pl;
		load();
	}
	
	@Override
	public void save() throws SQLException {
		
	}

	@Override
	public void update() {
		
	}

	@Override
	public void load() throws SQLException {
		
	}
	
	public long getExperience(SkillType type) throws SQLException {
		return experience;
	}

}
