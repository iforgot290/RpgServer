package me.neildennis.crypticrpg.utils;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.entity.Player;

/**
 * 
 * How To Use =================
 * 
 * new FancyMessage("Click") 
 *   .then(ChatColor.RED+"Here")
 *   .tooltip("You're now hovering over me!") .command("Thanks for clicking!")
 *   .send(<Player>)
 * 
 */
public class FancyMessage {

	private TextComponent	text;
	private TextComponent	all	= new TextComponent("");

	public FancyMessage(String string) {
		text = new TextComponent(string);
	}

	/**
	 * Adds a new string section to the message, this allows new tooltips, and
	 * events.
	 * 
	 * @param string
	 * @return
	 */
	public FancyMessage then(String string) {
		all.addExtra(text);
		text = new TextComponent(string);
		return this;
	}

	/**
	 * Tooltips are what appear when you hover over the message
	 * 
	 * @param strings
	 * @return
	 */
	public FancyMessage tooltip(String... strings) {
		ComponentBuilder component = new ComponentBuilder(strings[0]);
		for (int i = 1; i != strings.length; i++) {
			component.append("\n");
			component.append(strings[i]);
		}
		text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, component.create()));
		return this;
	}

	/**
	 * The command/Message the player will use/say when they click the message
	 * 
	 * To make it a command, don't forget to have a "/"
	 * 
	 * @param string
	 * @return
	 */
	public FancyMessage command(String string) {
		text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, string));
		return this;
	}

	/**
	 * Autotypes a message into their chatbar when they click the message
	 * 
	 * @param string
	 * @return
	 */
	public FancyMessage suggest(String string) {
		text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, string));
		return this;
	}
	/**
	 * Opens the link for the player when they click the message
	 * 
	 * @param string
	 * @return
	 */
	public FancyMessage link(String string) {
		text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, string));
		return this;
	}

	/**
	 * Sends the message to a player
	 * 
	 * @param player
	 */
	public void send(Player player) {
		if (text != null) all.addExtra(text);

		player.spigot().sendMessage(all);
	}
	
	public TextComponent getMessage(){
		if (text != null) all.addExtra(text);
		return all;
	}

	/**
	 * Sends the message to an array of players
	 * 
	 * @param players
	 */
	public void send(Player[] players) {
		if (text != null) all.addExtra(text);

		for (Player player : players)
			player.spigot().sendMessage(all);

	}

}