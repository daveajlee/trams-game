package de.davelee.trams.services;

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

import de.davelee.trams.data.Timetable;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class TimetableServiceTest {
	
	@Autowired
	private TimetableService timetableService;
	
	@Autowired
	private JourneyPatternService journeyPatternService;
	
	@Test
	public void testDateInfo() {
		Calendar testDate = Calendar.getInstance();
		testDate.set(2014, 4, 20, 14, 23);
		assertEquals(timetableService.getDateInfo(testDate), "Tuesday, 20 May 2014");
	}
	
	@Test
	public void testCreate() {
		Calendar testDate = Calendar.getInstance(); Calendar testDate2 = Calendar.getInstance();
		testDate.set(2014, 4, 20, 14, 23); testDate2.set(2014,5,20);
		List<Integer> daysOfOperation = new ArrayList<Integer>(); daysOfOperation.add(Calendar.MONDAY);
		daysOfOperation.add(Calendar.TUESDAY); daysOfOperation.add(Calendar.WEDNESDAY); daysOfOperation.add(Calendar.THURSDAY);
		daysOfOperation.add(Calendar.FRIDAY);
		Timetable timetable = timetableService.createTimetable("myTimetable", 
				testDate, testDate2, 1);
		assertNotNull(timetable);
		assertEquals(timetable.getName(), "myTimetable");
		assertEquals(timetable.getValidFromDate().get(Calendar.YEAR), 2014);
		assertEquals(timetable.getValidFromDate().get(Calendar.MONTH), 4);
		assertEquals(timetable.getValidFromDate().get(Calendar.DAY_OF_MONTH), 20);
		assertEquals(timetable.getValidToDate().get(Calendar.YEAR), 2014);
		assertEquals(timetable.getValidToDate().get(Calendar.MONTH), 5);
		assertEquals(timetable.getValidToDate().get(Calendar.DAY_OF_MONTH), 20);
	}

}
