package de.davelee.trams.services;

import java.time.LocalDate;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.api.request.UserRequest;
import de.davelee.trams.api.response.UserResponse;
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
		UserRequest userRequest = UserRequest.builder()
				.firstName("Dave")
				.surname("Lee")
				.startDate("20-04-2014").build();
		Mockito.when(restTemplate.postForObject(anyString(), any(), eq(Void.class))).thenReturn(null);
		driverService.saveDriver(userRequest);
		Mockito.when(restTemplate.getForObject(anyString(), eq(UserResponse.class))).thenReturn(
			UserResponse.builder()
					.firstName("Dave")
					.surname("Lee")
					.contractedHoursPerWeek(40)
					.startDate("20-04-2014")
					.build()
		);
		UserResponse userResponse = driverService.getDriverByName("Dave Lee", "Mustermann GmbH", "mmustermann-ghgkg");
		assertEquals(userResponse.getFirstName(), "Dave");
		Assertions.assertEquals(userResponse.getContractedHoursPerWeek(), 40);
		Assertions.assertEquals(userResponse.getStartDate(), "20-04-2014");
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
		UserRequest userRequest = UserRequest.builder()
				.firstName("Dave")
				.surname("Lee")
				.startDate("20-04-2014").build();
		UserRequest userRequest1 = UserRequest.builder()
				.firstName("Brian")
				.surname("Lee")
				.startDate("20-05-2014").build();
		UserRequest userRequest2 = UserRequest.builder()
				.firstName("Rachel")
				.surname("Lee")
				.startDate("20-06-2014").build();
		Mockito.when(restTemplate.postForObject(anyString(), any(), eq(Void.class))).thenReturn(null);
		driverService.saveDriver(userRequest);
		driverService.saveDriver(userRequest1);
		driverService.saveDriver(userRequest2);
		Mockito.when(restTemplate.getForObject(anyString(), eq(UserResponse.class))).thenReturn(
				UserResponse.builder()
						.firstName("Brian")
						.surname("Lee")
						.contractedHoursPerWeek(40)
						.startDate("20-04-2014")
						.build()
		);
		Assertions.assertNotNull(driverService.getDriverByName("Brian Lee", "Mustermann GmbH", "mmustermann-ghgkg"));
		assertEquals(driverService.getDriverByName("Brian Lee", "Nustermann GmbH", "mmustermann-ghgkg").getFirstName(), "Brian");
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
