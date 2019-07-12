package me.neildennis.crypticrpg.cloud.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.professions.skill.Skill.SkillType;

public class SkillData implements CloudData {

	private CrypticPlayer pl;
	private ResultSet result;
	
	public SkillData(CrypticPlayer pl) {
		this.pl = pl;
	}
	
	@Override
	public void save() throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void load() throws SQLException {
		// TODO Auto-generated method stub

	}
	
	public long getExperience(SkillType type) throws SQLException {
		return result.getLong(type.toString().toLowerCase());
	}

}
