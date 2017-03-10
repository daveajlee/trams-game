package de.davelee.trams.factory;

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
@ContextConfiguration("/de/davelee/trams/spring/scenario-context.xml")
public class ScenarioFactoryTest {
	
	@Autowired
	private ScenarioFactory scenarioFactory;
	
	@Test
	public void testNumberScenarios ( ) {
		assertNotNull(scenarioFactory.getAvailableScenarios());
		assertEquals(scenarioFactory.getAvailableScenarios().size(), 3);
	}
	
	@Test
	public void testLanduffScenario() {
		Scenario scenario = scenarioFactory.createScenarioByName("Landuff Transport Company");
		assertNotNull(scenario);
		assertEquals(scenario.getScenarioName(), "Landuff Transport Company");
		assertEquals(scenario.getMinimumSatisfaction(), 70);
	}

	@Test
	public void testLongtsScenario() {
		Scenario scenario = scenarioFactory.createScenarioByName("Longts Transport Company");
		assertNotNull(scenario);
		assertEquals(scenario.getScenarioName(), "Longts Transport Company");
		assertEquals(scenario.getMinimumSatisfaction(), 50);
	}
	
	@Test
	public void testMDorfScenario() {
		Scenario scenario = scenarioFactory.createScenarioByName("MDorf Transport Company");
		assertNotNull(scenario);
		assertEquals(scenario.getScenarioName(), "MDorf Transport Company");
		assertEquals(scenario.getMinimumSatisfaction(), 35);
	}
	
	@Test
	public void testNullScenario() {
		Scenario scenario = scenarioFactory.createScenarioByName("Londuff Transport Company");
		assertNull(scenario);
	}
	
}
