package me.neildennis.crypticrpg.player;

import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import me.neildennis.crypticrpg.Cryptic;

public class CrypticPlayerContainer implements MetadataValue {

	private CrypticPlayer player;
	
	public CrypticPlayerContainer(CrypticPlayer player) {
		this.player = player;
	}
	
	@Override
	public boolean asBoolean() {
		return false;
	}

	@Override
	public byte asByte() {
		return 0;
	}

	@Override
	public double asDouble() {
		return 0;
	}

	@Override
	public float asFloat() {
		return 0;
	}

	@Override
	public int asInt() {
		return 0;
	}

	@Override
	public long asLong() {
		return 0;
	}

	@Override
	public short asShort() {
		return 0;
	}

	@Override
	public String asString() {
		return null;
	}

	@Override
	public Plugin getOwningPlugin() {
		return Cryptic.getPlugin();
	}

	@Override
	public void invalidate() {
		
	}

	@Override
	public Object value() {
		return player;
	}

}
