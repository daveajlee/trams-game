package de.davelee.trams.repository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.davelee.trams.data.JourneyPattern;
import de.davelee.trams.model.JourneyPatternModel;
import de.davelee.trams.services.JourneyPatternService;

@ExtendWith(SpringExtension.class)
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
		JourneyPatternModel journeyPatternModel = JourneyPatternModel.builder()
				.name("Mon-Fri")
				.daysOfOperation(daysOfOperation)
				.outgoingTerminus("S+U Pankow")
				.returnTerminus("Rathaus Pankow")
				.startTime(Calendar.getInstance())
				.endTime(Calendar.getInstance())
				.frequency(15)
				.duration(3)
				.timetableName("Mon-Fri")
				.routeNumber("155")
				.build();
		journeyPatternService.saveJourneyPattern(journeyPatternModel);
		JourneyPattern journeyPattern2 = journeyPatternRepository.findByTimetableNameAndRouteNumber("Mon-Fri", "155").get(0);
		Assertions.assertNotNull(journeyPattern2);
		Assertions.assertEquals(journeyPattern2.getName(), "Mon-Fri");
		Assertions.assertEquals(journeyPattern2.getDaysOfOperation().size(), 5);
		Assertions.assertEquals(journeyPattern2.getOutgoingTerminus(), "S+U Pankow");
		Assertions.assertEquals(journeyPattern2.getReturnTerminus(), "Rathaus Pankow");
		Assertions.assertEquals(journeyPattern2.getRouteDuration(), 3);
		Assertions.assertEquals(journeyPattern2.getFrequency(), 15);
	}

}
