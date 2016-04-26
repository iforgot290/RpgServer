package me.neildennis.crypticrpg.items.custom;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import me.neildennis.crypticrpg.items.ItemManager;
import me.neildennis.crypticrpg.items.generator.ItemGenerator;
import me.neildennis.crypticrpg.items.metadata.ItemModifier;
import me.neildennis.crypticrpg.items.metadata.ItemModifier.ModifierType;
import me.neildennis.crypticrpg.items.metadata.ItemType;
import me.neildennis.crypticrpg.items.metadata.Rarity;

public abstract class CrypticGear extends Attributeable {

	protected String name;
	protected List<String> lore;
	protected int level;
	protected Rarity rarity;

	protected CrypticGear(){
		super();
	}

	protected CrypticGear(ItemType type, String name, List<String> lore, List<ItemModifier> attribs, int tier, Rarity rarity, int level){
		super(type, tier, 1, attribs);

		this.name = name;
		this.lore = lore;
		this.level = level;
		this.rarity = rarity;

		initItem();
	}
	
	private void initItem(){
		List<String> displayMeta = new ArrayList<String>();

		for (String str : lore){
			displayMeta.add(ChatColor.translateAlternateColorCodes('&', str));
		}

		for (String str : this.getAttributeDisplay())
			displayMeta.add(str);

		displayMeta.add(rarity.getColor() + rarity.getDisplay());

		String displayName = ItemManager.getTierColor(tier) + name;

		if (level > 0){
			displayName = ChatColor.RED + "[+" + level + "] " + displayName;
		}

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setLore(displayMeta);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

		item.setItemMeta(meta);
	}

	@Override
	public void loadFromJson(JsonObject obj) {
		type = ItemType.valueOf(obj.get("type").getAsString());
		item = new ItemStack(type.getMaterial());
		tier = obj.get("tier").getAsInt();
		name = obj.get("name").getAsString();
		rarity = Rarity.valueOf(obj.get("rarity").getAsString());
		level = obj.get("level").getAsInt();
		
		lore = new ArrayList<String>();
		if (obj.get("lore") != null && obj.get("lore").isJsonArray()){
			JsonArray jsonlore = obj.get("lore").getAsJsonArray();
			for (JsonElement ele : jsonlore){
				lore.add(ele.getAsString());
			}
		}
		
		attribs = new ArrayList<ItemModifier>();
		if (obj.get("attribs") != null && obj.get("attribs").isJsonArray()){
			JsonArray jsonattr = obj.get("attribs").getAsJsonArray();
			for (JsonElement ele : jsonattr){
				JsonObject attrobj = (JsonObject)ele;
				int max = attrobj.get("max").getAsInt();
				int min = attrobj.get("min").getAsInt();
				ModifierType type = ModifierType.valueOf(attrobj.get("type").getAsString());
				ItemModifier attr = new ItemModifier(type, max, min);
				attribs.add(attr);
			}
		}
		
		initItem();
	}

	@Override
	public JsonObject saveToJson() {
		JsonObject obj = new JsonObject();
		obj.addProperty("tier", tier);
		obj.addProperty("name", name);
		obj.addProperty("rarity", rarity.name());
		obj.addProperty("level", level);

		if (lore.size() > 0){
			JsonArray jsonlore = new JsonArray();
			for (String str : lore){
				jsonlore.add(new JsonPrimitive(str));
			}
			obj.add("lore", jsonlore);
		}
		
		if (attribs.size() > 0){
			JsonArray jsonattr = new JsonArray();
			for (ItemModifier attr : attribs){
				JsonObject attrobj = new JsonObject();
				attrobj.addProperty("type", attr.getType().name());
				attrobj.addProperty("max", attr.getMax());
				attrobj.addProperty("min", attr.getMin());
				jsonattr.add(attrobj);
			}
			obj.add("attribs", jsonattr);
		}

		return obj;
	}
	
	public CrypticGear generate(ItemGenerator gen){
		this.type = gen.getType();
		this.lore = gen.getLore();
		this.name = gen.getName();
		this.rarity = gen.getRarity();
		this.level = gen.getLevel();
		this.attribs = gen.getModifiers();
		
		if (level < 20) tier = 1;
		else if (level < 40) tier = 2;
		else if (level < 60) tier = 3;
		else if (level < 80) tier = 4;
		else tier = 5;
		
		initItem();
		return this;
	}

}
