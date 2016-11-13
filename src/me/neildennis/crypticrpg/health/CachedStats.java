package me.neildennis.crypticrpg.health;

import java.util.HashMap;

import org.bukkit.inventory.PlayerInventory;

import me.neildennis.crypticrpg.items.ItemManager;
import me.neildennis.crypticrpg.items.attribs.Attribute;
import me.neildennis.crypticrpg.items.attribs.AttributeType;
import me.neildennis.crypticrpg.items.type.CrypticGear;
import me.neildennis.crypticrpg.items.type.CrypticItem;
import me.neildennis.crypticrpg.items.type.armor.CrypticArmor;
import me.neildennis.crypticrpg.player.CrypticPlayer;

public class CachedStats{

	private boolean weaponUpdate;
	private int lastSlot;

	private HashMap<AttributeType, Attribute> attribs;
	private HashMap<AttributeType, Attribute> weapon;
	private CrypticPlayer pl;
	private PlayerInventory inv;

	public CachedStats(CrypticPlayer pl){
		this.pl = pl;
		this.attribs = new HashMap<AttributeType, Attribute>();
	}

	public void online(){
		this.inv = pl.getPlayer().getInventory();

		for (int i = 5; i <= 8; i++){
			CrypticItem item = ItemManager.getItemFromStack(inv.getItem(i));

			if (item == null || !(item instanceof CrypticArmor)) continue;

			CrypticArmor armor = (CrypticArmor) item;
			addArmor(armor);
		}

		updateWeapon();
	}

	private void item(CrypticGear item, boolean add, HashMap<AttributeType, Attribute> map){
		for (Attribute attr : item.getAttribs()){
			Attribute tochange = map.get(attr.getType());

			if (tochange == null) {
				if (!add) continue;
				tochange = new Attribute(attr.getType(), attr.getLow(), attr.getHigh());
			}

			else {
				tochange.setHigh(tochange.getHigh() + (add ? attr.getHigh() : -attr.getHigh()));
				tochange.setLow(tochange.getLow() + (add ? attr.getLow() : -attr.getLow()));
			}

			map.put(tochange.getType(), tochange);
		}
	}

	private void updateWeapon(){
		weapon.clear();
		lastSlot = inv.getHeldItemSlot();
		CrypticItem item = ItemManager.getItemFromStack(inv.getItem(lastSlot));

		if (item == null) return;

		CrypticGear gear = (CrypticGear) item;
		item(gear, true, weapon);
		weaponUpdate = false;
	}

	public void setNeedsUpdate(){
		this.weaponUpdate = true;
	}

	public void removeArmor(CrypticArmor armor){
		item(armor, false, attribs);
	}

	public void addArmor(CrypticArmor armor){
		item(armor, true, attribs);
	}

	public Attribute getAttribute(AttributeType type){
		return getAttribute(type, inv.getHeldItemSlot());
	}

	public Attribute getAttribute(AttributeType type, int slot){
		if (ItemManager.isWeaponMod(type))
			if (slot != lastSlot || weaponUpdate) updateWeapon();

		int low = 0;
		int high = 0;
		Attribute normal = attribs.get(type);
		Attribute weapon = attribs.get(type);

		if (normal != null){
			low += normal.getLow();
			high += normal.getHigh();
		}

		if (weapon != null){
			low += weapon.getLow();
			high += weapon.getHigh();
		}

		return new Attribute(type, low, high);
	}

}
