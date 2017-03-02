package de.davelee.trams.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.davelee.trams.data.Scenario;
import de.davelee.trams.data.Vehicle;
import de.davelee.trams.db.DatabaseManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class ScenarioServiceTest {
	
	@Autowired
	private ScenarioService scenarioService;
	
	@Autowired
	private FactoryService factoryService;
	
	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private DatabaseManager databaseManager;
	
	@Test
	public void testStopNames() {
		databaseManager.createAndStoreScenario(factoryService.createScenarioObject("Landuff Transport Company"));
		String[] stopNames = scenarioService.getStopNames(1);
		assertEquals(scenarioService.getScenarioById(1).getScenarioName(), "Landuff Transport Company");
		assertEquals(stopNames[0], "Airport");
		assertEquals(stopNames[46], "-");
		assertEquals(stopNames.length, 47);
	}

	@Test
	public void testStopDistances() {
		Scenario scenario = scenarioService.getScenarioById(1);
		assertNotNull(scenario);
		assertEquals(scenario.getStopDistances().size(), 46);
	}

	@Test
	public void testCreateSuppliedVehicles() {
		Calendar currentDate = Calendar.getInstance(); currentDate.set(Calendar.YEAR, 2014);
		List<Vehicle> vehicles = scenarioService.createSuppliedVehicles(scenarioService.getScenarioById(1).getSuppliedVehicles(), 
				currentDate, vehicleService, factoryService);
		assertEquals(vehicles.size(), 4);
	}
	
	@Test
	public void testScenarioById() {
		assertEquals(scenarioService.getScenarioById(1).getScenarioName(), "Landuff Transport Company");
		assertNull(scenarioService.getScenarioById(40));
	}
	
}
