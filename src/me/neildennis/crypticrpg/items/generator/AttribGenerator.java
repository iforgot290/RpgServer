package me.neildennis.crypticrpg.items.generator;

import java.util.HashMap;

import me.neildennis.crypticrpg.items.attribs.AttributeType;
import me.neildennis.crypticrpg.items.attribs.Tier;

public class AttribGenerator {
	
	private static HashMap<AttributeType, HashMap<Tier, AttribTemplate>> templates;
	
	public static void load(){
		templates = new HashMap<AttributeType, HashMap<Tier, AttribTemplate>>();
	}
	
	public static void generate(ItemGenerator gen){
		
	}
	
	private static class AttribTemplate {
		
		private int chance, min, max, range;
		
		public AttribTemplate(int chance, int min, int max, int range){
			this.chance = chance;
			this.min = min;
			this.max = max;
			this.range = range;
		}
		
	}

}
