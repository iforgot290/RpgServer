package me.neildennis.crypticrpg.items.custom;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.JsonObject;

import me.neildennis.crypticrpg.items.metadata.ItemType;

public class TeleportBook extends CrypticItem implements Interactable{

	private String name;
	private List<String> lore;
	private boolean chaotic;
	private Location loc;

	/*public TeleportBook(File file){

		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "";

			while ((line = reader.readLine()) != null){
				if (line.startsWith("name=")){
					name = line.replaceAll("name=", "");
				}

				else if (line.startsWith("lore=")){
					lore.add(line.replaceAll("lore=", ""));
				}

				else if (line.startsWith("chaotic=")){
					chaotic = Boolean.valueOf(line.replaceAll("chaotic=", ""));
				}

				else if (line.startsWith("location=")){
					String[] args = line.replaceAll("location=", "").split(":");
					if (args.length > 3)
						loc = new Location(Cryptic.getMainWorld(), Double.valueOf(args[0]), Double.valueOf(args[1]), Double.valueOf(args[2]), Float.valueOf(args[3]), Float.valueOf(args[4]));
					else
						loc = new Location(Cryptic.getMainWorld(), Double.valueOf(args[0]), Double.valueOf(args[1]), Double.valueOf(args[2]));
				}
			}
			
			reader.close();

		} catch (Exception e){
			e.printStackTrace();
			return;
		}
		
		generateItem();
	}*/

	public TeleportBook(String name, List<String> lore, boolean chaotic, Location loc){
		super(ItemType.TELEPORT_BOOK);
		
		this.name = name;
		this.lore = lore;
		this.chaotic = chaotic;
		this.loc = loc;
		
		generateItem();
	}
	
	private void generateItem(){
		item = new ItemStack(Material.BOOK);

		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.WHITE + ChatColor.BOLD.toString() + name);
		List<String> displaylore = new ArrayList<String>();

		for (String str : lore){
			displaylore.add(ChatColor.WHITE + str);
		}

		if (chaotic){
			displaylore.add(ChatColor.RED + ChatColor.BOLD.toString() + "CHAOTIC");
		}

		meta.setLore(displaylore);
		item.setItemMeta(meta);
	}

	public String getName(){
		return name;
	}

	public List<String> getLore(){
		return lore;
	}

	public boolean isChaotic(){
		return chaotic;
	}

	public Location getLocation(){
		return loc;
	}

	@Override
	public void loadFromJson(JsonObject obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JsonObject saveToJson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onInteract(PlayerInteractEvent event) {
		// TODO Auto-generated method stub
		
	}

}
