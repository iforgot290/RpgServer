package me.neildennis.crypticrpg.cloud;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueryThread implements Runnable{

	private ConcurrentLinkedQueue<String> queue;

	public QueryThread(){
		queue = new ConcurrentLinkedQueue<String>();
	}

	@Override
	public void run(){
		while (true){
			while (!queue.isEmpty()){
				try {
					Statement statement = ConnectionPool.getConnection().createStatement();
					statement.execute(queue.poll());
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	protected void addQuery(String str){
		queue.offer(str);
	}

}
