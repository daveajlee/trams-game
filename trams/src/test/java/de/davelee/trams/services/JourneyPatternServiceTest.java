package de.davelee.trams.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.davelee.trams.model.JourneyPatternModel;
import de.davelee.trams.repository.JourneyPatternRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
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
		assertEquals(journeyPatternModel.getName(), "Test");
		assertEquals(journeyPatternModel.getDaysOfOperation().size(), 3);
		assertEquals(journeyPatternModel.getStartTime().get(Calendar.DAY_OF_MONTH), 28);
		assertEquals(journeyPatternModel.getStartTime().get(Calendar.MONTH), 4);
		assertEquals(journeyPatternModel.getStartTime().get(Calendar.YEAR), 2014);
		assertEquals(journeyPatternModel.getEndTime().get(Calendar.DAY_OF_MONTH), 30);
		assertEquals(journeyPatternModel.getEndTime().get(Calendar.MONTH), 4);
		assertEquals(journeyPatternModel.getEndTime().get(Calendar.YEAR), 2014);
		assertEquals(journeyPatternModel.getFrequency(), 15);
		assertEquals(journeyPatternModel.getDuration(), 3);
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
		assertNotNull(journeyPatternRepository.findByTimetableNameAndRouteNumber("Mon-Fri Timetable", "155"));
		assertEquals(journeyPatternRepository.findByTimetableNameAndRouteNumber("Mon-Fri Timetable", "155").get(0).getName(), "Mon-Fri");
		assertEquals(journeyPatternRepository.findByTimetableNameAndRouteNumber("Mon-Fri Timetable", "156").size(), 0);
	}

}
