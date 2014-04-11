package com.games.dots.logic;


import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.joda.time.Period;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.games.dots.logic.eventhandlers.DotsEventArgs;
import com.games.dots.logic.eventhandlers.GameExpiredHandler;
import com.games.dots.logic.eventhandlers.MoveExpiredHandler;
import com.games.dots.logic.eventhandlers.MoveExpiredHandlerEventArgs;


public class GameClockTests {

	boolean isGameExpiredEventRecieved = false;
	//key is UserId, value is number of moves
	Map<String, Integer> moveExpiredEvents = null;
	
	@Before
	public void Before()
	{
		isGameExpiredEventRecieved = false;
		moveExpiredEvents = new HashMap<>();
	}
	
	@Test
	public void gameTimerTest_registerHandler_recieveGameTimerExpiredEvent() throws InterruptedException
	{
		//prepare
		String firstUserId = UUID.randomUUID().toString();
		String secondUserId = UUID.randomUUID().toString();		
		List<String> userIds = Arrays. asList(firstUserId, secondUserId);
		
		Period gamePeriod = new Period().withSeconds(1);
		Period movePeriod = new Period().withMillis(100);
		Period bonusPeriod = new Period().withMillis(100);
		
		IGameClock clock =  GameClockFactory.createGameClock(userIds, gamePeriod, movePeriod, bonusPeriod);
		
		
		
		clock.SubscribeToGameTimerExpiredEvent("GameId", new GameExpiredHandler(){
			@Override
			public void handleEvent(DotsEventArgs args) {

				isGameExpiredEventRecieved = true;
			}
		});
		
		//act
		//start the game 
		clock.switchUser(firstUserId, false);
		
		//wait
		Thread.sleep(1500);
		
		//assert
		Assert.assertTrue(isGameExpiredEventRecieved);
		
	}
	
	@Test
	public void moveTimerTest_registerHandler_Make2move_recieve2MoveExpiredEvent() throws InterruptedException
	{
		//prepare
		String firstUserId = UUID.randomUUID().toString();
		String secondUserId = UUID.randomUUID().toString();		
		List<String> userIds = Arrays. asList(firstUserId, secondUserId);
		
		Period gamePeriod = new Period().withSeconds(13);
		Period movePeriod = new Period().withMillis(100);
		Period bonusPeriod = new Period().withMillis(100);
		
		IGameClock clock =  GameClockFactory.createGameClock(userIds, gamePeriod, movePeriod, bonusPeriod);
		
		
		//Game Expired Handler
		clock.SubscribeToGameTimerExpiredEvent("GameId", new GameExpiredHandler(){
			@Override
			public void handleEvent(DotsEventArgs args) {

				isGameExpiredEventRecieved = true;
			}
		});
		
		//Move Expired Handler
		clock.SubscribeToMoveExpiredEvent("GameId", new MoveExpiredHandler(){
			@Override
			public void handleEvent(MoveExpiredHandlerEventArgs args) {
				String userId =  args.getUserId();
				if(moveExpiredEvents.containsKey(userId))
				{
					moveExpiredEvents.put(userId, moveExpiredEvents.get(userId) + 1);					
				}
				else
				{
					moveExpiredEvents.put(userId, 1);					
				}
			}
		});
		
		//act
		//start the game 
		clock.switchUser(firstUserId, false);
		Thread.sleep(200);
		clock.switchUser(secondUserId, false);
		
		//wait
		Thread.sleep(200);
		
		//assert
		Assert.assertFalse(isGameExpiredEventRecieved);
		Assert.assertEquals(moveExpiredEvents.get(firstUserId), Integer.valueOf(1));
		Assert.assertEquals(moveExpiredEvents.get(firstUserId), Integer.valueOf(1));
		
		//end
		clock.Close();
		
	}
}
