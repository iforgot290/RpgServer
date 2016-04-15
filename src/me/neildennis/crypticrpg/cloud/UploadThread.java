package me.neildennis.crypticrpg.cloud;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UploadThread implements Runnable{
	
	private ConcurrentLinkedQueue<String> queue;
	
	public UploadThread(){
		queue = new ConcurrentLinkedQueue<String>();
	}
	
	@Override
	public void run(){
		while (!queue.isEmpty()){
			try {
				Statement statement = ConnectionPool.getConnection().createStatement();
				statement.execute(queue.poll());
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
