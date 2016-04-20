package me.neildennis.crypticrpg.cloud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;

import me.neildennis.crypticrpg.cloud.queries.QueryExecutor;
import me.neildennis.crypticrpg.cloud.queries.RankQuery;

public class CrossServerReceive implements Runnable {
	
	private HashMap<String, QueryExecutor> executors;
	
	public CrossServerReceive(){
		executors = new HashMap<String, QueryExecutor>();
		
		executors.put("rank", new RankQuery());
	}

	@Override
	public void run() {
		while (true){
			Socket cloud = ConnectionPool.getCloudSocket();

			if (cloud == null || cloud.isClosed()){
				try {
					System.out.println("Cannot read from cloud... Retrying");
					Thread.sleep(1000L);
					continue;
				} catch (Exception e){
					e.printStackTrace();
					continue;
				}
			}

			BufferedReader in;
			String line;

			try {
				in = new BufferedReader(new InputStreamReader(cloud.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			
			try {
				while ((line = in.readLine()) != null){
					handleQuery(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
	}
	
	private void handleQuery(String line){
		if (line.equalsIgnoreCase("")) return;
		
		String[] args = line.split(" ");
		
		if (args.length < 2) return;
		
		int server = Integer.parseInt(args[0]);
		String cmd = args[1];
		String query = line.replaceAll(args[0] + " " + args[1] + " ", "");
		
		QueryExecutor exec = executors.get(cmd);
		exec.onQuery(server, cmd, query);
	}

}
