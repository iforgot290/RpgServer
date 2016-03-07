package minecade.dungeonrealms.ChatMechanics;

import java.util.UUID;

public class LoginProcessThread extends Thread {
	public void run() {
		while (true) {
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
			}
			if (ChatMechanics.async_mute_update.size() <= 0) {
				continue;
			}
			for (UUID p_uuid : ChatMechanics.async_mute_update) {
				ChatMechanics.getMuteStateSQL(p_uuid);
			}
			ChatMechanics.async_mute_update.clear();
		}
	}
}
