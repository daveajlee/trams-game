package de.davelee.trams.services;

import java.util.Calendar;

import de.davelee.trams.model.DriverModel;
import de.davelee.trams.repository.DriverRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class DriverServiceTest {
	
	@Autowired
	private DriverService driverService;
	
	@Autowired
	private DriverRepository driverRepository;
	
	@Test
	public void testCreateDriver() {
		Calendar startDate = Calendar.getInstance();
		startDate.set(2014, Calendar.APRIL, 20);
		DriverModel driverModel = new DriverModel();
		driverModel.setName("Dave Lee");
		driverModel.setContractedHours(40);
		driverModel.setStartDate(startDate);
		driverService.saveDriver(driverModel);
		DriverModel driverModel2 = driverService.getDriverByName("Dave Lee");
		Assertions.assertEquals(driverModel2.getName(), "Dave Lee");
		Assertions.assertEquals(driverModel2.getContractedHours(), 40);
		Assertions.assertEquals(driverModel2.getStartDate().get(Calendar.YEAR), 2014);
		Assertions.assertEquals(driverModel2.getStartDate().get(Calendar.MONTH), Calendar.APRIL);
		Assertions.assertEquals(driverModel2.getStartDate().get(Calendar.DAY_OF_MONTH), 20);
	}
	
	@Test
	public void testStartedWork() {
		//Test data.
		Calendar startDate = Calendar.getInstance();
		startDate.set(2014, Calendar.APRIL, 20);
		//Before - not started Work.
		Calendar currentDate = Calendar.getInstance(); currentDate.set(2013, Calendar.APRIL, 20);
		Assertions.assertFalse(driverService.hasStartedWork(startDate, currentDate));
		currentDate.set(2014, Calendar.MARCH, 20);
		Assertions.assertFalse(driverService.hasStartedWork(startDate, currentDate));
		currentDate.set(2014, Calendar.APRIL, 19);
		Assertions.assertFalse(driverService.hasStartedWork(startDate, currentDate));
		//Same - started Work.
		currentDate.set(2014, Calendar.APRIL, 20);
		Assertions.assertTrue(driverService.hasStartedWork(startDate, currentDate));
		//After - started Work.
		currentDate.set(2014, Calendar.APRIL, 21);
		Assertions.assertTrue(driverService.hasStartedWork(startDate, currentDate));
		currentDate.set(2014, Calendar.MAY, 20);
		Assertions.assertTrue(driverService.hasStartedWork(startDate, currentDate));
		currentDate.set(2015, Calendar.APRIL, 20);
		Assertions.assertTrue(driverService.hasStartedWork(startDate, currentDate));
	}
	
	@Test
	public void testGetDriverByName ( ) {
		Calendar startDate = Calendar.getInstance();
		startDate.set(2014, Calendar.APRIL, 20);
		//Treble needed so that test works in both Maven and JUnit.
		DriverModel driverModel = new DriverModel(); DriverModel driverModel2 = new DriverModel(); DriverModel driverModel3 = new DriverModel();
		driverModel.setName("Dave Lee"); driverModel2.setName("Brian Lee"); driverModel.setName("Rachel Lee");
		driverModel.setContractedHours(40); driverModel2.setContractedHours(35); driverModel3.setContractedHours(30);
		driverModel.setStartDate(startDate); driverModel2.setStartDate(startDate); driverModel3.setStartDate(startDate);
		driverService.saveDriver(driverModel);
		driverService.saveDriver(driverModel2);
		driverService.saveDriver(driverModel3);
		Assertions.assertNotNull(driverService.getDriverByName("Brian Lee"));
		Assertions.assertEquals(driverService.getDriverByName("Brian Lee").getName(), "Brian Lee");
		Assertions.assertNull(driverService.getDriverByName("Stephan Lee"));
	}

}
