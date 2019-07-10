package me.neildennis.crypticrpg.menu;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.neildennis.crypticrpg.menu.options.MenuOption;
import me.neildennis.crypticrpg.utils.Log;

public class ItemMenu {

	private String name = "Default Menu";
	private ItemStack fill = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);

	private int slots = 27;
	private int horizPad = 1;
	private int vertPad = 0;

	private ArrayList<Inventory> pages = new ArrayList<Inventory>();
	private ArrayList<MenuOption> options = new ArrayList<MenuOption>();
	private ArrayList<MenuOption> permanent = new ArrayList<MenuOption>();
	
	private int currentPage = 0;

	public ArrayList<Inventory> generateMenu() {

		if ((vertPad * 2) >= (slots / 9)) vertPad = 0;
		//Log.debug("Vertical padding: " + vertPad);
		if (horizPad > 4) horizPad = 4;
		//Log.debug("Horizontal padding: " + horizPad);
		if (slots < 9) slots = 9;
		if (slots > 45) slots = 45;
		if (slots % 9 != 0) slots += slots % 9;

		int rows = slots / 9;
		//Log.debug("Total rows: " + rows);
		int availSpots = 9 - (horizPad * 2);
		//Log.debug("Available spots to put stuff: " + availSpots);
		int itemPerPage = availSpots * (rows - (vertPad * 2));
		//Log.debug("Items per page: " + itemPerPage);
		int numPages = (options.size() / itemPerPage) + 1;
		//Log.debug("Total number of pages: " + numPages);

		for (int i = 0; i < numPages; i++) { // Loop for creating pages
			pages.add(generatePage(i, numPages, rows, itemPerPage, availSpots));
		}

		return pages;
	}

	public Inventory generatePage(int pageNum) {
		if ((vertPad * 2) >= (slots / 9)) vertPad = 0;
		//Log.debug("Vertical padding: " + vertPad);
		if (horizPad > 4) horizPad = 4;
		//Log.debug("Horizontal padding: " + horizPad);
		if (slots < 9) slots = 9;
		if (slots > 45) slots = 45;
		if (slots % 9 != 0) slots += slots % 9;

		int rows = slots / 9;
		//Log.debug("Total rows: " + rows);
		int availSpots = 9 - (horizPad * 2);
		//Log.debug("Available spots to put stuff: " + availSpots);
		int itemPerPage = availSpots * (rows - (vertPad * 2));
		//Log.debug("Items per page: " + itemPerPage);
		int numPages = (options.size() / itemPerPage) + 1;
		//Log.debug("Total number of pages: " + numPages);

		return generatePage(pageNum, numPages, rows, itemPerPage, availSpots);
	}

	private Inventory generatePage(int pageNum, int totalPage, int totalRow, int itemPerPage, int availSpots) {
		String givenName = totalPage > 1 ? name + " - Page " + (pageNum + 1) : name;
		Inventory inv = Bukkit.createInventory(null, slots + 9, givenName); // Creates inventory with 9 extra slots

		int place = itemPerPage * pageNum;

		for (int pad = 0; pad < vertPad; pad++) // Loops thru the first vertical padding area
			for (int spot = 0; spot < 9; spot++) { // Loops thru spots in that row
				int spotIndex = spot + (pad * 9);
				inv.setItem(spotIndex, fill.clone()); // Fills with placeholder material
			}

		for (int r = vertPad; r < totalRow - vertPad; r++) { // Loops thru rows with menu options

			//Log.debug("Menu option size: " + options.size());
			
			int needPad = options.size() - place >= availSpots ? horizPad : 9 - (options.size() - place);
			//Log.debug("Need padding: " + needPad);

			int rightPad = needPad / 2;
			//Log.debug("Right padding: " + rightPad);
			int leftPad = needPad - rightPad;
			//Log.debug("Left padding: " + leftPad);

			for (int spot = 0; spot < 9; spot++) { // Spots in those rows

				int spotIndex = spot + (r * 9); // Calculates raw index for giving to bukkit

				if (spot < leftPad || spot >= 9 - rightPad) {
					inv.setItem(spotIndex, fill.clone()); // Does horizontal padding
					continue;
				}

				MenuOption option = options.get(place++);
				option.setPage(pageNum);
				option.setSlot(spotIndex);
				
				Log.debug("Setting page: " + pageNum);
				Log.debug("Setting spot index: " + spotIndex);
				
				inv.setItem(spotIndex, option.getItem());
			}
		}

		for (int pad = totalRow - vertPad; pad < totalRow; pad++) // Loops thru the second vertical padding area
			for (int spot = 0; spot < 9; spot++) { // Loops thru spots in that row
				int spotIndex = spot + (pad * 9);
				inv.setItem(spotIndex, fill.clone()); // Fills with placeholder material
			}
		
		if (pageNum > 0)
			inv.setItem(totalRow * 9, new ItemStack(Material.ARROW));
		else
			inv.setItem(totalRow * 9, fill.clone());
		
		if (pageNum + 1 < totalPage)
			inv.setItem(((totalRow + 1) * 9) - 1, new ItemStack(Material.ARROW));
		else
			inv.setItem(((totalRow + 1) * 9) - 1, fill.clone());
		
		int needPad = permanent.size() - place >= availSpots ? 0 : 9 - (permanent.size() - place);
		//Log.debug("Need padding: " + needPad);

		int rightPad = needPad / 2;
		//Log.debug("Right padding: " + rightPad);
		int leftPad = needPad - rightPad;
		//Log.debug("Left padding: " + leftPad);
		
		for (int spot = 1; spot < 8; spot++) {
			int spotIndex = spot + (totalRow * 9);
			
			if (spot < leftPad || spot >= 9 - rightPad) {
				inv.setItem(spotIndex, fill.clone()); // Does horizontal padding
				continue;
			}
		}

		return inv;
	}

	public ItemMenu addOption(MenuOption option) {
		options.add(option);
		return this;
	}

	public ArrayList<MenuOption> getOptions() {
		return options;
	}

	public ItemMenu setName(String name) {
		this.name = name;
		return this;
	}

	public String getName() {
		return name;
	}

	public ItemMenu setSlots(int slots) {
		this.slots = slots;
		return this;
	}

	public int getSlots() {
		return slots;
	}

	public ItemMenu setVerticalPadding(int padding) {
		this.vertPad = padding;
		return this;
	}

	public int getVerticalPadding() {
		return vertPad;
	}

	public ItemMenu setHorizontalPadding(int padding) {
		this.horizPad = padding;
		return this;
	}

	public int getHorizontalPadding() {
		return horizPad;
	}
	
	public Inventory getPage(int page) {
		return pages.get(page);
	}
	
	public int getCurrentPage() {
		return currentPage;
	}
	
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

}
