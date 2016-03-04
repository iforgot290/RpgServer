package me.vilsol.itemgenerator.modifiers;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.ItemMeta;

import me.vilsol.itemgenerator.engine.ItemModifier;
import me.vilsol.itemgenerator.engine.ModifierCondition;
import me.vilsol.itemgenerator.engine.ModifierRange;
import me.vilsol.itemgenerator.engine.ModifierType;
import minecade.dungeonrealms.enums.ItemRarity;
import minecade.dungeonrealms.enums.ItemTier;
import minecade.dungeonrealms.enums.ItemType;

public class ArmorModifiers {
	
	private static List<ItemType> armor = Arrays.asList(ItemType.BOOTS, ItemType.CHESTPLATE, ItemType.LEGGINGS, ItemType.HELMET);
	private static ChatColor r = ChatColor.RED;
	private static List<String> elements = Arrays.asList("FIRE", "ICE", "POISON");

	public class HPRegen extends ItemModifier {

		public HPRegen() {
			super(armor, 100, r + "HP REGEN: +", " HP/s", false);
			addCondition(new ModifierCondition(ItemTier.T1, null, new ModifierRange(ModifierType.STATIC, 5, 15)).setCantContain(ArmorModifiers.EnergyRegen.class));
			addCondition(new ModifierCondition(ItemTier.T2, null, new ModifierRange(ModifierType.STATIC, 17, 40)).setCantContain(ArmorModifiers.EnergyRegen.class));
			addCondition(new ModifierCondition(ItemTier.T3, null, new ModifierRange(ModifierType.STATIC, 50, 70)).setCantContain(ArmorModifiers.EnergyRegen.class));
			addCondition(new ModifierCondition(ItemTier.T4, null, new ModifierRange(ModifierType.STATIC, 70, 90)).setCantContain(ArmorModifiers.EnergyRegen.class));
			addCondition(new ModifierCondition(ItemTier.T5, null, new ModifierRange(ModifierType.STATIC, 80, 140)).setCantContain(ArmorModifiers.EnergyRegen.class));
			
			setOrderPriority(5);
		}
		
	}
	
	public class MainArmor extends ItemModifier {

