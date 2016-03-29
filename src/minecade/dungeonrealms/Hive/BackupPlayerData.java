package minecade.dungeonrealms.Hive;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.fusesource.jansi.Ansi;

import minecade.dungeonrealms.MoneyMechanics.MoneyMechanics;
import minecade.dungeonrealms.ShopMechanics.ShopMechanics;

public class BackupPlayerData extends Thread {
	List<String> player_names;

	public static void backupPlayers(Player[] data) {
		for (Player p : data) {
			UUID id = p.getUniqueId();

			if (Hive.being_uploaded.contains(id) || Hive.pending_upload.contains(id)
					|| Hive.server_swap.containsKey(id)) {
				continue;
			}

			try {
				Hive.uploadPlayerDatabaseData(id);
			} catch (SQLException err) {
				err.printStackTrace();
			}
			; // Location, Inventory
			MoneyMechanics.uploadBankDatabaseData(id, false);
			ShopMechanics.uploadShopDatabaseData(id, false);
		}
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(900 * 1000);
			} catch (InterruptedException e) {
			}
			// 15 minute delay between syncs.

			if (!(Hive.restart_inc) && !(Hive.shutting_down) && !(ShopMechanics.shop_shutdown)
					&& !(Hive.server_frozen)) {
				// backupPlayers(Main.plugin.getServer().getOnlinePlayers().toArray(new
				// Player[Main.plugin.getServer().getOnlinePlayers().size()]));
				System.out.println("");
				System.out.println(Ansi.ansi().fg(Ansi.Color.MAGENTA).boldOff().toString()
						+ "[Hive] Backup Query Complete. Sleeping 15m..."
						+ Ansi.ansi().fg(Ansi.Color.WHITE).boldOff().toString());
				System.out.println("");
			}
		}
	}
}
