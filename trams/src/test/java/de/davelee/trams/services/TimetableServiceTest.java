package de.davelee.trams.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
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
		LocalDate validFromDate = LocalDate.of(2014,4,20);
		LocalDate validToDate = LocalDate.of(2014,5,20);
		List<DayOfWeek> daysOfOperation = List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY);
		TimetableModel timetableModel = TimetableModel.builder()
				.name("myTimetable")
				.routeNumber("155")
				.validFromDate(validFromDate)
				.validToDate(validToDate)
				.build();
		Assertions.assertNotNull(timetableModel);
		assertEquals(timetableModel.getName(), "myTimetable");
		Assertions.assertEquals(timetableModel.getValidFromDate().getYear(), 2014);
		Assertions.assertEquals(timetableModel.getValidFromDate().getMonth(), Month.APRIL);
		Assertions.assertEquals(timetableModel.getValidFromDate().getDayOfMonth(), 20);
		Assertions.assertEquals(timetableModel.getValidToDate().getYear(), 2014);
		Assertions.assertEquals(timetableModel.getValidToDate().getMonth(), Month.MAY);
		Assertions.assertEquals(timetableModel.getValidToDate().getDayOfMonth(), 20);
	}

	private void assertEquals ( final String expected, final String actual ) {
		Assertions.assertEquals(expected, actual);
	}

}
