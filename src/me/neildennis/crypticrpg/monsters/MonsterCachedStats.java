package me.neildennis.crypticrpg.monsters;

import java.util.HashMap;

import me.neildennis.crypticrpg.items.attribs.Attribute;
import me.neildennis.crypticrpg.items.attribs.AttributeType;
import me.neildennis.crypticrpg.items.type.CrypticGear;
import me.neildennis.crypticrpg.monsters.type.CrypticMonster;

public class MonsterCachedStats{

	private HashMap<AttributeType, Attribute> attribs = new HashMap<AttributeType, Attribute>();
	
	private CrypticMonster mob;

	public MonsterCachedStats(CrypticMonster mob){
		this.mob = mob;
		update();
	}
	
	public void update(){
		attribs.clear();
		
		if (mob.getHelmet() != null)
			item(mob.getHelmet());
		if (mob.getChestplate() != null)
			item(mob.getChestplate());
		if (mob.getLeggings() != null)
			item(mob.getLeggings());
		if (mob.getBoots() != null)
			item(mob.getBoots());
		if (mob.getWeapon() != null)
			item(mob.getWeapon());
	}

	private void item(CrypticGear item){
		for (Attribute attr : item.getAttribs()){
			Attribute tochange = attribs.get(attr.getType());

			if (tochange == null) {
				tochange = new Attribute(attr.getType(), attr.getLow(), attr.getHigh());
			}

			else {
				tochange.setHigh(tochange.getHigh() + attr.getHigh());
				tochange.setLow(tochange.getLow() + attr.getLow());
			}

			attribs.put(tochange.getType(), tochange);
		}
	}

	public Attribute getAttribute(AttributeType type){
		return attribs.get(type);
	}

}
