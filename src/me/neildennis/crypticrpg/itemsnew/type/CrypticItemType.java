package me.neildennis.crypticrpg.itemsnew.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;

public enum CrypticItemType {
	
	ASDF(CrypticItem.class, Material.AIR, Material.ACACIA_DOOR);
	
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
