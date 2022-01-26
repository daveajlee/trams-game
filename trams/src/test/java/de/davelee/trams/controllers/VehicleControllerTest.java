package de.davelee.trams.controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.api.response.PurchaseVehicleResponse;
import de.davelee.trams.api.response.VehicleResponse;
import de.davelee.trams.api.response.VehiclesResponse;
import de.davelee.trams.beans.Scenario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class VehicleControllerTest {

	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	private VehicleController vehicleController;
	
	@Test
	public void testSuppliedVehicles ( ) {
		Scenario scenario = new Scenario();
		scenario.setScenarioName("Landuff Transport Company");
		HashMap<String, Integer> suppliedVehicles = new HashMap<>();
		suppliedVehicles.put("MyBus Single Decker", 1);
		scenario.setSuppliedVehicles(suppliedVehicles);
		Mockito.when(restTemplate.postForObject(anyString(), any(), eq(PurchaseVehicleResponse.class))).thenReturn(PurchaseVehicleResponse.builder()
				.purchased(true)
				.purchasePrice(10000.0)
				.build());
		Mockito.when(vehicleController.getVehicles("Mustermann GmbH")).thenReturn(VehiclesResponse.builder()
				.count(0L)
				.vehicleResponses(new VehicleResponse[0]).build());
		assertEquals(1, vehicleController.createSuppliedVehicles(scenario.getSuppliedVehicles(), "01-01-2014 10:00", "Mustermann GmbH"));
	}

	@Test
	public void testVehicleDelivered() {
		//Test data.
		//Before - not been delivered.
		Assertions.assertFalse(vehicleController.hasVehicleBeenDelivered("20-04-2014", "20-04-2013 10:00"));
		Assertions.assertFalse(vehicleController.hasVehicleBeenDelivered("20-04-2014", "20-03-2014 10:00"));
		Assertions.assertFalse(vehicleController.hasVehicleBeenDelivered("20-04-2014", "19-04-2014 10:00"));
		//Same - been delivered.
		assertTrue(vehicleController.hasVehicleBeenDelivered("20-04-2014", "20-04-2014 10:00"));
		//After - been delivered.
		assertTrue(vehicleController.hasVehicleBeenDelivered("20-04-2014", "21-04-2014 10:00"));
		assertTrue(vehicleController.hasVehicleBeenDelivered("20-04-2014", "20-05-2014 10:00"));
		assertTrue(vehicleController.hasVehicleBeenDelivered("20-04-2014", "20-04-2015 10:00"));
	}

	@Test
	public void testValue () {
		//TODO: implement value correctly and then redo test.
		assertTrue(vehicleController.getValue(null, "20-04-2014") > 0.0);
	}

	@Test
	public void testAge () {
		//Vehicle is not even bought - age is minus.
		assertEquals(vehicleController.getAge("20-04-2014", "20-02-2014"), -1);
		//Vehicle is brand new - age 0.
		assertEquals(vehicleController.getAge("20-04-2014", "20-04-2014"), 0);
		//Vehicle is 12 months old.
		assertEquals(vehicleController.getAge("20-04-2014", "20-04-2015"), 12);
		//Vehicle is very old!
		assertEquals(vehicleController.getAge("20-04-2014", "22-04-2027"), 156);
	}

	@Test
	public void testGetVehicleById ( ) {
		//Test begins here.
		Mockito.when(vehicleController.getVehicles("Mustermann GmbH")).thenReturn(VehiclesResponse.builder()
				.count(0L)
				.vehicleResponses(new VehicleResponse[0]).build());
		vehicleController.purchaseVehicle("Mercedes", "Mustermann GmbH", 2014, Optional.of(101));
		Mockito.when(restTemplate.getForObject(anyString(), eq(VehiclesResponse.class))).
				thenReturn(VehiclesResponse.builder()
						.count(1L)
						.vehicleResponses(new VehicleResponse[] { VehicleResponse.builder().company("Mustermann GmbH")
								.additionalTypeInformationMap(Map.of("Registration Number", "DDD2 HJK"))
								.deliveryDate("24-12-2020").build() })
						.build());
		assertEquals(1, vehicleController.getVehicles("Mustermann GmbH").getCount());
		assertNotNull(vehicleController.getVehicleByRegistrationNumber("DDD2 HJK", "Mustermann GmbH"));
		Assertions.assertNull(vehicleController.getVehicleByRegistrationNumber("2013-001", "Mustermann GmbH"));
	}

	@Test
	public void testCheckIfRegExists() {
		Mockito.when(restTemplate.getForObject(anyString(), eq(VehiclesResponse.class))).
				thenReturn(VehiclesResponse.builder()
						.count(1L)
						.vehicleResponses(new VehicleResponse[] { VehicleResponse.builder().company("Mustermann GmbH")
								.additionalTypeInformationMap(Map.of("Registration Number", "DDD2 HJK"))
								.deliveryDate("24-12-2020").build() })
						.build());
		assertFalse(vehicleController.checkIfRegExists("Mustermann GmbH", "DDD2 HJK"));
	}

	@Test
	public void testLoadVehicles() {
		Mockito.doNothing().when(restTemplate).delete(anyString());
		vehicleController.loadVehicles(new VehicleResponse[] {
				VehicleResponse.builder()
						.company("Mustermann GmbH")
						.modelName("BendyBus 2000")
						.fleetNumber("2003")
						.build()
		}, "Mustermann GmbH", LocalDate.of(2021, 12, 1));
	}

	@Test
	public void testVehicleSize() {
		Mockito.when(restTemplate.getForObject(anyString(), eq(VehiclesResponse.class))).
				thenReturn(VehiclesResponse.builder()
						.count(1L)
						.vehicleResponses(new VehicleResponse[] { VehicleResponse.builder().company("Mustermann GmbH")
								.additionalTypeInformationMap(Map.of("Registration Number", "DDD2 HJK"))
								.deliveryDate("24-12-2020")
								.modelName("MyBus Single Decker").build() })
						.build());
		assertEquals(vehicleController.getVehicles("Mustermann GmbH").getCount(), 1);
	}

	@Test
	public void testAssignVehicleToTour() {
		Mockito.when(restTemplate.patchForObject(anyString(), any(), eq(Void.class))).
				thenReturn(null);
		Mockito.when(restTemplate.getForObject(anyString(), eq(VehiclesResponse.class))).
				thenReturn(VehiclesResponse.builder()
						.count(1L)
						.vehicleResponses(new VehicleResponse[] { VehicleResponse.builder().company("Mustermann GmbH")
								.additionalTypeInformationMap(Map.of("Registration Number", "DDD2 HJK"))
								.deliveryDate("24-12-2020")
								.modelName("MyBus Single Decker").build() })
						.build());
		vehicleController.assignVehicleToTour("DDD2 HJK", "1/1", "Mustermann GmbH");
	}

	@Test
	public void testGetAllCreatedVehicles() {
		Mockito.when(restTemplate.getForObject(anyString(), eq(VehiclesResponse.class))).
				thenReturn(VehiclesResponse.builder()
						.count(1L)
						.vehicleResponses(new VehicleResponse[] { VehicleResponse.builder().company("Mustermann GmbH")
								.additionalTypeInformationMap(Map.of("Registration Number", "DDD2 HJK"))
								.deliveryDate("24-12-2020")
								.modelName("MyBus Single Decker").build() })
						.build());
		assertNotNull(vehicleController.getAllCreatedVehicles("Mustermann GmbH"));
		Mockito.when(restTemplate.getForObject(anyString(), eq(VehiclesResponse.class))).
				thenReturn(null);
		assertNull(vehicleController.getAllCreatedVehicles("Mustermann GmbH"));
	}

	@Test
	public void testHasSomeVehiclesBeenDelivered() {
		Mockito.when(restTemplate.getForObject(anyString(), eq(VehiclesResponse.class))).
				thenReturn(VehiclesResponse.builder()
						.count(1L)
						.vehicleResponses(new VehicleResponse[] { VehicleResponse.builder().company("Mustermann GmbH")
								.additionalTypeInformationMap(Map.of("Registration Number", "DDD2 HJK"))
								.deliveryDate("24-12-2020")
								.modelName("MyBus Single Decker").build() })
						.build());
		assertTrue(vehicleController.hasSomeVehiclesBeenDelivered("Mustermann GmbH", "23-01-2022 05:30"));
		assertFalse(vehicleController.hasSomeVehiclesBeenDelivered("Mustermann GmbH", "23-01-2020 05:30"));
		Mockito.when(restTemplate.getForObject(anyString(), eq(VehiclesResponse.class))).
				thenReturn(VehiclesResponse.builder()
						.count(0L)
						.vehicleResponses(new VehicleResponse[0])
						.build());
		assertFalse(vehicleController.hasSomeVehiclesBeenDelivered("Mustermann GmbH", "23-01-2022 05:30"));
	}

	@Test
	public void testSellVehicle() {
		Mockito.doNothing().when(restTemplate).delete(anyString());
		assertNotNull(vehicleController.sellVehicle(VehicleResponse.builder().company("Mustermann GmbH")
				.additionalTypeInformationMap(Map.of("Registration Number", "DDD2 HJK"))
				.deliveryDate("24-12-2020")
				.modelName("MyBus Single Decker").build(), "23-01-2022 05:30"));
	}

	@Test
	public void testGetVehiclesForRoute() {
		assertNotNull(vehicleController.getVehiclesForRoute("Mustermann GmbH", "1A"));
	}

	@Test
	public void testGetCurrentStopName() {
		assertNotNull(vehicleController.getCurrentStopName(VehicleResponse.builder().company("Mustermann GmbH")
				.additionalTypeInformationMap(Map.of("Registration Number", "DDD2 HJK"))
				.deliveryDate("24-12-2020")
				.modelName("MyBus Single Decker").build(), "23-01-2022 05:30", "EASY"));
	}

	@Test
	public void testGetAllocations() {
		Mockito.when(restTemplate.getForObject(anyString(), eq(VehiclesResponse.class))).
				thenReturn(VehiclesResponse.builder()
						.count(1L)
						.vehicleResponses(new VehicleResponse[] { VehicleResponse.builder().company("Mustermann GmbH")
								.additionalTypeInformationMap(Map.of("Registration Number", "DDD2 HJK"))
								.deliveryDate("24-12-2020")
								.modelName("MyBus Single Decker")
								.allocatedTour("1/1").build() })
						.build());
		assertEquals(1, vehicleController.getAllocations("Mustermann GmbH").size());
	}

	@Test
	public void testGetPreviousVehicleModel() {
		Mockito.when(restTemplate.getForObject(anyString(), eq(VehiclesResponse.class))).
				thenReturn(VehiclesResponse.builder()
						.count(2L)
						.vehicleResponses(new VehicleResponse[] { VehicleResponse.builder().company("Mustermann GmbH")
								.additionalTypeInformationMap(Map.of("Registration Number", "DDD2 HJJ"))
								.deliveryDate("24-12-2020")
								.modelName("MyBus Double Decker")
								.allocatedTour("1/1").build(), VehicleResponse.builder().company("Mustermann GmbH")
						.additionalTypeInformationMap(Map.of("Registration Number", "DDD2 HJK"))
						.deliveryDate("24-12-2020")
						.modelName("MyBus Single Decker")
						.allocatedTour("1/1").build() })
			.build());
		assertEquals("MyBus Double Decker", vehicleController.getPreviousVehicleModel("MyBus Single Decker", "Mustermann GmbH"));
		Mockito.when(restTemplate.getForObject(anyString(), eq(VehiclesResponse.class))).
				thenReturn(VehiclesResponse.builder()
						.count(0L)
						.vehicleResponses(new VehicleResponse[0])
						.build());
		assertEquals("", vehicleController.getPreviousVehicleModel("MyBus Single Decker", "Mustermann GmbH"));
	}

	@Test
	public void testGetNextVehicleModel() {
		Mockito.when(restTemplate.getForObject(anyString(), eq(VehiclesResponse.class))).
				thenReturn(VehiclesResponse.builder()
						.count(2L)
						.vehicleResponses(new VehicleResponse[] { VehicleResponse.builder().company("Mustermann GmbH")
								.additionalTypeInformationMap(Map.of("Registration Number", "DDD2 HJJ"))
								.deliveryDate("24-12-2020")
								.modelName("MyBus Double Decker")
								.allocatedTour("1/1").build(), VehicleResponse.builder().company("Mustermann GmbH")
								.additionalTypeInformationMap(Map.of("Registration Number", "DDD2 HJK"))
								.deliveryDate("24-12-2020")
								.modelName("MyBus Single Decker")
								.allocatedTour("1/1").build() })
						.build());
		assertEquals("MyBus Single Decker", vehicleController.getNextVehicleModel("MyBus Double Decker", "Mustermann GmbH"));
		Mockito.when(restTemplate.getForObject(anyString(), eq(VehiclesResponse.class))).
				thenReturn(VehiclesResponse.builder()
						.count(0L)
						.vehicleResponses(new VehicleResponse[0])
						.build());
		assertEquals("", vehicleController.getNextVehicleModel("MyBus Single Decker", "Mustermann GmbH"));
	}

}
