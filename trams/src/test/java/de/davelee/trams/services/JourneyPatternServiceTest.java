package de.davelee.trams.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.davelee.trams.model.JourneyPatternModel;
import de.davelee.trams.repository.JourneyPatternRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class JourneyPatternServiceTest {
	
	@Autowired
	private JourneyPatternService journeyPatternService;
	
	@Autowired
	private JourneyPatternRepository journeyPatternRepository;

	@Test
	public void testCreateJourneyPattern() {
		Calendar startDate = Calendar.getInstance(); startDate.set(2014, 4, 28);
		Calendar endDate = Calendar.getInstance(); endDate.set(2014, 4, 30);
		List<Integer> daysOfOperation = new ArrayList<Integer>(); daysOfOperation.add(Calendar.MONDAY);
		daysOfOperation.add(Calendar.TUESDAY); daysOfOperation.add(Calendar.WEDNESDAY);
		JourneyPatternModel journeyPatternModel = new JourneyPatternModel();
		journeyPatternModel.setName("Test");
		journeyPatternModel.setDaysOfOperation(daysOfOperation);
		journeyPatternModel.setOutgoingTerminus("S+U Pankow");
		journeyPatternModel.setReturnTerminus("Rathaus Pankow");
		journeyPatternModel.setStartTime(startDate);
		journeyPatternModel.setEndTime(endDate);
		journeyPatternModel.setFrequency(15);
		journeyPatternModel.setDuration(3);
		journeyPatternModel.setTimetableName("Mon-Fri");
		journeyPatternModel.setRouteNumber("155");
		Assertions.assertEquals(journeyPatternModel.getName(), "Test");
		Assertions.assertEquals(journeyPatternModel.getDaysOfOperation().size(), 3);
		Assertions.assertEquals(journeyPatternModel.getStartTime().get(Calendar.DAY_OF_MONTH), 28);
		Assertions.assertEquals(journeyPatternModel.getStartTime().get(Calendar.MONTH), 4);
		Assertions.assertEquals(journeyPatternModel.getStartTime().get(Calendar.YEAR), 2014);
		Assertions.assertEquals(journeyPatternModel.getEndTime().get(Calendar.DAY_OF_MONTH), 30);
		Assertions.assertEquals(journeyPatternModel.getEndTime().get(Calendar.MONTH), 4);
		Assertions.assertEquals(journeyPatternModel.getEndTime().get(Calendar.YEAR), 2014);
		Assertions.assertEquals(journeyPatternModel.getFrequency(), 15);
		Assertions.assertEquals(journeyPatternModel.getDuration(), 3);
	}
	
	@Test
	public void testGetJourneyPattern ( ) {
		Calendar startDate = Calendar.getInstance(); startDate.set(2014, 4, 28);
		Calendar endDate = Calendar.getInstance(); endDate.set(2014, 4, 30);
		List<Integer> daysOfOperation = new ArrayList<Integer>(); daysOfOperation.add(Calendar.MONDAY);
		daysOfOperation.add(Calendar.TUESDAY); daysOfOperation.add(Calendar.WEDNESDAY);
		JourneyPatternModel journeyPatternModel = new JourneyPatternModel();
		journeyPatternModel.setName("Mon-Fri");
		journeyPatternModel.setDaysOfOperation(daysOfOperation);
		journeyPatternModel.setOutgoingTerminus("S+U Pankow");
		journeyPatternModel.setReturnTerminus("Rathaus Pankow");
		journeyPatternModel.setStartTime(startDate);
		journeyPatternModel.setEndTime(endDate);
		journeyPatternModel.setFrequency(15);
		journeyPatternModel.setDuration(3);
		journeyPatternModel.setTimetableName("Mon-Fri Timetable");
		journeyPatternModel.setRouteNumber("155");
		journeyPatternService.saveJourneyPattern(journeyPatternModel);
		Assertions.assertNotNull(journeyPatternRepository.findByTimetableNameAndRouteNumber("Mon-Fri Timetable", "155"));
		Assertions.assertEquals(journeyPatternRepository.findByTimetableNameAndRouteNumber("Mon-Fri Timetable", "155").get(0).getName(), "Mon-Fri");
		Assertions.assertEquals(journeyPatternRepository.findByTimetableNameAndRouteNumber("Mon-Fri Timetable", "156").size(), 0);
	}

}
