package me.neildennis.crypticrpg.items.metadata;

import org.bukkit.Material;

public enum ItemType {
	
	SWORD, STAFF, AXE, HELMET, CHESTPLATE, LEGGINGS, BOOTS,
	TELEPORT_BOOK;
	
	public static Material getMaterialFromType(ItemType type){
		return getMaterialFromType(type, 0);
	}
	
	public static Material getMaterialFromType(ItemType type, int tier){
		switch (type){
		
		case SWORD:
			switch (tier){
			case 1: return Material.WOOD_SWORD;
			case 2: return Material.STONE_SWORD;
			case 3: return Material.IRON_SWORD;
			case 4: return Material.DIAMOND_SWORD;
			case 5: return Material.GOLD_SWORD;
			}
		case STAFF:
			switch (tier){
			case 1: return Material.WOOD_HOE;
			case 2: return Material.STONE_HOE;
			case 3: return Material.IRON_HOE;
			case 4: return Material.DIAMOND_HOE;
			case 5: return Material.GOLD_HOE;
			}
		case AXE:
			switch (tier){
			case 1: return Material.WOOD_AXE;
			case 2: return Material.STONE_AXE;
			case 3: return Material.IRON_AXE;
			case 4: return Material.DIAMOND_AXE;
			case 5: return Material.GOLD_AXE;
			}
		case HELMET:
			switch (tier){
			case 1: return Material.LEATHER_HELMET;
			case 2: return Material.CHAINMAIL_HELMET;
			case 3: return Material.IRON_HELMET;
			case 4: return Material.DIAMOND_HELMET;
			case 5: return Material.GOLD_HELMET;
			}
		case CHESTPLATE:
			switch (tier){
			case 1: return Material.LEATHER_CHESTPLATE;
			case 2: return Material.CHAINMAIL_CHESTPLATE;
			case 3: return Material.IRON_CHESTPLATE;
			case 4: return Material.DIAMOND_CHESTPLATE;
			case 5: return Material.GOLD_CHESTPLATE;
			}
		case LEGGINGS:
			switch(tier){
			case 1: return Material.LEATHER_LEGGINGS;
			case 2: return Material.CHAINMAIL_LEGGINGS;
			case 3: return Material.IRON_LEGGINGS;
			case 4: return Material.DIAMOND_LEGGINGS;
			case 5: return Material.GOLD_LEGGINGS;
			}
		case BOOTS:
			switch (tier){
			case 1: return Material.LEATHER_BOOTS;
			case 2: return Material.CHAINMAIL_BOOTS;
			case 3: return Material.IRON_BOOTS;
			case 4: return Material.DIAMOND_BOOTS;
			case 5: return Material.GOLD_BOOTS;
			}
			
		case TELEPORT_BOOK: return Material.BOOK;
		
		default: return null;
			
			
		}
	}

}
