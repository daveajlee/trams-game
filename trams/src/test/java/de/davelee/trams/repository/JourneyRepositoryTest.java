package de.davelee.trams.repository;

import java.util.Calendar;
import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import de.davelee.trams.model.JourneyModel;
import de.davelee.trams.services.JourneyService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class JourneyRepositoryTest {
	
	@Autowired
	private JourneyService journeyService;
	
	@Autowired
	private JourneyRepository journeyRepository;
	
	@Test
	public void journeyTest() {
		HashMap<String, Calendar> stops = new HashMap<String, Calendar>();
		stops.put("Rathaus Pankow", Calendar.getInstance());
		stops.put("Pankow Kirche", Calendar.getInstance());
		stops.put("S+U Pankow", Calendar.getInstance());
        journeyService.saveJourney(JourneyModel.builder()
				.journeyNumber(1)
				.routeScheduleNumber(1)
				.routeNumber("155").build());
		assertEquals(journeyRepository.findByRouteScheduleNumberAndRouteNumber(1, "155").get(0).getRouteScheduleNumber(), 1);
	}

	private void assertEquals ( final int expected, final int actual ) {
		Assertions.assertEquals(expected, actual);
	}

}
