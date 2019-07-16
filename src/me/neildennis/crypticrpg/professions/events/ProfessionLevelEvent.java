package me.neildennis.crypticrpg.professions.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.neildennis.crypticrpg.player.CrypticPlayer;
import me.neildennis.crypticrpg.professions.skill.Skill.SkillType;

public class ProfessionLevelEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	
	private CrypticPlayer pl;
	private SkillType type;
	private int level;
	
	public ProfessionLevelEvent(CrypticPlayer pl, SkillType type, int level) {
		this.pl = pl;
		this.type = type;
		this.level = level;
	}
	
	public CrypticPlayer getPlayer() {
		return pl;
	}
	
	public SkillType getSkillType() {
		return type;
	}
	
	public int getNewLevel() {
		return level;
	}
	
	@Override
	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}

}
