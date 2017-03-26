package de.davelee.trams.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.davelee.trams.data.Stop;
import de.davelee.trams.repository.StopRepository;
import de.davelee.trams.services.JourneyService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class StopRepositoryTest {
	
	@Autowired
	private JourneyService journeyService;
	
	@Autowired
	private StopRepository stopRepository;

	@Test
	public void stopTest() {
		journeyService.saveStop("Ossietzkyplatz");
		Stop stop2 = stopRepository.findByStopName("Ossietzkyplatz");
		assertNotNull(stop2);
		assertEquals(stop2.getStopName(), "Ossietzkyplatz");
	}
	
}
