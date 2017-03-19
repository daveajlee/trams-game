package de.davelee.trams.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.davelee.trams.model.TimetableModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class TimetableServiceTest {
	
	@Autowired
	private TimetableService timetableService;
	
	@Autowired
	private JourneyPatternService journeyPatternService;
	
	@Test
	public void testCreate() {
		Calendar testDate = Calendar.getInstance(); Calendar testDate2 = Calendar.getInstance();
		testDate.set(2014, 4, 20, 14, 23); testDate2.set(2014,5,20);
		List<Integer> daysOfOperation = new ArrayList<Integer>(); daysOfOperation.add(Calendar.MONDAY);
		daysOfOperation.add(Calendar.TUESDAY); daysOfOperation.add(Calendar.WEDNESDAY); daysOfOperation.add(Calendar.THURSDAY);
		daysOfOperation.add(Calendar.FRIDAY);
		TimetableModel timetableModel = new TimetableModel();
		timetableModel.setName("myTimetable");
		timetableModel.setRouteNumber("155");
		timetableModel.setValidFromDate(testDate);
		timetableModel.setValidToDate(testDate2);
		assertNotNull(timetableModel);
		assertEquals(timetableModel.getName(), "myTimetable");
		assertEquals(timetableModel.getValidFromDate().get(Calendar.YEAR), 2014);
		assertEquals(timetableModel.getValidFromDate().get(Calendar.MONTH), 4);
		assertEquals(timetableModel.getValidFromDate().get(Calendar.DAY_OF_MONTH), 20);
		assertEquals(timetableModel.getValidToDate().get(Calendar.YEAR), 2014);
		assertEquals(timetableModel.getValidToDate().get(Calendar.MONTH), 5);
		assertEquals(timetableModel.getValidToDate().get(Calendar.DAY_OF_MONTH), 20);
	}

}
