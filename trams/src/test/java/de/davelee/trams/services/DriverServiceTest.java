package de.davelee.trams.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.davelee.trams.data.Driver;
import de.davelee.trams.db.DatabaseManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class DriverServiceTest {
	
	@Autowired
	private DriverService driverService;
	
	@Autowired
	private DatabaseManager databaseManager;
	
	@Test
	public void testCreateDriver() {
		Calendar startDate = Calendar.getInstance();
		startDate.set(2014, 4, 20);
		Driver driver = driverService.createDriver("Dave Lee", 40, startDate);
		assertEquals(driver.getName(), "Dave Lee");
		assertEquals(driver.getContractedHours(), 40);
		assertEquals(driver.getStartDate().get(Calendar.YEAR), 2014);
		assertEquals(driver.getStartDate().get(Calendar.MONTH), 4);
		assertEquals(driver.getStartDate().get(Calendar.DAY_OF_MONTH), 20);
	}
	
	@Test
	public void testStartedWork() {
		//Test data.
		Calendar startDate = Calendar.getInstance();
		startDate.set(2014, 4, 20);
		//Before - not started Work.
		Calendar currentDate = Calendar.getInstance(); currentDate.set(2013, 4, 20);
		assertFalse(driverService.hasStartedWork(startDate, currentDate));
		currentDate.set(2014, 3, 20);
		assertFalse(driverService.hasStartedWork(startDate, currentDate));
		currentDate.set(2014, 4, 19);
		assertFalse(driverService.hasStartedWork(startDate, currentDate));
		//Same - started Work.
		currentDate.set(2014, 4, 20);
		assertTrue(driverService.hasStartedWork(startDate, currentDate));
		//After - started Work.
		currentDate.set(2014, 4, 21);
		assertTrue(driverService.hasStartedWork(startDate, currentDate));
		currentDate.set(2014, 5, 20);
		assertTrue(driverService.hasStartedWork(startDate, currentDate));
		currentDate.set(2015, 4, 20);
		assertTrue(driverService.hasStartedWork(startDate, currentDate));
	}
	
	@Test
	public void testGetDriverById ( ) {
		Calendar startDate = Calendar.getInstance();
		startDate.set(2014, 4, 20);
		//Double needed so that test works in both Maven and JUnit.
		databaseManager.createAndStoreDriver(driverService.createDriver("Dave Lee", 40, startDate));
		databaseManager.createAndStoreDriver(driverService.createDriver("Dave Lee", 40, startDate));
		assertNotNull(databaseManager.getDriverById(2));
		assertEquals(databaseManager.getDriverById(2).getName(), "Dave Lee");
		assertNull(databaseManager.getDriverById(40));
	}

}
