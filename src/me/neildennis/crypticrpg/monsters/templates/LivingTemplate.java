package me.neildennis.crypticrpg.monsters.templates;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import me.neildennis.crypticrpg.items.attribs.Attribute;
import me.neildennis.crypticrpg.items.type.CrypticItemType;
import me.neildennis.crypticrpg.items.type.armor.CrypticArmor;
import me.neildennis.crypticrpg.monsters.MobType;
import me.neildennis.crypticrpg.utils.Utils;
import net.md_5.bungee.api.ChatColor;

public abstract class LivingTemplate extends SpawnTemplate {

	protected LivingEntity le;
	
	protected LivingTemplate(){
		super();
	}
	
	public LivingTemplate(MobType type, String name, boolean elite, int respawn){
		super(type, name, elite, respawn);
	}

	@Override
	public boolean isAlive() {
		if (le == null) return false;
		return !le.isDead();
	}
	
	@Override
	public void applyGear(){
		le.setMaxHealth(10);
		for (CrypticArmor gear : this.gear){
			if (gear.getType() == CrypticItemType.HELMET) le.getEquipment().setHelmet(gear.generateItemStack());
			else if (gear.getType() == CrypticItemType.CHESTPLATE) le.getEquipment().setChestplate(gear.generateItemStack());
			else if (gear.getType() == CrypticItemType.LEGGINGS) le.getEquipment().setLeggings(gear.generateItemStack());
			else if (gear.getType() == CrypticItemType.BOOTS) le.getEquipment().setBoots(gear.generateItemStack());
			
			le.setMaxHealth(le.getMaxHealth() + gear.getAttribute(Attribute.HEALTH));
		}
		le.setHealth(le.getMaxHealth());

		if (weapon != null)
			le.getEquipment().setItemInMainHand(weapon.generateItemStack());
		updateBar();
	}
	
	@Override
	public void updateBar(){
		String str = ChatColor.GREEN + "[" + level + "] [" + (int) Math.ceil(le.getHealth()) + "/" + (int) le.getMaxHealth() + "] " + Utils.getTierColor(tier);
		if (this.name == null){
			str += getTierPrefix(tier) + type.getDefaultName();
		} else {
			str += name;
		}
		le.setCustomName(str);
		le.setCustomNameVisible(true);
	}
	
	@Override
	public void spawnMob(Location loc, int level){
		super.spawnMob(loc, level);
		le = (LivingEntity)ent;
	}
	
	public LivingEntity getLivingEntity(){
		return le;
	}

}
