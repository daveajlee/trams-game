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

import de.davelee.trams.data.Timetable;
import de.davelee.trams.model.TimetableModel;
import de.davelee.trams.repository.TimetableRepository;
import de.davelee.trams.services.TimetableService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class TimetableRepositoryTest {
	
	@Autowired
	private TimetableService timetableService;
	
	@Autowired
	private TimetableRepository timetableRepository;
	
	@Test
	public void timetableTest() {
		List<Integer> daysOfOperation = new ArrayList<Integer>(); daysOfOperation.add(Calendar.MONDAY);
		daysOfOperation.add(Calendar.TUESDAY); daysOfOperation.add(Calendar.WEDNESDAY); daysOfOperation.add(Calendar.THURSDAY);
		daysOfOperation.add(Calendar.FRIDAY);
		TimetableModel timetableModel = new TimetableModel();
		timetableModel.setName("myTimetable");
		timetableModel.setRouteNumber("155");
		timetableModel.setValidFromDate(Calendar.getInstance());
		timetableModel.setValidToDate(Calendar.getInstance());
		timetableService.saveTimetable(timetableModel);
		Timetable timetable2 = timetableRepository.findByRouteNumberAndName("155", "myTimetable");
		assertNotNull(timetable2);
		assertEquals(timetable2.getName(), "myTimetable");
	}

}
