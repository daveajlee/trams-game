package de.davelee.trams.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.davelee.trams.data.JourneyPattern;
import de.davelee.trams.model.JourneyPatternModel;
import de.davelee.trams.repository.JourneyPatternRepository;
import de.davelee.trams.services.JourneyPatternService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class JourneyPatternRepositoryTest {
	
	@Autowired
	private JourneyPatternService journeyPatternService;
	
	@Autowired
	private JourneyPatternRepository journeyPatternRepository;
	
	@Test
	public void journeyPatternTest() {
		List<Integer> daysOfOperation = new ArrayList<Integer>(); daysOfOperation.add(Calendar.MONDAY);
		daysOfOperation.add(Calendar.TUESDAY); daysOfOperation.add(Calendar.WEDNESDAY); daysOfOperation.add(Calendar.THURSDAY);
		daysOfOperation.add(Calendar.FRIDAY);
		JourneyPatternModel journeyPatternModel = new JourneyPatternModel();
		journeyPatternModel.setName("Mon-Fri");
		journeyPatternModel.setDaysOfOperation(daysOfOperation);
		journeyPatternModel.setOutgoingTerminus("S+U Pankow");
		journeyPatternModel.setReturnTerminus("Rathaus Pankow");
		journeyPatternModel.setStartTime(Calendar.getInstance());
		journeyPatternModel.setEndTime(Calendar.getInstance());
		journeyPatternModel.setFrequency(15);
		journeyPatternModel.setDuration(3);
		journeyPatternModel.setTimetableName("Mon-Fri");
		journeyPatternModel.setRouteNumber("155");
		journeyPatternService.saveJourneyPattern(journeyPatternModel);
		JourneyPattern journeyPattern2 = journeyPatternRepository.findByTimetableNameAndRouteNumber("Mon-Fri", "155").get(0);
		assertNotNull(journeyPattern2);
		assertEquals(journeyPattern2.getName(), "Mon-Fri");
		assertEquals(journeyPattern2.getDaysOfOperation().size(), 5);
		assertEquals(journeyPattern2.getOutgoingTerminus(), "S+U Pankow");
		assertEquals(journeyPattern2.getReturnTerminus(), "Rathaus Pankow");
		assertEquals(journeyPattern2.getRouteDuration(), 3);
		assertEquals(journeyPattern2.getFrequency(), 15);
	}

}
