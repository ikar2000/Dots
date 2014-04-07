package com.games.dots.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.games.dots.logic.Game;
import com.games.dots.ui.entities.GameMessage;
import com.games.dots.ui.entities.Coordinates;
import com.games.dots.ui.entities.GameSettings;
import com.games.dots.ui.entities.IdType;
import com.games.dots.ui.entities.Move;
import com.games.dots.ui.entities.Player;
import com.games.dots.ui.entities.UserId;


public class GameTests {
	@Test
	public void gameCreationTest(){
		GameSettings settings = new GameSettings();
		settings.players = 2;
		
		settings.size = "Small";
		Game smallGame = new Game(settings);
		settings.size = "Medium";
		Game mediumGame = new Game(settings);
		settings.size = "Big";
		Game largeGame = new Game(settings);

		assertNotNull(smallGame);
		assertNotNull(mediumGame);
		assertNotNull(largeGame);		
	}
	
	@Test
	public void SimpleCycleTest(){
		//Arrange
		GameSettings settings = new GameSettings();
		settings.players = 2;
		settings.size = "Small";
		Game smallGame = new Game(settings);
		Player player1 = createPlayer();player1.color = "red";
		Player player2 = createPlayer();player2.color = "green";
		smallGame.addPlayer(player1);
		smallGame.addPlayer(player2);
		
		Coordinates[] coordinates = new Coordinates[8];
		coordinates[0] = new Coordinates(1, 1);
		coordinates[1] = new Coordinates(0, 1);
		coordinates[2] = new Coordinates(5, 5);
		coordinates[3] = new Coordinates(1, 0);
		coordinates[4] = new Coordinates(5, 6);
		coordinates[5] = new Coordinates(1, 2);
		coordinates[6] = new Coordinates(5, 7);
		coordinates[7] = new Coordinates(2, 1);
		Move[] moves = new Move[8];
		moves[0] = new Move(player1.id, coordinates[0]);
		moves[1] = new Move(player2.id, coordinates[1]);
		moves[2] = new Move(player1.id, coordinates[2]);
		moves[3] = new Move(player2.id, coordinates[3]);
		moves[4] = new Move(player1.id, coordinates[4]);
		moves[5] = new Move(player2.id, coordinates[5]);
		moves[6] = new Move(player1.id, coordinates[6]);
		moves[7] = new Move(player2.id, coordinates[7]);
		
		
		//Act
		for (int i = 0; i < moves.length-1 ;i++) {
			smallGame.makeMove(moves[i]);
		}		
		
		GameMessage actionList = smallGame.makeMove(moves[moves.length-1]);
		
		//Assert		
		assertEquals(1, actionList.newCycles.size());
		assertEquals(1, actionList.newDeadDots.size());
		
	}
	
	@Test
	public void BigCycleTest(){
		//Arrange
		
		GameSettings settings = new GameSettings();
		settings.players = 2;
		settings.size = "Small";
		Game smallGame = new Game(settings);
		Player player1 = new Player();player1.id = 0;		
		Player player2 = new Player();player2.id = 1;
		smallGame.addPlayer(player1);
		smallGame.addPlayer(player2);
		
		List<Move> moves = new ArrayList<>();
		moves.add(new Move(player1.id, new Coordinates(1,1)));
		moves.add(new Move(player2.id, new Coordinates(1,0)));
		moves.add(new Move(player1.id, new Coordinates(1,2)));
		moves.add(new Move(player2.id, new Coordinates(0,1)));
		moves.add(new Move(player1.id, new Coordinates(8,2)));
		moves.add(new Move(player2.id, new Coordinates(0,2)));
		moves.add(new Move(player1.id, new Coordinates(8,3)));
		moves.add(new Move(player2.id, new Coordinates(0,3)));
		moves.add(new Move(player1.id, new Coordinates(8,4)));
		moves.add(new Move(player2.id, new Coordinates(2,1)));
		moves.add(new Move(player1.id, new Coordinates(8,5)));
		moves.add(new Move(player2.id, new Coordinates(2,2)));
		moves.add(new Move(player1.id, new Coordinates(8,6)));
		moves.add(new Move(player2.id, new Coordinates(2,3)));
		moves.add(new Move(player1.id, new Coordinates(8,7)));
		moves.add(new Move(player2.id, new Coordinates(1,3)));
		
		//Act		
		for (int i = 0; i< moves.size()-1;i++) {
			smallGame.makeMove(moves.get(i));
		}
		
		GameMessage actionList = smallGame.makeMove(moves.get(moves.size()-1));
		
		//Assert		
		assertEquals(1, actionList.newCycles.size());
		assertEquals(2, actionList.newDeadDots.size());
		
	}
	
	private Player createPlayer() {
		Player player = new Player();
		player.userId = new UserId();		
		player.userId.id = UUID.randomUUID().toString();
		player.userId.type = IdType.FBUser;
		
		return player;
	}
	
}

