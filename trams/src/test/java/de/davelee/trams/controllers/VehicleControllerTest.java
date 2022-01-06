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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
		HashMap<String, Integer> suppliedVehicles = new HashMap<String, Integer>();
		suppliedVehicles.put("MyBus Single Decker", Integer.valueOf(1));
		scenario.setSuppliedVehicles(suppliedVehicles);
		Mockito.when(restTemplate.postForObject(anyString(), any(), eq(PurchaseVehicleResponse.class))).thenReturn(PurchaseVehicleResponse.builder()
				.purchased(true)
				.purchasePrice(10000.0)
				.build());
		Mockito.when(vehicleController.getVehicles("Mustermann GmbH")).thenReturn(VehiclesResponse.builder()
				.count(0L)
				.vehicleResponses(new VehicleResponse[0]).build());
		assertEquals(1, vehicleController.createSuppliedVehicles(scenario.getSuppliedVehicles(), "01-01-2014", "Mustermann GmbH"));
	}

	@Test
	public void testVehicleDelivered() {
		//Test data.
		//Before - not been delivered.
		Assertions.assertFalse(vehicleController.hasVehicleBeenDelivered("20-04-2014", "20-04-2013"));
		Assertions.assertFalse(vehicleController.hasVehicleBeenDelivered("20-04-2014", "20-03-2014"));
		Assertions.assertFalse(vehicleController.hasVehicleBeenDelivered("20-04-2014", "19-04-2014"));
		//Same - been delivered.
		assertTrue(vehicleController.hasVehicleBeenDelivered("20-04-2014", "20-04-2014"));
		//After - been delivered.
		assertTrue(vehicleController.hasVehicleBeenDelivered("20-04-2014", "21-04-2014"));
		assertTrue(vehicleController.hasVehicleBeenDelivered("20-04-2014", "20-05-2014"));
		assertTrue(vehicleController.hasVehicleBeenDelivered("20-04-2014", "20-04-2015"));
	}

	@Test
	public void testValue () {
		//TODO: implement value correctly and then redo test.
		assertEquals(0.0, vehicleController.getValue(null, "20-04-2014"));
		/*//Test data.
		LocalDate deliveryDate = LocalDate.of(2014,4,20);
		double purchasePrice = 20000.00; double depreciationFactor = 0.006;
		//Vehicle is brand new - full value.
		LocalDate currentDate = LocalDate.of(2014,4,20);
		assertEquals(vehicleController.getValue(purchasePrice, depreciationFactor, deliveryDate, currentDate), 20000.00, 0.01);
		//Vehicle is a bit older.
		currentDate = LocalDate.of(2017,5,21);
		assertEquals(vehicleController.getValue(purchasePrice, depreciationFactor, deliveryDate, currentDate), 15560.00, 0.01);
		//Vehicle is practically worthless.
		currentDate = LocalDate.of(2027,5,21);
		assertEquals(vehicleController.getValue(purchasePrice, depreciationFactor, deliveryDate, currentDate), 1160.00, 0.01);*/
	}

	@Test
	public void testAge () {
		//Test data.
		LocalDate deliveryDate = LocalDate.of(2014,4,20);
		//Vehicle is not even bought - age is minus.
		LocalDate currentDate = LocalDate.of(2014,2,20);
		assertEquals(vehicleController.getAge("20-04-2014", "20-02-2014"), -1);
		//Vehicle is brand new - age 0.
		currentDate = LocalDate.of(2014, 4, 20);
		assertEquals(vehicleController.getAge("20-04-2014", "20-04-2014"), 0);
		//Vehicle is 12 months old.
		currentDate = LocalDate.of(2015, 4, 20);
		assertEquals(vehicleController.getAge("20-04-2014", "20-04-2015"), 12);
		//Vehicle is very old!
		currentDate = LocalDate.of(2027, 4, 22);
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
		Assertions.assertNotNull(vehicleController.getVehicleByRegistrationNumber("DDD2 HJK", "Mustermann GmbH"));
		Assertions.assertNull(vehicleController.getVehicleByRegistrationNumber("2013-001", "Mustermann GmbH"));
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

}
