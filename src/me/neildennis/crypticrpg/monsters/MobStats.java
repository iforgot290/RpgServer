package me.neildennis.crypticrpg.monsters;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class MobStats {
	
	private static String filepath = "plugins/CrypticRpg/monsters/stats/";
	
	public MobStats(MobType type, int tier) throws JsonIOException, JsonSyntaxException, FileNotFoundException{
		JsonParser parse = new JsonParser();
		JsonObject obj = (JsonObject) parse.parse(new FileReader(filepath + type.name().toLowerCase() + ".template"));
	}

}
