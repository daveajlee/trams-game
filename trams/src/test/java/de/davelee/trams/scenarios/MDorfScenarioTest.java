package de.davelee.trams.scenarios;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.davelee.trams.beans.Scenario;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class MDorfScenarioTest {
	
	@Autowired
	@Qualifier("mDorfScenario")
	private Scenario mDorfScenario;
	
	@Test
	public void testPopulate() {
		assertEquals(mDorfScenario.getScenarioName(), "MDorf Transport Company");
	}

	private void assertEquals ( final String expected, final String actual ) {
		Assertions.assertEquals(expected, actual);
	}

}
