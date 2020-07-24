package de.davelee.trams.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class JourneyServiceTest {

	@Autowired
	private JourneyService journeyService;

	@Test
	public void testStopDistances() {
		Assertions.assertEquals(journeyService.getDistance("Landuff Transport Company", "Airport", "Cargo Terminal"), 12);
	}

	@Test
	public void testStopDistanceNull1() {
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> journeyService.getDistance("Landuff Transport Company", "Strasse 201", "Cargo Terminal"));
	}

	@Test
	public void testStopDistanceNull2() {
		Assertions.assertThrows(IndexOutOfBoundsException.class, () -> journeyService.getDistance("Landuff Transport Company", "Airport", "Strasse 201"));
	}
	
}
