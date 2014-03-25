package com.games.dots.logic;

public interface GameClockIntf {

	void nextUser(String userId);
	
	void RegisterToMoveExpiredEvent(Object o);
	
	void RegisterToGameTimerExpiredEvent(Object o);
}
