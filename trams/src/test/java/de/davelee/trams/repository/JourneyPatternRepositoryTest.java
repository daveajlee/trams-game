package de.davelee.trams.repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import de.davelee.trams.TramsGameApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.davelee.trams.data.JourneyPattern;
import de.davelee.trams.model.JourneyPatternModel;
import de.davelee.trams.services.JourneyPatternService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class JourneyPatternRepositoryTest {
	
	@Autowired
	private JourneyPatternService journeyPatternService;
	
	@Autowired
	private JourneyPatternRepository journeyPatternRepository;
	
	@Test
	public void journeyPatternTest() {
		List<DayOfWeek> daysOfOperation = new ArrayList<DayOfWeek>(); daysOfOperation.add(DayOfWeek.MONDAY);
		daysOfOperation.add(DayOfWeek.TUESDAY); daysOfOperation.add(DayOfWeek.WEDNESDAY); daysOfOperation.add(DayOfWeek.THURSDAY);
		daysOfOperation.add(DayOfWeek.FRIDAY);
		JourneyPatternModel journeyPatternModel = JourneyPatternModel.builder()
				.name("Mon-Fri")
				.daysOfOperation(daysOfOperation)
				.outgoingTerminus("S+U Pankow")
				.returnTerminus("Rathaus Pankow")
				.startTime(LocalTime.now())
				.endTime(LocalTime.now())
				.frequency(15)
				.duration(3)
				.timetableName("Mon-Fri")
				.routeNumber("155")
				.build();
		journeyPatternService.saveJourneyPattern(journeyPatternModel);
		JourneyPattern journeyPattern2 = journeyPatternRepository.findByTimetableNameAndRouteNumber("Mon-Fri", "155").get(0);
		Assertions.assertNotNull(journeyPattern2);
		assertEquals(journeyPattern2.getName(), "Mon-Fri");
		Assertions.assertEquals(journeyPattern2.getDaysOfOperation().size(), 5);
		assertEquals(journeyPattern2.getOutgoingTerminus(), "S+U Pankow");
		assertEquals(journeyPattern2.getReturnTerminus(), "Rathaus Pankow");
		Assertions.assertEquals(journeyPattern2.getRouteDuration(), 3);
		Assertions.assertEquals(journeyPattern2.getFrequency(), 15);
	}

	private void assertEquals ( final String expected, final String actual ) {
		Assertions.assertEquals(expected, actual);
	}

}
