package de.davelee.trams.scenarios;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.davelee.trams.beans.Scenario;
import de.davelee.trams.services.ScenarioService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class LongtsScenarioTest {
	
	@Autowired
	@Qualifier("longtsScenario")
	private Scenario longtsScenario;
	
	@Autowired
	private ScenarioService scenarioService;
	
	@Test
	public void testName() {
		Assertions.assertEquals(longtsScenario.getScenarioName(), "Longts Transport Company");
	}

}
