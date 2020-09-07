package de.davelee.trams.repository;

import java.time.LocalDate;

import de.davelee.trams.TramsGameApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.davelee.trams.data.Timetable;
import de.davelee.trams.model.TimetableModel;
import de.davelee.trams.services.TimetableService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class TimetableRepositoryTest {
	
	@Autowired
	private TimetableService timetableService;
	
	@Autowired
	private TimetableRepository timetableRepository;
	
	@Test
	public void timetableTest() {
		timetableService.saveTimetable(TimetableModel.builder()
				.name("myTimetable")
				.routeNumber("155")
				.validFromDate(LocalDate.now())
				.validToDate(LocalDate.now()).build());
		Timetable timetable2 = timetableRepository.findByRouteNumberAndName("155", "myTimetable");
		Assertions.assertNotNull(timetable2);
		assertEquals(timetable2.getName(), "myTimetable");
	}

	private void assertEquals ( final String expected, final String actual ) {
		Assertions.assertEquals(expected, actual);
	}

}
