package de.davelee.trams.services;

import java.time.LocalDate;
import java.time.Month;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.model.TimetableModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class TimetableServiceTest {
	
	@Test
	public void testCreate() {
		LocalDate validFromDate = LocalDate.of(2014,4,20);
		LocalDate validToDate = LocalDate.of(2014,5,20);
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
