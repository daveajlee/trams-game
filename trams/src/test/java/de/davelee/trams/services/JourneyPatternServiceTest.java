package de.davelee.trams.services;

import java.time.DayOfWeek;

import java.time.LocalTime;
import java.util.List;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.model.JourneyPatternModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class JourneyPatternServiceTest {
	
	@Autowired
	private JourneyPatternService journeyPatternService;

	@Test
	public void testCreateJourneyPattern() {
		LocalTime startTime = LocalTime.of(6,0);
		LocalTime endTime = LocalTime.of(18,30);
		List<DayOfWeek> daysOfOperation = List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY);
		JourneyPatternModel journeyPatternModel = JourneyPatternModel.builder()
				.name("Test")
				.daysOfOperation(daysOfOperation)
				.outgoingTerminus("S+U Pankow")
				.returnTerminus("Rathaus Pankow")
				.startTime(startTime)
				.endTime(endTime)
				.frequency(15)
				.duration(3)
				.timetableName("Mon-Fri")
				.routeNumber("155")
				.build();
		assertEquals(journeyPatternModel.getName(), "Test");
		Assertions.assertEquals(journeyPatternModel.getDaysOfOperation().size(), 3);
		Assertions.assertEquals(journeyPatternModel.getStartTime().getHour(), 6);
		Assertions.assertEquals(journeyPatternModel.getStartTime().getMinute(), 0);
		Assertions.assertEquals(journeyPatternModel.getEndTime().getHour(), 18);
		Assertions.assertEquals(journeyPatternModel.getEndTime().getMinute(), 30);
		Assertions.assertEquals(journeyPatternModel.getFrequency(), 15);
		Assertions.assertEquals(journeyPatternModel.getDuration(), 3);
	}
	
	@Test
	public void testGetJourneyPattern ( ) {
		LocalTime startTime = LocalTime.of(5,0);
		LocalTime endTime = LocalTime.of(19,30);
		List<DayOfWeek> daysOfOperation = List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY);
		JourneyPatternModel journeyPatternModel = JourneyPatternModel.builder()
				.name("Mon-Fri")
				.daysOfOperation(daysOfOperation)
				.outgoingTerminus("S+U Pankow")
				.returnTerminus("Rathaus Pankow")
				.startTime(startTime)
				.endTime(endTime)
				.frequency(15)
				.duration(3)
				.timetableName("Mon-Fri Timetable")
				.routeNumber("155")
				.build();
		journeyPatternService.saveJourneyPattern(journeyPatternModel);
		Assertions.assertNotNull(journeyPatternRepository.findByTimetableNameAndRouteNumber("Mon-Fri Timetable", "155"));
		assertEquals(journeyPatternRepository.findByTimetableNameAndRouteNumber("Mon-Fri Timetable", "155").get(0).getName(), "Mon-Fri");
		Assertions.assertEquals(journeyPatternRepository.findByTimetableNameAndRouteNumber("Mon-Fri Timetable", "156").size(), 0);
	}

	private void assertEquals ( final String expected, final String actual ) {
		Assertions.assertEquals(expected, actual);
	}

}
