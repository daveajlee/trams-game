package de.davelee.trams.services;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class JourneyServiceTest {

	@Autowired
	private JourneyService journeyService;

	@Test
	public void testStopDistances() {
		assertEquals(journeyService.getDistance("Landuff Transport Company", "Airport", "Cargo Terminal"), 12);
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testStopDistanceNull1() {
		assertEquals(journeyService.getDistance("Landuff Transport Company", "Strasse 201", "Cargo Terminal"), 12);
	}

	@Test(expected=IndexOutOfBoundsException.class)
	public void testStopDistanceNull2() {
		assertEquals(journeyService.getDistance("Landuff Transport Company", "Airport", "Strasse 201"), 12);
	}
	
}
