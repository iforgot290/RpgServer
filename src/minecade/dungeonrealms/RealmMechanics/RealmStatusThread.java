package minecade.dungeonrealms.RealmMechanics;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import minecade.dungeonrealms.database.ConnectionPool;

public class RealmStatusThread extends Thread {

	public void run() {
		while (true) {
			try {
				Thread.sleep(250);
			} catch (Exception err) {
			}
			for (UUID id : RealmMechanics.async_realm_status) {
				PreparedStatement pst = null;

				try {
					pst = ConnectionPool.getConnection().prepareStatement(
							"SELECT realm_loaded FROM player_database WHERE p_name = '" + id.toString() + "'");

					pst.execute();
					ResultSet rs = pst.getResultSet();
					if (rs.next() == false) {
						RealmMechanics.realm_loaded_status.put(id, false);
						RealmMechanics.async_realm_status.remove(id);
						continue;
					}

					Boolean loaded = rs.getBoolean("realm_loaded");
					RealmMechanics.realm_loaded_status.put(id, loaded);
					RealmMechanics.async_realm_status.remove(id);
					continue;

				} catch (SQLException ex) {
					ex.printStackTrace();

				} finally {
					try {
						if (pst != null) {
							pst.close();
						}

					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}

				RealmMechanics.realm_loaded_status.put(id, false);
				RealmMechanics.async_realm_status.remove(id);
				continue;
			}
		}
	}
}
