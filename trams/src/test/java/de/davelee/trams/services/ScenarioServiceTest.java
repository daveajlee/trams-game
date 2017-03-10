package de.davelee.trams.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.davelee.trams.beans.Scenario;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class ScenarioServiceTest {
	
	@Autowired
	private ScenarioService scenarioService;
	
	@Autowired
	private VehicleService vehicleService;
	
	@Test
	public void testStopNames() {
		String[] stopNames = scenarioService.getStopNames("Landuff Transport Company");
		assertEquals(scenarioService.retrieveScenarioObject("Landuff Transport Company").getScenarioName(), "Landuff Transport Company");
		assertEquals(stopNames[0], "Airport");
		assertEquals(stopNames[46], "-");
		assertEquals(stopNames.length, 47);
	}

	@Test
	public void testStopDistances() {
		Scenario scenario = scenarioService.retrieveScenarioObject("Landuff Transport Company");
		assertNotNull(scenario);
		assertEquals(scenario.getStopDistances().size(), 46);
	}

	public void testRetrieveScenarioObject() {
		assertEquals(scenarioService.retrieveScenarioObject("Landuff Transport Company").getScenarioName(), "Landuff Transport Company");
		assertNull(scenarioService.retrieveScenarioObject("Berlin Transport Company"));
	}

	@Test
	public void testCreateScenario() {
		assertNotNull(scenarioService.retrieveScenarioObject("Landuff Transport Company"));
		assertNull(scenarioService.retrieveScenarioObject("Londuff Transport Company"));
	}

	@Test
	public void testScenarioSize() {
		assertEquals(scenarioService.getNumberAvailableScenarios(), 3);
	}

	@Test
	public void testScenarioNames() {
		String[] scenarioNames = scenarioService.getAvailableScenarioNames();
		assertEquals(scenarioNames.length, 3);
		assertEquals(scenarioNames[0], "Landuff Transport Company");
	}

	@Test
	public void testScenarioDescriptions() {
		String[] scenarioDescriptions = scenarioService.getAvailableScenarioCityDescriptions();
		assertEquals(scenarioDescriptions.length, 3);
	}
	
}
