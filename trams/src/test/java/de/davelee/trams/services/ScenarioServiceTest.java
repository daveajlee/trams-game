package de.davelee.trams.services;

import de.davelee.trams.model.ScenarioModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.NoSuchElementException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class ScenarioServiceTest {
	
	@Autowired
	private ScenarioService scenarioService;
	
	@Test
	public void testStopNames() {
		ScenarioModel scenario = scenarioService.retrieveScenarioObject("Landuff Transport Company");
		Assertions.assertEquals(scenario.getName(), "Landuff Transport Company");
		Assertions.assertEquals(scenario.getStopNames()[0], "Airport");
		Assertions.assertEquals(scenario.getStopNames()[45], "Mile Inn");
		Assertions.assertEquals(scenario.getStopNames().length, 47);
	}

	@Test
	public void testStopDistances() {
		ScenarioModel scenario = scenarioService.retrieveScenarioObject("Landuff Transport Company");
		Assertions.assertNotNull(scenario);
		Assertions.assertEquals(scenario.getStopNames().length, 47);
	}

	public void testRetrieveScenarioObject() {
		Assertions.assertEquals(scenarioService.retrieveScenarioObject("Landuff Transport Company").getName(), "Landuff Transport Company");
		Assertions.assertNull(scenarioService.retrieveScenarioObject("Berlin Transport Company"));
	}

	@Test
	public void testCreateScenario() {
		Assertions.assertNotNull(scenarioService.retrieveScenarioObject("Landuff Transport Company"));
		Assertions.assertThrows(NoSuchElementException.class, () -> scenarioService.retrieveScenarioObject("Londuff Transport Company"));
	}

	@Test
	public void testScenarioSize() {
		Assertions.assertEquals(scenarioService.getAvailableScenarios().length, 3);
	}

	@Test
	public void testScenarioModels() {
		ScenarioModel[] scenarioModels = scenarioService.getAvailableScenarios();
		Assertions.assertEquals(scenarioModels.length, 3);
		Assertions.assertEquals(scenarioModels[0].getName(), "Landuff Transport Company");
	}
	
}
