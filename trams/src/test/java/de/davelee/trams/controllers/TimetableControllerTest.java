package de.davelee.trams.controllers;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class TimetableControllerTest {
	
	@Autowired
	private TimetableController timetableController;
	
	@Test
	public void testDateInfo() {
		Calendar testDate = Calendar.getInstance();
		testDate.set(2014, 4, 20, 14, 23);
		assertEquals(timetableController.getDateInfo(testDate), "Tuesday, 20 May 2014");
	}

}
