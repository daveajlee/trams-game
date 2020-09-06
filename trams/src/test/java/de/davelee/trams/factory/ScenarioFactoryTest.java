package de.davelee.trams.factory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import de.davelee.trams.beans.Scenario;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class ScenarioFactoryTest {
	
	@Autowired
	private ScenarioFactory scenarioFactory;
	
	@Test
	public void testNumberScenarios ( ) {
		Assertions.assertNotNull(scenarioFactory.getAvailableScenarios());
		assertEquals(scenarioFactory.getAvailableScenarios().size(), 3);
	}
	
	@Test
	public void testLanduffScenario() {
		Scenario scenario = scenarioFactory.createScenarioByName("Landuff Transport Company");
		Assertions.assertNotNull(scenario);
		assertEquals(scenario.getScenarioName(), "Landuff Transport Company");
		Assertions.assertEquals(scenario.getMinimumSatisfaction(), 70);
	}

	@Test
	public void testLongtsScenario() {
		Scenario scenario = scenarioFactory.createScenarioByName("Longts Transport Company");
		Assertions.assertNotNull(scenario);
		assertEquals(scenario.getScenarioName(), "Longts Transport Company");
		Assertions.assertEquals(scenario.getMinimumSatisfaction(), 50);
	}
	
	@Test
	public void testMDorfScenario() {
		Scenario scenario = scenarioFactory.createScenarioByName("MDorf Transport Company");
		Assertions.assertNotNull(scenario);
		assertEquals(scenario.getScenarioName(), "MDorf Transport Company");
		Assertions.assertEquals(scenario.getMinimumSatisfaction(), 35);
	}

	@Test
	public void testNullScenario() {
		assertThrows(NoSuchElementException.class, () -> scenarioFactory.createScenarioByName("Londuff Transport Company"));
	}

	private void assertEquals ( final String expected, final String actual ) {
		Assertions.assertEquals(expected, actual);
	}

	private void assertEquals ( final int expected, final int actual ) {
		Assertions.assertEquals(expected, actual);
	}

	
}
