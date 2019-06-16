package me.neildennis.crypticrpg.cloud.data;

import java.sql.SQLException;

public interface CloudData {
	
	public abstract void save() throws SQLException;
	public abstract void update();
	public abstract void load() throws SQLException;

}
