package de.davelee.trams.services;

import java.util.Calendar;
import java.util.GregorianCalendar;

import de.davelee.trams.data.Game;
import de.davelee.trams.model.GameModel;
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
	
	@Test
	public void testIncrement() {
		gameService.saveGame(GameModel.builder()
				.playerName("Dave A J Lee")
				.scenarioName("Landuff Transport Company")
				.balance(80000.0)
				.currentTime(new GregorianCalendar(2009,Calendar.AUGUST,20,5,0,0))
				.timeIncrement(15)
				.difficultyLevel(DifficultyLevel.EASY)
				.passengerSatisfaction(100)
				.build());
		Assertions.assertNotNull(gameService.getGameByPlayerName("Dave A J Lee"));
		Assertions.assertNull(gameService.getGameByPlayerName("My First Name"));
		GameModel gameModel2 = gameService.getGameByPlayerName("Dave A J Lee");
		assertEquals(DateFormats.HOUR_MINUTE_FORMAT.getFormat().format(gameModel2.getCurrentTime().getTime()), "05:00");
		gameService.incrementTime("Dave A J Lee");
		gameModel2 = gameService.getGameByPlayerName("Dave A J Lee");
		assertEquals(DateFormats.HOUR_MINUTE_FORMAT.getFormat().format(gameModel2.getCurrentTime().getTime()), "05:15");
		gameService.withdrawBalance(100.0, "Dave A J Lee");
		gameService.creditBalance(10.0, "Dave A J Lee");
		Assertions.assertEquals(gameService.computeAndReturnPassengerSatisfaction("Dave A J Lee", 4, 3, 2), 91);
	}

	private void assertEquals ( final String expected, final String actual ) {
		Assertions.assertEquals(expected, actual);
	}

}
