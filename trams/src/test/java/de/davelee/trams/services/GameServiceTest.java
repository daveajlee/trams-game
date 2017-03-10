package de.davelee.trams.services;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import de.davelee.trams.db.DatabaseManager;
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
	private DatabaseManager databaseManager;
	
	@Test
	public void testIncrement() {
		databaseManager.createAndStoreGame(gameService.createGame("Dave Lee", "Landuff Transport Company"));
		assertNotNull(gameService.getGame());
		assertEquals(DateFormats.HOUR_MINUTE_FORMAT.getFormat().format(gameService.getCurrentTime().getTime()), "05:00");
		gameService.setTimeIncrement(15);
		gameService.incrementTime();
		assertEquals(DateFormats.HOUR_MINUTE_FORMAT.getFormat().format(gameService.getCurrentTime().getTime()), "05:15");
	}
	
	@Test
	public void testFormat() {
		Calendar calendar = Calendar.getInstance(); calendar.set(2014,4,20,22,57);
		assertEquals(gameService.formatDateString(calendar, DateFormats.FULL_FORMAT), "Tuesday, 20 May 2014");
	}

}
