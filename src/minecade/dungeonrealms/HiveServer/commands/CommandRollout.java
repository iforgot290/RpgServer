package minecade.dungeonrealms.HiveServer.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import minecade.dungeonrealms.Main;
import minecade.dungeonrealms.CommunityMechanics.CommunityMechanics;
import minecade.dungeonrealms.HiveServer.HiveServer;
import minecade.dungeonrealms.RealmMechanics.RealmMechanics;
import minecade.dungeonrealms.config.Config;

public class CommandRollout implements CommandExecutor {
	
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			p.sendMessage(ChatColor.RED + "You cannot issue this command from anywhere but the console window.");
			return true;
		}
		
		String ip = "*";
		
		if(args.length > 0) {
			ip = args[0];
		}
		
		final String taskstr = ip;
		
		if(HiveServer.isThisRootMachine()) {
			
			Bukkit.getScheduler().scheduleAsyncDelayedTask(Main.plugin, new Runnable(){

				@Override
				public void run() {
					Bukkit.broadcastMessage(ChatColor.GREEN+"Uploading new patch to distribution server...");
					
					try {
						uploadPlugin();
						new File(Config.getRootDir()+"/payload.zip").delete();
						Bukkit.broadcastMessage(ChatColor.GREEN+""+ChatColor.BOLD+"SUCCESS! "+ChatColor.GREEN+"Commencing rollout...");
					} catch (IOException e) {
						e.printStackTrace();
						Bukkit.broadcastMessage(ChatColor.RED+""+ChatColor.BOLD+"ERROR: "+ChatColor.RED+"Could not upload patch... cancelling all rollouts");
						return;
					}
					
					if(taskstr.equalsIgnoreCase("*")) {
						//send8008Packet("@rollout@", null, true);
						CommunityMechanics.sendPacketCrossServer("@rollout@", -1, true);
					} else {
						CommunityMechanics.sendPacketCrossServer("@rollout@", taskstr);
						//send8008Packet("@rollout@", args[0], false);
					}
				}
				
			});
			
		}
		
		if(HiveServer.isThisRootMachine()) {
			/*for(Player p : Bukkit.getServer().getOnlinePlayers()) {
				p.saveData();
				p.kickPlayer("Launching a Content Patch to ALL #DungeonRealms Servers...");
			}
			
			World w = Bukkit.getWorlds().get(0);
			Bukkit.unloadWorld(w, true);
			
			Bukkit.shutdown();*/
			return true;
		}
		return true;
	}
	
	private void uploadPlugin() throws IOException{
		String rootdir = Config.getRootDir();
		RealmMechanics.zipFile(new File(rootdir+"/plugins/dungeonrealms.jar"), new File(rootdir+"/payload.zip"));
		
		URL url = new URL("ftp://" + Config.ftp_user + ":" + Config.ftp_pass + "@" + Config.Hive_IP +  "/sdata/payload.zip");
		URLConnection urlc = url.openConnection();
		OutputStream out = urlc.getOutputStream();

		InputStream is = new FileInputStream(rootdir + "/payload.zip");

		byte buf[] = new byte[1024];
		int len;

		while ((len = is.read(buf)) > 0) {
			out.write(buf, 0, len);
		}

		out.close();
		is.close();
	}
	
}