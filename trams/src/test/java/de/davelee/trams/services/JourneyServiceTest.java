package de.davelee.trams.services;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.controllers.ScenarioController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class JourneyServiceTest {

	@Autowired
	private JourneyService journeyService;

	@Autowired
	private ScenarioController scenarioController;

	@Test
	public void testStopDistances() {
		assertEquals(journeyService.getDistance(scenarioController.getScenario("Landuff Transport Company").getStopDistances(), "Airport", "Cargo Terminal"), 12);
	}

	@Test
	public void testStopDistanceNull1() {
		assertThrows(IndexOutOfBoundsException.class, () -> journeyService.getDistance(scenarioController.getScenario("Landuff Transport Company").getStopDistances(), "Strasse 201", "Cargo Terminal"));
	}

	@Test
	public void testStopDistanceNull2() {
		assertThrows(IndexOutOfBoundsException.class, () -> journeyService.getDistance(scenarioController.getScenario("Landuff Transport Company").getStopDistances(), "Airport", "Strasse 201"));
	}

	private void assertEquals ( final int expected, final int actual ) {
		Assertions.assertEquals(expected, actual);
	}
	
}
