package de.davelee.trams.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import de.davelee.trams.model.ScenarioModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.davelee.trams.beans.Scenario;

import java.util.NoSuchElementException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class ScenarioServiceTest {
	
	@Autowired
	private ScenarioService scenarioService;
	
	@Autowired
	private VehicleService vehicleService;
	
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
		assertNotNull(scenario);
		assertEquals(scenario.getStopNames().length, 47);
	}

	public void testRetrieveScenarioObject() {
		assertEquals(scenarioService.retrieveScenarioObject("Landuff Transport Company").getName(), "Landuff Transport Company");
		assertNull(scenarioService.retrieveScenarioObject("Berlin Transport Company"));
	}

	@Test(expected=NoSuchElementException.class)
	public void testCreateScenario() {
		assertNotNull(scenarioService.retrieveScenarioObject("Landuff Transport Company"));
		scenarioService.retrieveScenarioObject("Londuff Transport Company");
	}

	@Test
	public void testScenarioSize() {
		assertEquals(scenarioService.getAvailableScenarios().length, 3);
	}

	@Test
	public void testScenarioModels() {
		ScenarioModel[] scenarioModels = scenarioService.getAvailableScenarios();
		assertEquals(scenarioModels.length, 3);
		assertEquals(scenarioModels[0].getName(), "Landuff Transport Company");
	}
	
}
