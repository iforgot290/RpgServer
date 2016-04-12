package me.neildennis.crypticrpg.player;

public class PlayerBuff{

	private int ticksleft;
	private Runnable run;
	private boolean interruptable;
	
	public PlayerBuff(Runnable run, int ticks, boolean interruptable){
		this.run = run;
		this.ticksleft = ticks;
		this.interruptable = interruptable;
	}
	
	public void tickBuff(){
		run.run();
		ticksleft--;
	}

	public boolean isExpired(){
		return ticksleft <= 0;
	}
	
	public boolean isInterruptable(){
		return interruptable;
	}
	
}

