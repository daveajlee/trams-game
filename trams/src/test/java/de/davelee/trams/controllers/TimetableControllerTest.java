package de.davelee.trams.controllers;

import java.util.Calendar;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class TimetableControllerTest {
	
	@Autowired
	private TimetableController timetableController;
	
	@Test
	public void testDateInfo() {
		Calendar testDate = Calendar.getInstance();
		testDate.set(2014, 4, 20, 14, 23);
		assertEquals("Tuesday, 20 May 2014", timetableController.getDateInfo(testDate));
	}

	private void assertEquals ( final String expected, final String actual ) {
		Assertions.assertEquals(expected, actual);
	}

}
