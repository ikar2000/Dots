package com.games.dots.ui.entities;

public interface IPlayer {
	int getId();
	String getGameId();
	String getColor();
	int getScore();
	void setScore(int score);
}
