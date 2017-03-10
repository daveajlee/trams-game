package de.davelee.trams.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.davelee.trams.dao.JourneyPatternDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.davelee.trams.data.JourneyPattern;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class JourneyPatternServiceTest {
	
	@Autowired
	private JourneyPatternService journeyPatternService;
	
	@Autowired
	private JourneyPatternDao journeyPatternDao;

	@Test
	public void testCreateJourneyPattern() {
		Calendar startDate = Calendar.getInstance(); startDate.set(2014, 4, 28);
		Calendar endDate = Calendar.getInstance(); endDate.set(2014, 4, 30);
		List<Integer> daysOfOperation = new ArrayList<Integer>(); daysOfOperation.add(Calendar.MONDAY);
		daysOfOperation.add(Calendar.TUESDAY); daysOfOperation.add(Calendar.WEDNESDAY);
		JourneyPattern journeyPattern = journeyPatternService.createJourneyPattern("Test", daysOfOperation, "S+U Pankow", "Rathaus Pankow", startDate, endDate, 15, 3, 1);
		assertEquals(journeyPattern.getName(), "Test");
		assertEquals(journeyPattern.getDaysOfOperation().size(), 3);
		assertEquals(journeyPattern.getStartTime().get(Calendar.DAY_OF_MONTH), 28);
		assertEquals(journeyPattern.getStartTime().get(Calendar.MONTH), 4);
		assertEquals(journeyPattern.getStartTime().get(Calendar.YEAR), 2014);
		assertEquals(journeyPattern.getEndTime().get(Calendar.DAY_OF_MONTH), 30);
		assertEquals(journeyPattern.getEndTime().get(Calendar.MONTH), 4);
		assertEquals(journeyPattern.getEndTime().get(Calendar.YEAR), 2014);
		assertEquals(journeyPattern.getFrequency(), 15);
		assertEquals(journeyPattern.getRouteDuration(), 3);
	}
	
	@Test
	public void testGetDriverById ( ) {
		Calendar startDate = Calendar.getInstance(); startDate.set(2014, 4, 28);
		Calendar endDate = Calendar.getInstance(); endDate.set(2014, 4, 30);
		List<Integer> daysOfOperation = new ArrayList<Integer>(); daysOfOperation.add(Calendar.MONDAY);
		daysOfOperation.add(Calendar.TUESDAY); daysOfOperation.add(Calendar.WEDNESDAY);
		journeyPatternDao.createAndStoreJourneyPattern(journeyPatternService.createJourneyPattern("Mon-Fri", daysOfOperation, "S+U Pankow", "Rathaus Pankow", startDate, endDate, 15, 3, 1));
		assertNotNull(journeyPatternDao.getJourneyPatternById(1));
		assertEquals(journeyPatternDao.getJourneyPatternById(1).getName(), "Mon-Fri");
		assertNull(journeyPatternDao.getJourneyPatternById(40));
	}

}
