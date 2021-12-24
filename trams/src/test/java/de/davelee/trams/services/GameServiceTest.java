package de.davelee.trams.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.api.request.AddTimeRequest;
import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.api.response.TimeResponse;
import de.davelee.trams.model.GameModel;
import de.davelee.trams.util.DifficultyLevel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class GameServiceTest {

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private GameService gameService;
	
	@Test
	public void testIncrement() {
		Mockito.when(restTemplate.postForObject(anyString(), any(), eq(Void.class))).thenReturn(null);
		gameService.saveGame(GameModel.builder()
				.playerName("Dave A J Lee")
				.scenarioName("Landuff Transport Company")
				.balance(80000.0)
				.currentDateTime(LocalDateTime.of(2009,8,20,5,0,0))
				.difficultyLevel(DifficultyLevel.EASY)
				.passengerSatisfaction(100)
				.build());
		Mockito.when(restTemplate.getForObject(anyString(), eq(CompanyResponse.class))).thenReturn(null);
		Assertions.assertNull(gameService.getGameByPlayerName("Mustermann GmbH", "My First Name"));
		Mockito.when(restTemplate.getForObject(anyString(), eq(CompanyResponse.class))).thenReturn(
				CompanyResponse.builder().name("Mustermann GmbH").playerName("Dave A J Lee").difficultyLevel("EASY")
						.time("24-12-2020 11:58").build()
		);
		Assertions.assertNotNull(gameService.getGameByPlayerName("Mustermann GmbH", "Dave A J Lee"));
		GameModel gameModel2 = gameService.getGameByPlayerName("Mustermann GmbH", "Dave A J Lee");
		assertEquals(DateTimeFormatter.ofPattern("HH:mm").format(gameModel2.getCurrentDateTime()), "11:58");
		Mockito.when(restTemplate.patchForObject(anyString(), eq(AddTimeRequest.class), eq(TimeResponse.class))).thenReturn(
				TimeResponse.builder()
						.company("Mustermann GmbH")
						.time("24-12-2020 12:13")
						.build()
		);
		LocalDateTime localDateTime = gameService.incrementTime("Mustermann GmbH", 15);
		assertEquals(DateTimeFormatter.ofPattern("HH:mm").format(localDateTime), "12:13");
		gameService.withdrawOrCreditBalance(-100.0, "Dave A J Lee");
		gameService.withdrawOrCreditBalance(10.0, "Dave A J Lee");
		Assertions.assertEquals(gameService.computeAndReturnPassengerSatisfaction("Mustermann GmbH", DifficultyLevel.EASY, 4, 3, 2), 91);
	}

	private void assertEquals ( final String expected, final String actual ) {
		Assertions.assertEquals(expected, actual);
	}

}
