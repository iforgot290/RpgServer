package me.neildennis.crypticrpg.professions.events;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.neildennis.crypticrpg.items.attribs.Tier;
import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.professions.OreCluster;

public class OreMinedEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	
	private CrypticPlayer pl;
	private OreCluster ore;
	private Location loc;
	
	public OreMinedEvent(CrypticPlayer pl, OreCluster ore, Location loc) {
		this.pl = pl;
		this.ore = ore;
		this.loc = loc;
	}
	
	public CrypticPlayer getCrypticPlayer() {
		return pl;
	}
	
	public OreCluster getOre() {
		return ore;
	}
	
	public Tier getOreTier() {
		return ore.getTier();
	}
	
	public Location getLocation() {
		return loc;
	}

	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

}
