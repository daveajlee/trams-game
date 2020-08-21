package de.davelee.trams.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.davelee.trams.model.TimetableModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class TimetableServiceTest {
	
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
		Assertions.assertNotNull(timetableModel);
		assertEquals(timetableModel.getName(), "myTimetable");
		Assertions.assertEquals(timetableModel.getValidFromDate().get(Calendar.YEAR), 2014);
		Assertions.assertEquals(timetableModel.getValidFromDate().get(Calendar.MONTH), 4);
		Assertions.assertEquals(timetableModel.getValidFromDate().get(Calendar.DAY_OF_MONTH), 20);
		Assertions.assertEquals(timetableModel.getValidToDate().get(Calendar.YEAR), 2014);
		Assertions.assertEquals(timetableModel.getValidToDate().get(Calendar.MONTH), 5);
		Assertions.assertEquals(timetableModel.getValidToDate().get(Calendar.DAY_OF_MONTH), 20);
	}

	private void assertEquals ( final String expected, final String actual ) {
		Assertions.assertEquals(expected, actual);
	}

}
