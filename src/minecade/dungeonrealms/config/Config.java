package minecade.dungeonrealms.config;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;

import minecade.dungeonrealms.RealmMechanics.RealmMechanics;

public class Config {





	// Watcha doin here?



























	// U sure u wanna be here?












































	// Well okay...































	public static List<String> us_public_servers = new ArrayList<String>(Arrays.asList("US-1", "US-5", "US-3", "US-4"));
	public static List<String> us_beta_servers = new ArrayList<String>(Arrays.asList("US-100"/*, "US-101", "US-102", "US-103", "US-104", "US-105", "US-106", "US-107", "US-108", "US-109", "US-110"*/));
	public static List<String> us_private_servers = new ArrayList<String>(Arrays.asList("US-2"));


	public static int transfer_port = 3306;
	public static String Hive_IP = "69.69.69.69";

	public static int SQL_port = 3306;
	public static String sql_user = "";
	public static String sql_password = "";
	public static String sql_url = "jdbc:mysql://" + Hive_IP + ":" + SQL_port + "/dungeonrealms";

	public static int FTP_port = 21;
	public static String ftp_user = "";
	public static String ftp_pass = ""; 

	public static String version = "1.9 BETA";

	public static String local_IP = Bukkit.getIp();

	public static String realmPath = "/rdata/";

	public static String getRootDir(){
		String rootDir = "";
		CodeSource codeSource = RealmMechanics.class.getProtectionDomain().getCodeSource();
		File jarFile = null;
		try {
			jarFile = new File(codeSource.getLocation().toURI().getPath());
		} catch (URISyntaxException e1) {
		}
		rootDir = jarFile.getParentFile().getPath();
		int rep = rootDir.contains("/plugins") ? rootDir.indexOf("/plugins") : rootDir.indexOf("\\plugins");
		rootDir = rootDir.substring(0, rep);
		return rootDir;
	}
}