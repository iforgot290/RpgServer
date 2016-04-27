package me.neildennis.crypticrpg.items;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.neildennis.crypticrpg.items.custom.CrypticItem;
import me.neildennis.crypticrpg.items.metadata.ItemType;
import me.neildennis.crypticrpg.player.CrypticPlayer;

public class ItemData {

	private CrypticPlayer pl;
	private ArrayList<ItemTracker> items;

	private HashMap<Integer, CrypticItem> loaded;
	private HashMap<Integer, ItemStack> loadedVanilla;

	public ItemData(CrypticPlayer pl, ResultSet data){
		this.pl = pl;
		this.items = new ArrayList<ItemTracker>();
		this.loaded = new HashMap<Integer, CrypticItem>();
		this.loadedVanilla = new HashMap<Integer, ItemStack>();

		try {
			String invstring = data.getString("inventory");
			if (invstring == null || invstring.equals("")) return;
			JsonArray results = (JsonArray) new JsonParser().parse(invstring);

			for (JsonElement ele : results){
				JsonObject obj = (JsonObject) ele;
				String strType = obj.get("type").getAsString();

				if (strType.equalsIgnoreCase("vanilla")){
					Material mat = Material.valueOf(obj.get("material").getAsString());
					int amount = obj.get("amount").getAsInt();
					short durability = obj.get("durability").getAsShort();
					loadedVanilla.put(obj.get("slot").getAsInt(), new ItemStack(mat, amount, durability));
				} else {
					ItemType type = ItemType.valueOf(strType);
					CrypticItem item = type.getHandleClass().newInstance();
					item.loadFromJson(obj);
					loaded.put(obj.get("slot").getAsInt(), item);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void online(Player pl){
		Inventory inv = pl.getInventory();
		inv.clear();

		for (Entry<Integer, CrypticItem> entry : loaded.entrySet()){
			inv.setItem(entry.getKey(), entry.getValue().getItemStack());
			trackItem(entry.getValue());
		}
		
		for (Entry<Integer, ItemStack> entry : loadedVanilla.entrySet()){
			inv.setItem(entry.getKey(), entry.getValue());
		}
	}

	public CrypticItem getCrypticItem(ItemStack stack){
		for (ItemTracker track : items)
			if (track.getItemStack().equals(stack))
				return track.getCrypticItem();
		return null;
	}

	public void trackItem(CrypticItem item){
		items.add(new ItemTracker(item.getItemStack(), item));
	}

	public void untrackItem(CrypticItem item){
		items.remove(item.getItemStack());
	}

	public void untrackItem(ItemStack stack){
		items.remove(stack);
	}

}
