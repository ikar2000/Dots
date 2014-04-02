package com.games.dots.logic;

import java.util.List;

import org.joda.time.Period;

public class GameClockFactory {
	
	/**
	 * Create Game Clock 
	 * @param UserIds - list of players identifications
	 * @param gamePeriod - total game length
	 * @param movePeriod  - single move length 
	 * @param bonusPeriod - bonus for appropriate move
	 * @return
	 */
	public static GameClockIntf createGameClock(List<String> UserIds, Period gamePeriod, Period movePeriod, Period bonusPeriod)
	{
		//TODO: all periods might have default values
		
		return new GameClock(UserIds, gamePeriod, movePeriod, bonusPeriod);
	}

}
