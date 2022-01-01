package de.davelee.trams.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.api.request.CompanyRequest;
import de.davelee.trams.api.response.CompanyResponse;
import de.davelee.trams.api.response.SatisfactionRateResponse;
import de.davelee.trams.api.response.TimeResponse;
import de.davelee.trams.util.DifficultyLevel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
		gameService.saveGame(CompanyRequest.builder()
				.playerName("Dave A J Lee")
				.scenarioName("Landuff Transport Company")
				.startingBalance(80000.0)
				.startingTime("20-08-2009 05:00")
				.difficultyLevel(DifficultyLevel.EASY.name())
				.build());
		Mockito.when(restTemplate.getForObject(anyString(), eq(CompanyResponse.class))).thenReturn(null);
		Assertions.assertNull(gameService.getGameByPlayerName("Mustermann GmbH", "My First Name"));
		Mockito.when(restTemplate.getForObject(anyString(), eq(CompanyResponse.class))).thenReturn(
				CompanyResponse.builder().name("Mustermann GmbH").playerName("Dave A J Lee").difficultyLevel("EASY")
						.time("24-12-2020 11:58").build()
		);
		Assertions.assertNotNull(gameService.getGameByPlayerName("Mustermann GmbH", "Dave A J Lee"));
		CompanyResponse companyResponse = gameService.getGameByPlayerName("Mustermann GmbH", "Dave A J Lee");
		assertEquals(companyResponse.getTime(), "24-12-2020 11:58");
		Mockito.when(restTemplate.patchForObject(anyString(), any(), eq(TimeResponse.class))).thenReturn(
				TimeResponse.builder()
						.company("Mustermann GmbH")
						.time("24-12-2020 12:13")
						.build()
		);
		LocalDateTime localDateTime = gameService.incrementTime("Mustermann GmbH", 15);
		assertEquals(DateTimeFormatter.ofPattern("HH:mm").format(localDateTime), "12:13");
		gameService.withdrawOrCreditBalance(-100.0, "Dave A J Lee");
		gameService.withdrawOrCreditBalance(10.0, "Dave A J Lee");
		Mockito.when(restTemplate.patchForObject(anyString(), any(), eq(SatisfactionRateResponse.class))).thenReturn(
				SatisfactionRateResponse.builder()
						.company("Mustermann GmbH")
						.satisfactionRate(91)
						.build()
		);
		assertEquals(gameService.computeAndReturnPassengerSatisfaction("Mustermann GmbH", DifficultyLevel.EASY, 4, 3, 2), 91);
	}

}
