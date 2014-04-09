package com.games.dots.logic.eventhandlers;

public interface DotsEventHandlerIntf<T extends DotsEventArgs> {
	
	void handleEvent(T args);

}
