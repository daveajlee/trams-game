package de.davelee.trams.services;

import java.time.LocalDate;
import java.time.Month;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.model.DriverModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class DriverServiceTest {

	@Autowired
	private DriverService driverService;
	
	@Test
	public void testCreateDriver() {
		driverService.removeAllDrivers();
		DriverModel driverModel = DriverModel.builder()
				.name("Dave Lee")
				.contractedHours(40)
				.startDate(LocalDate.of(2014,4,20)).build();
		driverService.saveDriver(driverModel);
		DriverModel driverModel2 = driverService.getDriverByName("Dave Lee");
		assertEquals(driverModel2.getName(), "Dave Lee");
		Assertions.assertEquals(driverModel2.getContractedHours(), 40);
		Assertions.assertEquals(driverModel2.getStartDate().getYear(), 2014);
		Assertions.assertEquals(driverModel2.getStartDate().getMonth(), Month.APRIL);
		Assertions.assertEquals(driverModel2.getStartDate().getDayOfMonth(), 20);
	}
	
	@Test
	public void testStartedWork() {
		//Test data.
		LocalDate startDate = LocalDate.of(2014,4,20);
		//Before - not started Work.
		LocalDate currentDate = LocalDate.of(2013,4,20);
		Assertions.assertFalse(driverService.hasStartedWork(startDate, currentDate));
		currentDate = LocalDate.of(2014,3,20);
		Assertions.assertFalse(driverService.hasStartedWork(startDate, currentDate));
		currentDate = LocalDate.of(2014,4,19);
		Assertions.assertFalse(driverService.hasStartedWork(startDate, currentDate));
		//Same - started Work.
		currentDate = LocalDate.of(2014,4,20);
		assertTrue(driverService.hasStartedWork(startDate, currentDate));
		//After - started Work.
		currentDate = LocalDate.of(2014,4,21);
		assertTrue(driverService.hasStartedWork(startDate, currentDate));
		currentDate = LocalDate.of(2014,5,20);
		assertTrue(driverService.hasStartedWork(startDate, currentDate));
		currentDate = LocalDate.of(2015, 4,20);
		assertTrue(driverService.hasStartedWork(startDate, currentDate));
	}
	
	@Test
	public void testGetDriverByName ( ) {
		//Treble needed so that test works in both Maven and JUnit.
		DriverModel driverModel = DriverModel.builder()
				.name("Dave Lee")
				.contractedHours(40)
				.startDate(LocalDate.of(2014,4,20)).build();
		DriverModel driverModel2 = DriverModel.builder()
				.name("Brian Lee")
				.contractedHours(35)
				.startDate(LocalDate.of(2014,5,20)).build();
		DriverModel driverModel3 = DriverModel.builder()
				.name("Rachel Lee")
				.contractedHours(30)
				.startDate(LocalDate.of(2014,6,20)).build();
		driverService.saveDriver(driverModel);
		driverService.saveDriver(driverModel2);
		driverService.saveDriver(driverModel3);
		Assertions.assertNotNull(driverService.getDriverByName("Brian Lee"));
		assertEquals(driverService.getDriverByName("Brian Lee").getName(), "Brian Lee");
		Assertions.assertNull(driverService.getDriverByName("Stephan Lee"));
	}

	private void assertEquals ( final String expected, final String actual ) {
		Assertions.assertEquals(expected, actual);
	}

	private void assertTrue ( final boolean condition ) {
		Assertions.assertTrue(condition);
	}

}
