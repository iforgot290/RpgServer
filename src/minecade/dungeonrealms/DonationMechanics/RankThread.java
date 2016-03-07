package minecade.dungeonrealms.DonationMechanics;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;

import minecade.dungeonrealms.database.ConnectionPool;

public class RankThread extends Thread {

	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (Exception err) {
			}
			for (Entry<UUID, String> data : DonationMechanics.async_set_rank.entrySet()) {
				UUID id = data.getKey();
				String rank = data.getValue();

				Connection con = null;
				PreparedStatement pst = null;

				try {
					con = ConnectionPool.getConnection();
					pst = con.prepareStatement("INSERT INTO player_database (p_name, rank)" + " VALUES" + "('"
							+ id.toString() + "', '" + rank + "') ON DUPLICATE KEY UPDATE rank='" + rank + "'");

					pst.executeUpdate();
					DonationMechanics.log
							.info("[DonationMechanics] Set rank of player " + id.toString() + " to " + rank);

				} catch (SQLException ex) {
					DonationMechanics.log.log(Level.SEVERE, ex.getMessage(), ex);

				} finally {
					try {
						if (pst != null) {
							pst.close();
						}
						if (con != null) {
							con.close();
						}

					} catch (SQLException ex) {
						DonationMechanics.log.log(Level.WARNING, ex.getMessage(), ex);
					}
				}

				DonationMechanics.async_set_rank.remove(id);
			}
		}
	}
}
