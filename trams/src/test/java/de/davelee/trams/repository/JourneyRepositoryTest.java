package de.davelee.trams.repository;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.davelee.trams.model.JourneyModel;
import de.davelee.trams.repository.JourneyRepository;
import de.davelee.trams.services.JourneyService;

@RunWith(SpringJUnit4ClassRunner.class)
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
		JourneyModel journeyModel = new JourneyModel();
		journeyModel.setJourneyNumber(1);
		journeyModel.setRouteScheduleNumber(1);
		journeyModel.setRouteNumber("155");
        journeyService.saveJourney(journeyModel);
		assertEquals(journeyRepository.findByRouteScheduleNumberAndRouteNumber(1, "155").get(0).getRouteScheduleNumber(), 1);
	}

}
