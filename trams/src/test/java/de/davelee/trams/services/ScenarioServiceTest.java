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
		assertEquals(scenario.getName(), "Landuff Transport Company");
		assertEquals(scenario.getStopNames()[0], "Airport");
		assertEquals(scenario.getStopNames()[45], "Mile Inn");
		assertEquals(scenario.getStopNames().length, 47);
	}

	@Test
	public void testStopDistances() {
		ScenarioModel scenario = scenarioService.retrieveScenarioObject("Landuff Transport Company");
		Assertions.assertNotNull(scenario);
		assertEquals(scenario.getStopNames().length, 47);
	}

	public void testRetrieveScenarioObject() {
		Assertions.assertEquals(scenarioService.retrieveScenarioObject("Landuff Transport Company").getName(), "Landuff Transport Company");
		Assertions.assertNull(scenarioService.retrieveScenarioObject("Berlin Transport Company"));
	}

	@Test
	public void testCreateScenario() {
		assertNotNull(scenarioService.retrieveScenarioObject("Landuff Transport Company"));
		Assertions.assertThrows(NoSuchElementException.class, () -> scenarioService.retrieveScenarioObject("Londuff Transport Company"));
	}

	@Test
	public void testScenarioSize() {
		assertEquals(scenarioService.getAvailableScenarios().length, 3);
	}

	@Test
	public void testScenarioModels() {
		ScenarioModel[] scenarioModels = scenarioService.getAvailableScenarios();
		Assertions.assertEquals(scenarioModels.length, 3);
		assertEquals(scenarioModels[0].getName(), "Landuff Transport Company");
	}

	private void assertEquals ( final String expected, final String actual ) {
		Assertions.assertEquals(expected, actual);
	}

	private void assertEquals ( final int expected, final int actual ) {
		Assertions.assertEquals(expected, actual);
	}

	private void assertNotNull ( final Object actual ){
		Assertions.assertNotNull(actual);
	}

}
