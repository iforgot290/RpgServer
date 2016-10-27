package me.neildennis.crypticrpg.items.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

import me.neildennis.crypticrpg.items.ItemManager;
import me.neildennis.crypticrpg.items.attribs.AttributeType;
import me.neildennis.crypticrpg.items.generator.modifiers.ItemModifier;
import me.neildennis.crypticrpg.items.type.CrypticItem;

public class AttribGenerator {
	
	public static void generate(ItemGenerator gen){
		ArrayList<ItemModifier> req = new ArrayList<ItemModifier>();
		ArrayList<ItemModifier> possible = new ArrayList<ItemModifier>();
		Class<? extends CrypticItem> item = gen.getType().getHandleClass();
		
		for (Map.Entry<AttributeType, ItemModifier> type : ItemManager.getMods().entrySet()){
			ItemModifier mod = type.getValue();
			
			if (mod.isPossible(item) && !mod.isExcluded(item)){
				if (mod.getChance() == 1.0F) req.add(mod);
				else possible.add(mod);
			}
		}
		
		Random r = new Random();
		ArrayList<ItemModifier> applicants = new ArrayList<ItemModifier>();
		
		for (ItemModifier mod : possible){
			float chance = r.nextFloat();
			
			if (chance < mod.getChance()){
				applicants.add(mod);
			}
		}
		
		Collections.sort(applicants);
		
		for (int i = 3; i < applicants.size(); i++){
			applicants.remove(i);
		}
		
		applicants.addAll(req);
		
		Collections.sort(applicants);
		
		for (ItemModifier mod : applicants){
			gen.setAttribute(mod.getType(), mod.getValues(gen.getTier(), gen.getRarity()));
		}
		
		
	}

}
