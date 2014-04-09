package com.games.dots.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.joda.time.Period;

import com.games.dots.logic.eventhandlers.GameExpiredHandler;
import com.games.dots.logic.eventhandlers.MoveExpiredHandler;
import com.games.dots.logic.eventhandlers.MoveExpiredHandlerEventArgs;

public class GameClock implements GameClockIntf {

	Map<String, MoveExpiredHandler> MoveExpiredMailingList = new HashMap<>();
	Map<String, GameExpiredHandler> GameExpiredMailingList = new HashMap<>();
	
	private String moveOwnerUserId;
	private List<String> userIds;
	//private Period gamePeriod;
	private Period movePeriod;
	private Period bonusPeriod;
	
	private Timer moveTimer = null;
	private Timer gameTimer = null;

	public GameClock(List<String> userIds, Period gamePeriod, Period movePeriod, Period bonusPeriod) {
		
		this.userIds = userIds;
		//this.gamePeriod = gamePeriod;
		this.movePeriod = movePeriod;
		this.bonusPeriod = bonusPeriod;
		
		gameTimer = new Timer();
		gameTimer.schedule(new GameExpiredTask(), gamePeriod.toStandardDuration().getMillis());
		
	}

	@Override
	public void switchUser(String userId, boolean hasBonus) throws IllegalArgumentException {
		
		if(!userIds.contains(userId))
		{
			throw new IllegalArgumentException(String.format("UserId %d doesn't belong to the game", userId));
		}
		
		//reset counter if previous move ended preliminary 
		if(moveTimer != null)
		{
			moveTimer.cancel();			
		}		
		//set move owner
		moveOwnerUserId = userId;
		
		//calculate move duration - base and bonus
		long overallMoveDuration =  movePeriod.toStandardDuration().getMillis();
		if(hasBonus)
		{
			overallMoveDuration += bonusPeriod.toStandardDuration().getMillis();
		}		
		
		moveTimer = new Timer();
		moveTimer.schedule(new MoveExpiredTask(), overallMoveDuration); 
	}
	
	
	public void Close() {
		// reset counter if previous move ended preliminary
		if (gameTimer != null) {
			gameTimer.cancel();
		}
	}	

//EVENTS  Section
	
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
	        	for (MoveExpiredHandler handler : MoveExpiredMailingList.values()) {
	        		
					 handler.handleEvent(new MoveExpiredHandlerEventArgs(moveOwnerUserId));
				}
	        }
	 }
	 
	 //notify all listeners that game ended
	 class GameExpiredTask extends TimerTask {

	        public void run() {	        	 
	        	//Game Ended
	        	for (GameExpiredHandler handler : GameExpiredMailingList.values()) {
					handler.handleEvent(null);
				}	           
	        }
	 }

}



