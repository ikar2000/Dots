package com.games.dots.logic;

import com.games.dots.logic.eventhandlers.GameExpiredHandler;
import com.games.dots.logic.eventhandlers.MoveExpiredHandler;

public interface GameClockIntf {

	void switchUser(String userId, boolean hasBonus);
	
	void SubscribeToMoveExpiredEvent(String GameId, MoveExpiredHandler handler);
	void UnSubscribeToMoveExpiredEvent(String GameId);
	
	void SubscribeToGameTimerExpiredEvent(String GameId, GameExpiredHandler handler);
	void UnSubscribeToGameTimerExpiredEvent(String GameId);
}
