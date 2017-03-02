package de.davelee.trams.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class FactoryServiceTest {
	
	@Autowired
	private FactoryService factoryService;
	
	@Test
	public void testCreateVehicle() {
		assertNotNull(factoryService.createVehicleObject("MyBus Single Decker", "CV58 2DD", Calendar.getInstance()));
		assertNull(factoryService.createVehicleObject("MyTrain", "123", Calendar.getInstance()));
	}
	
	@Test
	public void testCreateScenario() {
		assertNotNull(factoryService.createScenarioObject("Landuff Transport Company"));
		assertNull(factoryService.createScenarioObject("Londuff Transport Company"));
	}
	
	@Test
	public void testGetModelPos() {
		assertNotNull(factoryService.getVehicleModel(1));
		assertEquals(factoryService.getVehicleModel(1), "MyBus Double Decker");
		assertNull(factoryService.getVehicleModel(7));
	}
	
	@Test
	public void testVehicleSize() {
		assertEquals(factoryService.getNumberAvailableVehicles(), 4);
	}
	
	@Test
	public void testScenarioSize() {
		assertEquals(factoryService.getNumberAvailableScenarios(), 3);
	}
	
	@Test
	public void testScenarioNames() {
		String[] scenarioNames = factoryService.getAvailableScenarioNames();
		assertEquals(scenarioNames.length, 3);
		assertEquals(scenarioNames[0], "Landuff Transport Company");
	}
	
	@Test
	public void testScenarioDescriptions() {
		String[] scenarioDescriptions = factoryService.getAvailableScenarioCityDescriptions();
		assertEquals(scenarioDescriptions.length, 3);
	}

}
