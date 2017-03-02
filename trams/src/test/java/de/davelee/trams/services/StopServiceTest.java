package de.davelee.trams.services;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.davelee.trams.data.Stop;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class StopServiceTest {

	@Autowired
	private StopService stopService;
	
	@Test
	public void testDisplayStopTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, 10); calendar.set(Calendar.MINUTE, 57);
		calendar.set(Calendar.AM_PM, Calendar.PM);
		assertEquals(stopService.getDisplayStopTime(calendar), "22:57");
	}
	
	@Test
	public void testCreate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, 10); calendar.set(Calendar.MINUTE, 57);
		calendar.set(Calendar.AM_PM, Calendar.PM);
		Stop stop = stopService.createStop("Rathaus Pankow", calendar);
		assertEquals(stop.getStopName(), "Rathaus Pankow");
		assertEquals(stop.getStopTime().get(Calendar.HOUR_OF_DAY), 22);
		assertEquals(stop.getStopTime().get(Calendar.MINUTE), 57);
	}
	
}
