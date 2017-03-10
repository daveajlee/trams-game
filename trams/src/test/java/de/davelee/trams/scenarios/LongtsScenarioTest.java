package de.davelee.trams.scenarios;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.davelee.trams.beans.Scenario;
import de.davelee.trams.services.ScenarioService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class LongtsScenarioTest {
	
	@Autowired
	@Qualifier("longtsScenario")
	Scenario longtsScenario;
	
	@Autowired
	ScenarioService scenarioService;
	
	@Test
	public void testName() {
		assertEquals(longtsScenario.getScenarioName(), "Longts Transport Company");
	}

}
