package de.davelee.trams.services;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import de.davelee.trams.data.StopTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.davelee.trams.data.Stop;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class JourneyServiceTest {

	@Autowired
	private JourneyService journeyService;
	
	@Test
	public void testCreate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, 10); calendar.set(Calendar.MINUTE, 57);
		calendar.set(Calendar.AM_PM, Calendar.PM);
		Stop stop = journeyService.createStop("Rathaus Pankow");
		StopTime stopTime = journeyService.createStopTime(stop.getId(), 1, calendar);
		assertEquals(stop.getStopName(), "Rathaus Pankow");
		assertEquals(stopTime.getTime().get(Calendar.HOUR_OF_DAY), 22);
		assertEquals(stopTime.getTime().get(Calendar.MINUTE), 57);
	}

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
