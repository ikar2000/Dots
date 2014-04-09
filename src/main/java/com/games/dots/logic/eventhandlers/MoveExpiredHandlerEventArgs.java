package com.games.dots.logic.eventhandlers;

public class MoveExpiredHandlerEventArgs extends DotsEventArgs {

	private String userId;
	
	public MoveExpiredHandlerEventArgs( String userId)
	{
		this.userId = userId;
	}
		
	public String getUserId()
	{
		return userId;
	}
}
