package de.davelee.trams.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.util.DifficultyLevel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class GameServiceTest {
	
	@Autowired
	private GameService gameService;
	
	@Test
	public void testIncrement() {
		gameService.saveGame(GameModel.builder()
				.playerName("Dave A J Lee")
				.scenarioName("Landuff Transport Company")
				.balance(80000.0)
				.currentDateTime(LocalDateTime.of(2009,8,20,5,0,0))
				.timeIncrement(15)
				.difficultyLevel(DifficultyLevel.EASY)
				.passengerSatisfaction(100)
				.build());
		Assertions.assertNotNull(gameService.getGameByPlayerName("Mustermann GmbH", "Dave A J Lee"));
		Assertions.assertNull(gameService.getGameByPlayerName("Mustermann GmbH", "My First Name"));
		GameModel gameModel2 = gameService.getGameByPlayerName("Mustermann GmbH", "Dave A J Lee");
		assertEquals(DateTimeFormatter.ofPattern("HH:mm").format(gameModel2.getCurrentDateTime()), "05:00");
		gameService.incrementTime("Mustermann GmbH", 15);
		gameModel2 = gameService.getGameByPlayerName("Mustermann GmbH", "Dave A J Lee");
		assertEquals(DateTimeFormatter.ofPattern("HH:mm").format(gameModel2.getCurrentDateTime()), "05:15");
		gameService.withdrawOrCreditBalance(-100.0, "Dave A J Lee");
		gameService.withdrawOrCreditBalance(10.0, "Dave A J Lee");
		Assertions.assertEquals(gameService.computeAndReturnPassengerSatisfaction("Mustermann GmbH", DifficultyLevel.EASY, 4, 3, 2), 91);
	}

	private void assertEquals ( final String expected, final String actual ) {
		Assertions.assertEquals(expected, actual);
	}

}
