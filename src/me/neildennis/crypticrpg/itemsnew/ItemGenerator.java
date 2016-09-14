package me.neildennis.crypticrpg.itemsnew;

import java.lang.reflect.Constructor;

import me.neildennis.crypticrpg.itemsnew.attribs.Tier;
import me.neildennis.crypticrpg.itemsnew.type.CrypticItemType;
import me.neildennis.crypticrpg.itemsnew.type.weapon.CrypticWeapon;

public class ItemGenerator {
	
	public static CrypticWeapon generateWeapon(CrypticItemType type, Tier tier){
		for (Constructor<?> cons : type.getHandleClass().getDeclaredConstructors()){
			if (cons.getParameterTypes().length == 0){
				
			}
		}
		return null;
	}

}
