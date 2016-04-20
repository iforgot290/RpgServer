package me.neildennis.crypticrpg.cloud;

import java.io.PrintWriter;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CrossServerSend implements Runnable{

	private ConcurrentLinkedQueue<String> reqs;
	
	public CrossServerSend(){
		reqs = new ConcurrentLinkedQueue<String>();
	}
	
	@Override
	public void run() {
		while (true){
			PrintWriter print = ConnectionPool.getCloudWriter();
			
			if (print == null){
				System.out.println("Connection to the cloud lost... Reconnecting");
				try {
					Thread.sleep(1000L);
					continue;
				} catch (InterruptedException e) {
					e.printStackTrace();
					continue;
				}
				
			}
			
			while (!reqs.isEmpty()){
				print.println(reqs.poll());
				print.flush();
			}
			
			try {
				Thread.sleep(200L);
			} catch (Exception e){
				e.printStackTrace();
			}
			
			
		}
	}
	
	void sendCrossServer(String query){
		reqs.offer(query);
	}

}
