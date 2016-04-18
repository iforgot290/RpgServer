package me.neildennis.crypticrpg.items.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.neildennis.crypticrpg.Cryptic;
import me.neildennis.crypticrpg.items.ItemInterface;
import me.neildennis.crypticrpg.items.metadata.ItemType;

public class TeleportBook implements ItemInterface{

	private String name;
	private List<String> lore;
	private boolean chaotic;
	private Location loc;

	private ItemStack item;

	public TeleportBook(File file){

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
	}

	public TeleportBook(String name, List<String> lore, boolean chaotic, Location loc){
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
		meta.getLore().clear();

		for (String str : lore){
			meta.getLore().add(ChatColor.WHITE + str);
		}

		if (chaotic){
			meta.getLore().add(ChatColor.RED + ChatColor.BOLD.toString() + "CHAOTIC");
		}

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
	public String getSaveString() {
		return null;
	}

	@Override
	public ItemType getItemType() {
		return ItemType.TELEPORT_BOOK;
	}

	@Override
	public ItemStack getBukkitItem() {
		return item.clone();
	}

}
