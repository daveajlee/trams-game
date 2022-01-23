package de.davelee.trams.controllers;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.api.response.StopResponse;
import de.davelee.trams.api.response.StopsResponse;
import de.davelee.trams.beans.Scenario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class StopControllerTest {

	@Mock
	private RestTemplate restTemplate;

	@Mock
	private ScenarioController scenarioController;

	@InjectMocks
	private StopController stopController;

	@Test
	public void testSaveStop() {
		Mockito.when(restTemplate.postForObject(anyString(), any(), eq(Void.class))).thenReturn(null);
		stopController.saveStop("Town Centre", "Mustermann GmbH");
	}

	@Test
	public void testGetAllStops() {
		Mockito.when(restTemplate.getForObject(anyString(), eq(StopsResponse.class))).thenReturn(
			StopsResponse.builder().count(1L)
					.stopResponses(new StopResponse[] {
							StopResponse.builder()
									.company("Mustermann GmbH")
									.name("Town Centre")
									.build()
					})
					.build()
		);
		assertEquals(1, stopController.getAllStops("Mustermann GmbH").length);
		Mockito.when(restTemplate.getForObject(anyString(), eq(StopsResponse.class))).thenReturn(
				null);
		assertNull(stopController.getAllStops("Mustermann GmbH"));
	}

	@Test
	public void testGetDistance() {
		Scenario landuffScenario = new Scenario();
		landuffScenario.setStopDistances(List.of("Airport:0,12", "Town Centre:12,0"));
		Mockito.when(scenarioController.getScenario("Landuff")).thenReturn(
				landuffScenario
		);
		assertEquals(12, stopController.getDistance("Landuff", "Airport", "Town Centre"));
	}
	
}
