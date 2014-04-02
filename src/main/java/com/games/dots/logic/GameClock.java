package com.games.dots.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;

import org.joda.time.Period;
import org.joda.time.ReadableInstant;

import com.games.dots.logic.handlers.GameExpiredHandler;
import com.games.dots.logic.handlers.MoveExpiredHandler;

public class GameClock implements GameClockIntf {

	Map<String, MoveExpiredHandler> MoveExpiredMailingList = new HashMap<>();
	Map<String, GameExpiredHandler> GameExpiredMailingList = new HashMap<>();
	
	
	private List<String> userIds;
	//private Period gamePeriod;
	private Period movePeriod;
	private Period bonusPeriod;
	
	private Timer moveTimer = null;

	public GameClock(List<String> userIds, Period gamePeriod, Period movePeriod, Period bonusPeriod) {
		
		this.userIds = userIds;
		//this.gamePeriod = gamePeriod;
		this.movePeriod = movePeriod;
		this.bonusPeriod = bonusPeriod;
		
		Timer gameTimer = new Timer();
		gameTimer.schedule(new GameExpiredTask(), gamePeriod.toStandardDuration().getMillis());
		
	}

	@Override
	public void switchUser(String userId, boolean hasBonus) {
		
		if(moveTimer != null)
		{
			moveTimer.cancel();			
		}		
		
		long overallMoveDuration =  movePeriod.toStandardDuration().getMillis();
		if(hasBonus)
		{
			overallMoveDuration += bonusPeriod.toStandardDuration().getMillis();
		}
		
		
		moveTimer = new Timer();
		moveTimer.schedule(new MoveExpiredTask(), overallMoveDuration);
		 
		 
	}


//Events
	
	@Override
	public void SubscribeToMoveExpiredEvent(String GameId, MoveExpiredHandler o) {
		MoveExpiredMailingList.put(GameId, o);		
	}

	@Override
	public void UnSubscribeToMoveExpiredEvent(String GameId) {
		MoveExpiredMailingList.remove(GameId);
		
	}

	@Override
	public void SubscribeToGameTimerExpiredEvent(String GameId, GameExpiredHandler o) {
		GameExpiredMailingList.put(GameId, o);
		
	}

	@Override
	public void UnSubscribeToGameTimerExpiredEvent(String GameId) {
		GameExpiredMailingList.remove(GameId);
		
	}
	
	 //notify all listeners that move time expired
	 class MoveExpiredTask extends TimerTask {
	        public void run() {      
	        	
	        	//Game Ended
	        	for (GameExpiredHandler handler : GameExpiredMailingList.values()) {
					//TODO: handler.handleEvent(pass userId or eventArgs);
				}
	        }
	 }
	 
	 //notify all listeners that game ended
	 class GameExpiredTask extends TimerTask {

	        public void run() {	        	 
	        	//Game Ended
	        	for (GameExpiredHandler handler : GameExpiredMailingList.values()) {
					handler.handleEvent();
				}
	           
	        }
	 }

}



