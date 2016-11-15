package me.neildennis.crypticrpg.monsters.type;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;

import me.neildennis.crypticrpg.monsters.SpawnType;

public abstract class LivingMonster extends CrypticMonster{

	protected LivingEntity le;

	public LivingMonster() {

	}

	@Override
	public LivingEntity spawn(Location loc, SpawnType spawnType){
		le = (LivingEntity) super.spawn(loc, spawnType);

		le.setCanPickupItems(false);
		setMaxHealth(maxhealth);
		setHealth(health);

		EntityEquipment equip = le.getEquipment();

		if (helm != null)
			equip.setHelmet(helm.generateItemStack());
		if (chest != null)
			equip.setChestplate(chest.generateItemStack());
		if (legs != null)
			equip.setLeggings(legs.generateItemStack());
		if (boots != null)
			equip.setBoots(boots.generateItemStack());
		if (weapon != null)
			equip.setItemInMainHand(weapon.generateItemStack());

		return le;
	}

	@Override
	public LivingEntity getEntity(){
		return le;
	}

	@Override
	public double getHealth(){
		return le == null ? health : le.getHealth();
	}

	@Override
	public void setHealth(double health){
		le.setHealth(health);
	}

	@Override
	public double getMaxHealth(){
		return le == null ? maxhealth : le.getMaxHealth();
	}

	@Override
	public void setMaxHealth(double maxhealth){
		le.setMaxHealth(maxhealth);
	}

}
