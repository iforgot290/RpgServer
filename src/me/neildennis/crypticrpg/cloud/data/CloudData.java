package me.neildennis.crypticrpg.cloud.data;

import java.sql.SQLException;

public interface CloudData {
	
	/**
	 * Saves the data present in the class to the SQL server
	 * @throws SQLException
	 */
	public abstract void save() throws SQLException;
	
	/**
	 * Updates the data in the class with current data from this instance of the server
	 */
	public abstract void update();
	
	/**
	 * Loads data into this class from the SQL server
	 * @throws SQLException
	 */
	public abstract void load() throws SQLException;

}
