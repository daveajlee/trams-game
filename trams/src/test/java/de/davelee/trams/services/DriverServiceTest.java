package de.davelee.trams.services;

import java.time.LocalDate;
import java.time.Month;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.api.response.UserResponse;
import de.davelee.trams.model.DriverModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.*;

@SpringBootTest(classes= TramsGameApplication.class)
public class DriverServiceTest {

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private DriverService driverService;
	
	@Test
	public void testCreateDriver() {
		Mockito.doNothing().when(restTemplate).delete(anyString());
		driverService.removeAllDrivers("Mustermann GmbH", "mmustermann-ghgkg");
		DriverModel driverModel = DriverModel.builder()
				.name("Dave Lee")
				.contractedHours(40)
				.startDate(LocalDate.of(2014,4,20)).build();
		Mockito.when(restTemplate.postForObject(anyString(), any(), eq(Void.class))).thenReturn(null);
		driverService.saveDriver(driverModel);
		Mockito.when(restTemplate.getForObject(anyString(), eq(UserResponse.class))).thenReturn(
			UserResponse.builder()
					.firstName("Dave")
					.surname("Lee")
					.contractedHoursPerWeek(40)
					.startDate("20-04-2014")
					.build()
		);
		DriverModel driverModel2 = driverService.getDriverByName("Dave Lee", "Mustermann GmbH", "mmustermann-ghgkg");
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
		Mockito.when(restTemplate.postForObject(anyString(), any(), eq(Void.class))).thenReturn(null);
		driverService.saveDriver(driverModel);
		driverService.saveDriver(driverModel2);
		driverService.saveDriver(driverModel3);
		Mockito.when(restTemplate.getForObject(anyString(), eq(UserResponse.class))).thenReturn(
				UserResponse.builder()
						.firstName("Brian")
						.surname("Lee")
						.contractedHoursPerWeek(40)
						.startDate("20-04-2014")
						.build()
		);
		Assertions.assertNotNull(driverService.getDriverByName("Brian Lee", "Mustermann GmbH", "mmustermann-ghgkg"));
		assertEquals(driverService.getDriverByName("Brian Lee", "Nustermann GmbH", "mmustermann-ghgkg").getName(), "Brian Lee");
		Mockito.when(restTemplate.getForObject(anyString(), eq(UserResponse.class))).thenReturn(null);
		Assertions.assertNull(driverService.getDriverByName("Stephan Lee", "Mustermann GmbH", "mmustermann-ghgkg"));
	}

	private void assertEquals ( final String expected, final String actual ) {
		Assertions.assertEquals(expected, actual);
	}

	private void assertTrue ( final boolean condition ) {
		Assertions.assertTrue(condition);
	}

}