		public MainArmor() {
			super(Arrays.asList(ItemType.CHESTPLATE, ItemType.LEGGINGS), 100, r + "ARMOR: ", "%", false); // 50% chance for DPS, 50% for armor
			
			addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.COMMON, new ModifierRange(ModifierType.TRIPLE, 1, 1, 1)).setCantContain(MainDPS.class));
			addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, 1, 2, 3)).setCantContain(MainDPS.class));
			addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.RARE, new ModifierRange(ModifierType.TRIPLE, 1, 2, 3)).setCantContain(MainDPS.class));
			addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.UNIQUE, new ModifierRange(ModifierType.TRIPLE, 1, 2, 3)).setCantContain(MainDPS.class));

			addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.COMMON, new ModifierRange(ModifierType.TRIPLE, 1, 2, 3)).setCantContain(MainDPS.class));
			addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, 3, 4, 5)).setCantContain(MainDPS.class));
			addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.RARE, new ModifierRange(ModifierType.TRIPLE, 5, 6, 7)).setCantContain(MainDPS.class));
			addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.UNIQUE, new ModifierRange(ModifierType.TRIPLE, 5, 6, 7)).setCantContain(MainDPS.class));

			addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.COMMON, new ModifierRange(ModifierType.TRIPLE, 5, 6, 7)).setCantContain(MainDPS.class));
			addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, 6, 9, 10)).setCantContain(MainDPS.class));
			addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.RARE, new ModifierRange(ModifierType.TRIPLE, 8, 9, 10)).setCantContain(MainDPS.class));
			addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.UNIQUE, new ModifierRange(ModifierType.TRIPLE, 8, 9, 11)).setCantContain(MainDPS.class));

			addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.COMMON, new ModifierRange(ModifierType.TRIPLE, 8, 9, 10)).setCantContain(MainDPS.class));
			addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, 10, 11, 12)).setCantContain(MainDPS.class));
			addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.RARE, new ModifierRange(ModifierType.TRIPLE, 11, 12, 13)).setCantContain(MainDPS.class));
			addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.UNIQUE, new ModifierRange(ModifierType.TRIPLE, 12, 13, 14)).setCantContain(MainDPS.class));

			addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.COMMON, new ModifierRange(ModifierType.TRIPLE, 11, 12, 13)).setCantContain(MainDPS.class));
			addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, 13, 14, 15)).setCantContain(MainDPS.class));
			addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.RARE, new ModifierRange(ModifierType.TRIPLE, 16, 17, 18)).setCantContain(MainDPS.class));
			addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.UNIQUE, new ModifierRange(ModifierType.TRIPLE, 17, 18, 19)).setCantContain(MainDPS.class));
			
			setOrderPriority(2);
		}
		
	}
	
	public class OtherArmor extends ItemModifier {

		public OtherArmor() {
			super(Arrays.asList(ItemType.HELMET, ItemType.BOOTS), 100, r + "ARMOR: ", "%", false); // 50% chance for DPS, 50% for armor
			
			addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.COMMON, new ModifierRange(ModifierType.TRIPLE, 1, 1, 1)).setCantContain(OtherDPS.class));
			addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, 1, 1, 2)).setCantContain(OtherDPS.class));
			addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.RARE, new ModifierRange(ModifierType.TRIPLE, 1, 1, 2)).setCantContain(OtherDPS.class));
			addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.UNIQUE, new ModifierRange(ModifierType.TRIPLE, 1, 1, 2)).setCantContain(OtherDPS.class));

			addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.COMMON, new ModifierRange(ModifierType.TRIPLE, 1, 1, 2)).setCantContain(OtherDPS.class));
			addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, 2, 2, 3)).setCantContain(OtherDPS.class));
			addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.RARE, new ModifierRange(ModifierType.TRIPLE, 3, 3, 4)).setCantContain(OtherDPS.class));
			addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.UNIQUE, new ModifierRange(ModifierType.TRIPLE, 3, 3, 4)).setCantContain(OtherDPS.class));

			addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.COMMON, new ModifierRange(ModifierType.TRIPLE, 3, 3, 4)).setCantContain(OtherDPS.class));
			addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, 3, 5, 5)).setCantContain(OtherDPS.class));
			addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.RARE, new ModifierRange(ModifierType.TRIPLE, 4, 5, 5)).setCantContain(OtherDPS.class));
			addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.UNIQUE, new ModifierRange(ModifierType.TRIPLE, 4, 5, 6)).setCantContain(OtherDPS.class));

			addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.COMMON, new ModifierRange(ModifierType.TRIPLE, 4, 5, 5)).setCantContain(OtherDPS.class));
			addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, 5, 6, 6)).setCantContain(OtherDPS.class));
			addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.RARE, new ModifierRange(ModifierType.TRIPLE, 6, 6, 7)).setCantContain(OtherDPS.class));
			addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.UNIQUE, new ModifierRange(ModifierType.TRIPLE, 6, 7, 7)).setCantContain(OtherDPS.class));

			addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.COMMON, new ModifierRange(ModifierType.TRIPLE, 6, 6, 7)).setCantContain(OtherDPS.class));
			addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, 7, 7, 8)).setCantContain(OtherDPS.class));
			addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.RARE, new ModifierRange(ModifierType.TRIPLE, 8, 9, 9)).setCantContain(OtherDPS.class));
			addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.UNIQUE, new ModifierRange(ModifierType.TRIPLE, 9, 9, 10)).setCantContain(OtherDPS.class));
			
			setOrderPriority(2);
		}
		
	}
	
	public class MainDPS extends ItemModifier {

        public MainDPS() {
            super(Arrays.asList(ItemType.CHESTPLATE, ItemType.LEGGINGS), 50, r + "DPS: ", "%", false); // 50% chance for DPS, 50% for armor
            
            addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.COMMON, new ModifierRange(ModifierType.TRIPLE, 1, 1, 1)));
            addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, 1, 2, 3)));
            addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.RARE, new ModifierRange(ModifierType.TRIPLE, 1, 2, 3)));
            addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.UNIQUE, new ModifierRange(ModifierType.TRIPLE, 1, 2, 3)));

            addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.COMMON, new ModifierRange(ModifierType.TRIPLE, 1, 2, 3)));
            addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, 3, 4, 5)));
            addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.RARE, new ModifierRange(ModifierType.TRIPLE, 5, 6, 7)));
            addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.UNIQUE, new ModifierRange(ModifierType.TRIPLE, 5, 6, 7)));

            addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.COMMON, new ModifierRange(ModifierType.TRIPLE, 5, 6, 7)));
            addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, 6, 9, 10)));
            addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.RARE, new ModifierRange(ModifierType.TRIPLE, 8, 9, 10)));
            addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.UNIQUE, new ModifierRange(ModifierType.TRIPLE, 8, 9, 11)));

            addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.COMMON, new ModifierRange(ModifierType.TRIPLE, 8, 9, 10)));
            addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, 10, 11, 12)));
            addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.RARE, new ModifierRange(ModifierType.TRIPLE, 11, 12, 13)));
            addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.UNIQUE, new ModifierRange(ModifierType.TRIPLE, 12, 13, 14)));

            addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.COMMON, new ModifierRange(ModifierType.TRIPLE, 11, 12, 13)));
            addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, 13, 14, 15)));
            addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.RARE, new ModifierRange(ModifierType.TRIPLE, 16, 17, 18)));
            addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.UNIQUE, new ModifierRange(ModifierType.TRIPLE, 17, 18, 19)));
            
            setOrderPriority(1);
        }
        
    }
	
	public class OtherDPS extends ItemModifier {

        public OtherDPS() {
            super(Arrays.asList(ItemType.HELMET, ItemType.BOOTS), 50, r + "DPS: ", "%", false); // 50% chance for DPS, 50% for armor
            
            addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.COMMON, new ModifierRange(ModifierType.TRIPLE, 1, 1, 1)));
            addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, 1, 1, 2)));
            addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.RARE, new ModifierRange(ModifierType.TRIPLE, 1, 1, 2)));
            addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.UNIQUE, new ModifierRange(ModifierType.TRIPLE, 1, 1, 2)));

            addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.COMMON, new ModifierRange(ModifierType.TRIPLE, 1, 1, 2)));
            addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, 2, 2, 3)));
            addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.RARE, new ModifierRange(ModifierType.TRIPLE, 3, 3, 4)));
            addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.UNIQUE, new ModifierRange(ModifierType.TRIPLE, 3, 3, 4)));

            addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.COMMON, new ModifierRange(ModifierType.TRIPLE, 3, 3, 4)));
            addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, 3, 5, 5)));
            addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.RARE, new ModifierRange(ModifierType.TRIPLE, 4, 5, 5)));
            addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.UNIQUE, new ModifierRange(ModifierType.TRIPLE, 4, 5, 6)));

            addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.COMMON, new ModifierRange(ModifierType.TRIPLE, 4, 5, 5)));
            addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, 5, 6, 6)));
            addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.RARE, new ModifierRange(ModifierType.TRIPLE, 6, 6, 7)));
            addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.UNIQUE, new ModifierRange(ModifierType.TRIPLE, 6, 7, 7)));

            addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.COMMON, new ModifierRange(ModifierType.TRIPLE, 6, 6, 7)));
            addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.TRIPLE, 7, 7, 8)));
            addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.RARE, new ModifierRange(ModifierType.TRIPLE, 8, 9, 9)));
            addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.UNIQUE, new ModifierRange(ModifierType.TRIPLE, 9, 9, 10)));
            
            setOrderPriority(1);
        }
        
    }
	
	public class ChestplateHP extends ItemModifier {

        public ChestplateHP() {
            super(Arrays.asList(ItemType.CHESTPLATE), 100, r + "HP: +", null, false);

            addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.COMMON, new ModifierRange(ModifierType.STATIC, 10, 30 )));
            addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 31, 60)));
            addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.RARE, new ModifierRange(ModifierType.STATIC, 61, 90)));
            addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.UNIQUE, new ModifierRange(ModifierType.STATIC, 91, 120)));

            addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.COMMON, new ModifierRange(ModifierType.STATIC, 70, 110)));
            addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 111, 190)));
            addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.RARE, new ModifierRange(ModifierType.STATIC, 191, 240)));
            addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.UNIQUE, new ModifierRange(ModifierType.STATIC, 241, 310)));

            addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.COMMON, new ModifierRange(ModifierType.STATIC, 200, 450)));
            addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 451, 650)));
            addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.RARE, new ModifierRange(ModifierType.STATIC, 651, 749)));
            addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.UNIQUE, new ModifierRange(ModifierType.STATIC, 750, 850)));

            addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.COMMON, new ModifierRange(ModifierType.STATIC, 650, 960)));
            addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 961, 1450)));
            addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.RARE, new ModifierRange(ModifierType.STATIC, 1451, 2300)));
            addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.UNIQUE, new ModifierRange(ModifierType.STATIC, 2301, 2800)));

            addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.COMMON, new ModifierRange(ModifierType.STATIC, 1450, 2500)));
            addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 2501, 3800)));
            addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.RARE, new ModifierRange(ModifierType.STATIC, 3801, 5500)));
            addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.UNIQUE, new ModifierRange(ModifierType.STATIC, 5501, 6000)));
            
            setOrderPriority(3);
        }
        
    }
	
	public class LeggingsHP extends ItemModifier {

        public LeggingsHP() {
            super(Arrays.asList(ItemType.LEGGINGS), 100, r + "HP: +", null, false);

            addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.COMMON, new ModifierRange(ModifierType.STATIC, 10, 30 )));
            addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 31, 60)));
            addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.RARE, new ModifierRange(ModifierType.STATIC, 61, 90)));
            addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.UNIQUE, new ModifierRange(ModifierType.STATIC, 91, 120)));

            addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.COMMON, new ModifierRange(ModifierType.STATIC, 70, 110)));
            addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 111, 190)));
            addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.RARE, new ModifierRange(ModifierType.STATIC, 191, 240)));
            addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.UNIQUE, new ModifierRange(ModifierType.STATIC, 241, 310)));

            addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.COMMON, new ModifierRange(ModifierType.STATIC, 200, 450)));
            addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 451, 650)));
            addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.RARE, new ModifierRange(ModifierType.STATIC, 651, 749)));
            addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.UNIQUE, new ModifierRange(ModifierType.STATIC, 750, 850)));

            addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.COMMON, new ModifierRange(ModifierType.STATIC, 650, 960)));
            addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 961, 1450)));
            addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.RARE, new ModifierRange(ModifierType.STATIC, 1451, 2300)));
            addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.UNIQUE, new ModifierRange(ModifierType.STATIC, 2301, 2800)));

            addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.COMMON, new ModifierRange(ModifierType.STATIC, 1450, 2500)));
            addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 2501, 3800)));
            addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.RARE, new ModifierRange(ModifierType.STATIC, 3801, 5500)));
            addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.UNIQUE, new ModifierRange(ModifierType.STATIC, 5501, 6000)));
            
            setOrderPriority(3);
        }
        
    }
	
	// HP generation for helmets and boots
	public class OtherHP extends ItemModifier {

        public OtherHP() {
            super(Arrays.asList(ItemType.HELMET, ItemType.BOOTS), 100, r + "HP: +", null, false);

            addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.COMMON, new ModifierRange(ModifierType.STATIC, 5, 12)));
            addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 13, 27)));
            addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.RARE, new ModifierRange(ModifierType.STATIC, 28, 45)));
            addCondition(new ModifierCondition(ItemTier.T1, ItemRarity.UNIQUE, new ModifierRange(ModifierType.STATIC, 46, 65)));

            addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.COMMON, new ModifierRange(ModifierType.STATIC, 30, 67)));
            addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 68, 105)));
            addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.RARE, new ModifierRange(ModifierType.STATIC, 106, 153)));
            addCondition(new ModifierCondition(ItemTier.T2, ItemRarity.UNIQUE, new ModifierRange(ModifierType.STATIC, 154, 215)));

            addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.COMMON, new ModifierRange(ModifierType.STATIC, 100, 195)));
            addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 196, 280)));
            addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.RARE, new ModifierRange(ModifierType.STATIC, 281, 380)));
            addCondition(new ModifierCondition(ItemTier.T3, ItemRarity.UNIQUE, new ModifierRange(ModifierType.STATIC, 381, 486)));

            addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.COMMON, new ModifierRange(ModifierType.STATIC, 300, 450)));
            addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 451, 700)));
            addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.RARE, new ModifierRange(ModifierType.STATIC, 701, 999)));
            addCondition(new ModifierCondition(ItemTier.T4, ItemRarity.UNIQUE, new ModifierRange(ModifierType.STATIC, 1000, 1300)));

            addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.COMMON, new ModifierRange(ModifierType.STATIC, 700, 1200)));
            addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.UNCOMMON, new ModifierRange(ModifierType.STATIC, 1201, 2000)));
            addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.RARE, new ModifierRange(ModifierType.STATIC, 2001, 2600)));
            addCondition(new ModifierCondition(ItemTier.T5, ItemRarity.UNIQUE, new ModifierRange(ModifierType.STATIC, 2601, 3000)));
            
            setOrderPriority(3);
        }
        
    }
	
	public class StrDexVitInt extends ItemModifier {

		public StrDexVitInt() {
			super(armor, -1, null, null);
			addCondition(new ModifierCondition(ItemTier.T1, null, new ModifierRange(ModifierType.STATIC, 1, 15), 25));
			addCondition(new ModifierCondition(ItemTier.T2, null, new ModifierRange(ModifierType.STATIC, 1, 35), 20).setBonus(new ModifierCondition(ItemTier.T2, null, new ModifierRange(ModifierType.STATIC, 1, 35), 5).setReplacement(StrDexVitInt.class)));
			addCondition(new ModifierCondition(ItemTier.T3, null, new ModifierRange(ModifierType.STATIC, 1, 75), 15).setBonus(new ModifierCondition(ItemTier.T3, null, new ModifierRange(ModifierType.STATIC, 1, 75), 5).setReplacement(StrDexVitInt.class).setBonus(new ModifierCondition(ItemTier.T3, null, new ModifierRange(ModifierType.STATIC, 1, 75), 1).setReplacement(StrDexVitInt.class))));
			addCondition(new ModifierCondition(ItemTier.T4, null, new ModifierRange(ModifierType.STATIC, 1, 115), 15).setBonus(new ModifierCondition(ItemTier.T4, null, new ModifierRange(ModifierType.STATIC, 1, 115), 9).setReplacement(StrDexVitInt.class).setBonus(new ModifierCondition(ItemTier.T4, null, new ModifierRange(ModifierType.STATIC, 1, 115), 4).setReplacement(StrDexVitInt.class))));
			addCondition(new ModifierCondition(ItemTier.T5, null, new ModifierRange(ModifierType.STATIC, 1, 315), 20).setBonus(new ModifierCondition(ItemTier.T5, null, new ModifierRange(ModifierType.STATIC, 1, 315), 10).setReplacement(StrDexVitInt.class).setBonus(new ModifierCondition(ItemTier.T5, null, new ModifierRange(ModifierType.STATIC, 1, 315), 5).setReplacement(StrDexVitInt.class))));
		}
		
		@Override
		public String getPrefix(ItemMeta meta){
			List<String> allStats = Arrays.asList(r + "STR: +", r + "INT: +", r + "VIT: +", r + "DEX: +");
			return allStats.get(new Random().nextInt(allStats.size()));
		}
		
	}
	
	public class EnergyRegen extends ItemModifier {

		public EnergyRegen() {
			super(armor, 50, r + "ENERGY REGEN: +", "%", false);
			addCondition(new ModifierCondition(ItemTier.T1, null, new ModifierRange(ModifierType.STATIC, 1, 5, true)));
			addCondition(new ModifierCondition(ItemTier.T2, null, new ModifierRange(ModifierType.STATIC, 3, 7, true)));
			addCondition(new ModifierCondition(ItemTier.T3, null, new ModifierRange(ModifierType.STATIC, 5, 9, true)));
			addCondition(new ModifierCondition(ItemTier.T4, null, new ModifierRange(ModifierType.STATIC, 7, 12, true)));
			addCondition(new ModifierCondition(ItemTier.T5, null, new ModifierRange(ModifierType.STATIC, 7, 12, true)));
			
			setOrderPriority(4);
		}
		
	}
	
	public class Thorns extends ItemModifier {

		public Thorns() {
			super(armor, -1, r + "THORNS: +", "% DMG");
			addCondition(new ModifierCondition(ItemTier.T1, null, new ModifierRange(ModifierType.STATIC, 1, 2), 3));
			addCondition(new ModifierCondition(ItemTier.T2, null, new ModifierRange(ModifierType.STATIC, 1, 3), 5));
			addCondition(new ModifierCondition(ItemTier.T3, null, new ModifierRange(ModifierType.STATIC, 1, 5), 10));
			addCondition(new ModifierCondition(ItemTier.T4, null, new ModifierRange(ModifierType.STATIC, 1, 9), 13));
			addCondition(new ModifierCondition(ItemTier.T5, null, new ModifierRange(ModifierType.STATIC, 1, 9), 20));
		}
		
	}
	
	public class Dodge extends ItemModifier {

		public Dodge() {
			super(armor, -1, r + "DODGE: ", "%");
			addCondition(new ModifierCondition(ItemTier.T1, null, new ModifierRange(ModifierType.STATIC, 1, 5), 5));
			addCondition(new ModifierCondition(ItemTier.T2, null, new ModifierRange(ModifierType.STATIC, 1, 8), 9));
			addCondition(new ModifierCondition(ItemTier.T3, null, new ModifierRange(ModifierType.STATIC, 1, 10), 15));
			addCondition(new ModifierCondition(ItemTier.T4, null, new ModifierRange(ModifierType.STATIC, 1, 12), 25));
			addCondition(new ModifierCondition(ItemTier.T5, null, new ModifierRange(ModifierType.STATIC, 1, 12), 30));
		}
		
	}
	
	public class Block extends ItemModifier {

		public Block() {
			super(armor, -1, r + "BLOCK: ", "%");
			addCondition(new ModifierCondition(ItemTier.T1, null, new ModifierRange(ModifierType.STATIC, 1, 5), 5));
			addCondition(new ModifierCondition(ItemTier.T2, null, new ModifierRange(ModifierType.STATIC, 1, 8), 9));
			addCondition(new ModifierCondition(ItemTier.T3, null, new ModifierRange(ModifierType.STATIC, 1, 10), 15));
			addCondition(new ModifierCondition(ItemTier.T4, null, new ModifierRange(ModifierType.STATIC, 1, 12), 25));
			addCondition(new ModifierCondition(ItemTier.T5, null, new ModifierRange(ModifierType.STATIC, 1, 12), 30));
		}
		
	}
	
	public class Resistances extends ItemModifier {

		public Resistances() {
			super(armor, -1, null, "%");
			addCondition(new ModifierCondition(ItemTier.T1, null, new ModifierRange(ModifierType.STATIC, 1, 5), 15));
			addCondition(new ModifierCondition(ItemTier.T2, null, new ModifierRange(ModifierType.STATIC, 1, 7), 15));
			addCondition(new ModifierCondition(ItemTier.T3, null, new ModifierRange(ModifierType.STATIC, 1, 20), 25));
			addCondition(new ModifierCondition(ItemTier.T4, null, new ModifierRange(ModifierType.STATIC, 1, 32), 20));
			addCondition(new ModifierCondition(ItemTier.T5, null, new ModifierRange(ModifierType.STATIC, 1, 45), 30));
		}
		
		@Override
		public String getPrefix(ItemMeta meta){
			return r + elements.get(new Random().nextInt(elements.size())) + " RESISTANCE: ";
		}
		
	}
	
	public class Reflection extends ItemModifier {

		public Reflection() {
			super(armor, -1, r + "REFLECTION: ", "%");
			addCondition(new ModifierCondition(ItemTier.T1, null, new ModifierRange(ModifierType.STATIC, 1, 1), 3));
			addCondition(new ModifierCondition(ItemTier.T2, null, new ModifierRange(ModifierType.STATIC, 1, 2), 5));
			addCondition(new ModifierCondition(ItemTier.T3, null, new ModifierRange(ModifierType.STATIC, 1, 4), 10));
			addCondition(new ModifierCondition(ItemTier.T4, null, new ModifierRange(ModifierType.STATIC, 1, 5), 13));
			addCondition(new ModifierCondition(ItemTier.T5, null, new ModifierRange(ModifierType.STATIC, 1, 5), 15));
		}
		
	}
	
	public class GemFind extends ItemModifier {

		public GemFind() {
			super(armor, -1, r + "GEM FIND: ", "%");
			addCondition(new ModifierCondition(ItemTier.T1, null, new ModifierRange(ModifierType.STATIC, 1, 5), 5));
			addCondition(new ModifierCondition(ItemTier.T2, null, new ModifierRange(ModifierType.STATIC, 1, 8), 5));
			addCondition(new ModifierCondition(ItemTier.T3, null, new ModifierRange(ModifierType.STATIC, 1, 15), 5));
			addCondition(new ModifierCondition(ItemTier.T4, null, new ModifierRange(ModifierType.STATIC, 1, 20), 5));
			addCondition(new ModifierCondition(ItemTier.T5, null, new ModifierRange(ModifierType.STATIC, 1, 20), 5));
		}
		
	}
	
	public class ItemFind extends ItemModifier {

		public ItemFind() {
			super(armor, -1, r + "ITEM FIND: +", "%");
			addCondition(new ModifierCondition(ItemTier.T1, null, new ModifierRange(ModifierType.STATIC, 1, 1), 5));
			addCondition(new ModifierCondition(ItemTier.T2, null, new ModifierRange(ModifierType.STATIC, 1, 2), 5));
			addCondition(new ModifierCondition(ItemTier.T3, null, new ModifierRange(ModifierType.STATIC, 1, 3), 5));
			addCondition(new ModifierCondition(ItemTier.T4, null, new ModifierRange(ModifierType.STATIC, 1, 4), 5));
			addCondition(new ModifierCondition(ItemTier.T5, null, new ModifierRange(ModifierType.STATIC, 1, 4), 5));
		}
		
	}
	
}
