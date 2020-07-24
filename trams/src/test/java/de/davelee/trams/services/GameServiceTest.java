package de.davelee.trams.services;

import java.util.Calendar;
import java.util.GregorianCalendar;

import de.davelee.trams.model.GameModel;
import de.davelee.trams.repository.GameRepository;
import de.davelee.trams.util.DifficultyLevel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.davelee.trams.util.DateFormats;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class GameServiceTest {
	
	@Autowired
	private GameService gameService;

	@Autowired
	private GameRepository gameRepository;
	
	@Test
	public void testIncrement() {
		GameModel gameModel = new GameModel();
		gameModel.setPlayerName("Dave A J Lee");
		gameModel.setScenarioName("Landuff Transport Company");
		gameModel.setBalance(80000.0);
		gameModel.setCurrentTime(new GregorianCalendar(2009,Calendar.AUGUST,20,5,0,0));
		gameModel.setTimeIncrement(15);
		gameModel.setDifficultyLevel(DifficultyLevel.EASY);
		gameModel.setPassengerSatisfaction(100);
		gameService.saveGame(gameModel);
		Assertions.assertNotNull(gameService.getGameByPlayerName("Dave A J Lee"));
		Assertions.assertNull(gameService.getGameByPlayerName("My First Name"));
		GameModel gameModel2 = gameService.getGameByPlayerName("Dave A J Lee");
		Assertions.assertEquals(DateFormats.HOUR_MINUTE_FORMAT.getFormat().format(gameModel2.getCurrentTime().getTime()), "05:00");
		gameService.incrementTime("Dave A J Lee");
		gameModel2 = gameService.getGameByPlayerName("Dave A J Lee");
		Assertions.assertEquals(DateFormats.HOUR_MINUTE_FORMAT.getFormat().format(gameModel2.getCurrentTime().getTime()), "05:15");
		gameService.withdrawBalance(100.0, "Dave A J Lee");
		gameService.creditBalance(10.0, "Dave A J Lee");
		Assertions.assertEquals(gameService.computeAndReturnPassengerSatisfaction("Dave A J Lee", 4, 3, 2), 91);
	}

}
