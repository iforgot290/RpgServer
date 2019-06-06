package me.neildennis.crypticrpg.items.type;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

import me.neildennis.crypticrpg.items.type.armor.CrypticBoots;
import me.neildennis.crypticrpg.items.type.armor.CrypticChestplate;
import me.neildennis.crypticrpg.items.type.armor.CrypticHelmet;
import me.neildennis.crypticrpg.items.type.armor.CrypticLeggings;
import me.neildennis.crypticrpg.items.type.weapon.CrypticSword;

public enum CrypticItemType {
	
	SWORD(CrypticSword.class, 	Material.WOODEN_SWORD,
								Material.STONE_SWORD,
								Material.IRON_SWORD,
								Material.DIAMOND_SWORD,
								Material.GOLDEN_SWORD),
	
	HELMET(CrypticHelmet.class, Material.LEATHER_HELMET,
								Material.CHAINMAIL_HELMET,
								Material.IRON_HELMET,
								Material.DIAMOND_HELMET,
								Material.GOLDEN_HELMET),
	
	CHESTPLATE(CrypticChestplate.class, Material.LEATHER_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.IRON_CHESTPLATE, Material.DIAMOND_CHESTPLATE,
			Material.GOLDEN_CHESTPLATE),
	LEGGINGS(CrypticLeggings.class, Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.IRON_LEGGINGS, Material.DIAMOND_LEGGINGS,
			Material.GOLDEN_LEGGINGS),
	BOOTS(CrypticBoots.class, Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.DIAMOND_BOOTS, Material.GOLDEN_BOOTS);
	
	private Class<? extends CrypticItem> c;
	private Material[] mat;
	
	CrypticItemType(Class<? extends CrypticItem> c, Material... mat){
		this.c = c;
		this.mat = mat;
	}
	
	public Class<? extends CrypticItem> getHandleClass(){
		return c;
	}
	
	public Material[] getMaterials(){
		return mat;
	}
	
	public static CrypticItemType fromMaterial(Material mat){
		for (CrypticItemType type : CrypticItemType.values()){
			List<Material> mats = Arrays.asList(type.getMaterials());
			if (mats.contains(mat)) return type;
		}
		return null;
	}

}
