package de.davelee.trams.services;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.GregorianCalendar;

import de.davelee.trams.model.GameModel;
import de.davelee.trams.repository.GameRepository;
import de.davelee.trams.util.DifficultyLevel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.davelee.trams.util.DateFormats;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class GameServiceTest {
	
	@Autowired
	private GameService gameService;

	@Autowired
	private GameRepository gameRepository;
	
	@Test
	public void testIncrement() {
		assertEquals(gameService.getCurrentPlayerName(), "John Smith");
		GameModel gameModel = new GameModel();
		gameModel.setPlayerName("Dave A J Lee");
		gameModel.setScenarioName("Landuff Transport Company");
		gameModel.setBalance(80000.0);
		gameModel.setCurrentTime(new GregorianCalendar(2009,Calendar.AUGUST,20,5,0,0));
		gameModel.setTimeIncrement(15);
		gameModel.setDifficultyLevel(DifficultyLevel.EASY);
		gameModel.setPassengerSatisfaction(100);
		gameModel.setPreviousTime((Calendar) gameModel.getCurrentTime().clone());
		gameService.saveGame(gameModel);
		assertNotNull(gameService.getGameByPlayerName("Dave A J Lee"));
		assertNull(gameService.getGameByPlayerName("My First Name"));
		assertEquals(gameService.getCurrentPlayerName(), "John Smith");
		GameModel gameModel2 = gameService.getGameByPlayerName("Dave A J Lee");
		assertEquals(DateFormats.HOUR_MINUTE_FORMAT.getFormat().format(gameModel2.getCurrentTime().getTime()), "05:00");
		gameService.incrementTime("Dave A J Lee");
		gameModel2 = gameService.getGameByPlayerName("Dave A J Lee");
		assertEquals(DateFormats.HOUR_MINUTE_FORMAT.getFormat().format(gameModel2.getCurrentTime().getTime()), "05:15");
		gameService.withdrawBalance(100.0, "Dave A J Lee");
		gameService.creditBalance(10.0, "Dave A J Lee");
		assertEquals(gameService.computeAndReturnPassengerSatisfaction("Dave A J Lee", 4, 3, 2), 100);
	}
	
	@Test
	public void testFormat() {
		Calendar calendar = Calendar.getInstance(); calendar.set(2014,4,20,22,57);
		assertEquals(gameService.formatDateString(calendar, DateFormats.FULL_FORMAT), "Tuesday, 20 May 2014");
	}

}
