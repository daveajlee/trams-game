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
public class MDorfScenarioTest {
	
	@Autowired
	@Qualifier("mDorfScenario")
	Scenario mDorfScenario;
	
	@Autowired
	ScenarioService scenarioService;
	
	@Test
	public void testPopulate() {
		Assertions.assertEquals(mDorfScenario.getScenarioName(), "MDorf Transport Company");
	}

}
