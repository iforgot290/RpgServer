package me.neildennis.crypticrpg.permission;

import java.sql.ResultSet;
import java.sql.SQLException;

import me.neildennis.crypticrpg.player.CrypticPlayer;

public class RankData {
	
	private CrypticPlayer pl;
	private Rank rank = Rank.NORMAL;
	
	public RankData(CrypticPlayer pl, ResultSet data){
		this.pl = pl;
		
		try {
			rank = Rank.valueOf(data.getString("rank"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Rank getRank(){
		return rank;
	}
	
	public void setRank(Rank rank){
		this.rank = rank;
	}

}
