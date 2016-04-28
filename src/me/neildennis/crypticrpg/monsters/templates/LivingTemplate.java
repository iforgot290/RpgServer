package me.neildennis.crypticrpg.monsters.templates;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import me.neildennis.crypticrpg.items.custom.CrypticGear;
import me.neildennis.crypticrpg.items.metadata.ItemModifier;
import me.neildennis.crypticrpg.items.metadata.ItemModifier.ModifierType;
import me.neildennis.crypticrpg.items.metadata.ItemType;
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
		for (CrypticGear gear : this.gear){
			if (gear.getType() == ItemType.HELMET) le.getEquipment().setHelmet(gear.getItemStack());
			else if (gear.getType() == ItemType.CHESTPLATE) le.getEquipment().setChestplate(gear.getItemStack());
			else if (gear.getType() == ItemType.LEGGINGS) le.getEquipment().setLeggings(gear.getItemStack());
			else if (gear.getType() == ItemType.BOOTS) le.getEquipment().setBoots(gear.getItemStack());
			
			ItemModifier mod = gear.getAttribute(ModifierType.HEALTH);
			if (mod != null){
				le.setMaxHealth(le.getHealth() + mod.getValue());
			}
		}
		le.setHealth(le.getMaxHealth());

		if (weapon != null)
			le.getEquipment().setItemInMainHand(weapon.getItemStack());
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
