package de.davelee.trams.services;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.davelee.trams.util.DateFormats;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class SimulationServiceTest {
	
	@Autowired
	private SimulationService simulationService;
	
	@Before
	public void before() {
		simulationService.createSimulator();
	}
	
	@Test
	public void testIncrement() {
		Calendar calendar = Calendar.getInstance(); calendar.set(2014,4,20,22,57);
		simulationService.getSimulator().setCurrentTime(calendar);
		assertEquals(DateFormats.HOUR_MINUTE_FORMAT.getFormat().format(calendar.getTime()), "22:57");
		simulationService.getSimulator().setTimeIncrement(15);
		simulationService.incrementTime();
		assertEquals(DateFormats.HOUR_MINUTE_FORMAT.getFormat().format(calendar.getTime()), "23:12");
	}
	
	@Test
	public void testFormat() {
		Calendar calendar = Calendar.getInstance(); calendar.set(2014,4,20,22,57);
		assertEquals(simulationService.formatDateString(calendar, DateFormats.FULL_FORMAT), "Tuesday, 20 May 2014");
	}

}
