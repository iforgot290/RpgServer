package me.neildennis.crypticrpg.cloud.queries;

public interface QueryExecutor {
	
	public void onQuery(int server, String command, String args);

}
