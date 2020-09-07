package de.davelee.trams.controllers;

import java.time.LocalDate;

import de.davelee.trams.TramsGameApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class TimetableControllerTest {
	
	@Autowired
	private TimetableController timetableController;
	
	@Test
	public void testDateInfo() {
		assertEquals("Tuesday, 20 May 2014", timetableController.getDateInfo(LocalDate.of(2014,5,20)));
	}

	private void assertEquals ( final String expected, final String actual ) {
		Assertions.assertEquals(expected, actual);
	}

}
