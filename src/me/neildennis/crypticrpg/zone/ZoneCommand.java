package me.neildennis.crypticrpg.zone;

import me.neildennis.crypticrpg.CrypticCommand;
import me.neildennis.crypticrpg.player.CrypticPlayer;

public class ZoneCommand extends CrypticCommand{

	@Override
	protected boolean sendUsage() {
		return true;
	}

	@Override
	public boolean command(CrypticPlayer pl) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean console() {
		// TODO Auto-generated method stub
		return false;
	}

}
