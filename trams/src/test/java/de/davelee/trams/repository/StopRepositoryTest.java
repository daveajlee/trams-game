package de.davelee.trams.repository;

import de.davelee.trams.TramsGameApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.davelee.trams.data.Stop;
import de.davelee.trams.services.JourneyService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class StopRepositoryTest {
	
	@Autowired
	private JourneyService journeyService;
	
	@Autowired
	private StopRepository stopRepository;

	@Test
	public void stopTest() {
		journeyService.saveStop("Ossietzkyplatz");
		Stop stop2 = stopRepository.findByStopName("Ossietzkyplatz");
		Assertions.assertNotNull(stop2);
		assertEquals(stop2.getStopName(), "Ossietzkyplatz");
	}

	private void assertEquals ( final String expected, final String actual ) {
		Assertions.assertEquals(expected, actual);
	}
	
}
