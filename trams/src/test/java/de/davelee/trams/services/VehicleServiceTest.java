package de.davelee.trams.services;

import java.time.LocalDate;
import java.time.Month;
import java.util.Map;
import java.util.NoSuchElementException;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.api.response.VehicleResponse;
import de.davelee.trams.api.response.VehiclesResponse;
import de.davelee.trams.model.VehicleModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest(classes= TramsGameApplication.class)
public class VehicleServiceTest {

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private VehicleService vehicleService = new VehicleService();
	
	@Test
	public void testCreateVehicle() {
		//Test data.
		//Add vehicle.
		VehicleModel vehicleModel = new VehicleModel();
		vehicleModel.setRegistrationNumber("CV58 2DX");
		vehicleModel.setDeliveryDate(LocalDate.of(2014,4,20));
		vehicleModel.setDepreciationFactor(0.06);
		vehicleModel.setImagePath("singledecker.png");
		vehicleModel.setModel("Mercedes");
		vehicleModel.setRouteNumber("155");
		vehicleModel.setRouteScheduleNumber(1);
		vehicleModel.setSeatingCapacity(45);
		vehicleModel.setStandingCapacity(20);
		vehicleModel.setPurchasePrice(20000.00);
		assertEquals(vehicleModel.getRegistrationNumber(), "CV58 2DX");
		Assertions.assertEquals(vehicleModel.getDeliveryDate().getYear(), 2014);
		Assertions.assertEquals(vehicleModel.getDeliveryDate().getMonth(), Month.APRIL);
		Assertions.assertEquals(vehicleModel.getDeliveryDate().getDayOfMonth(), 20);
		Assertions.assertEquals(vehicleModel.getDepreciationFactor(), 0.06, 0.0001);
		assertEquals(vehicleModel.getImagePath(), "singledecker.png");
		assertEquals(vehicleModel.getModel(), "Mercedes");
		Assertions.assertEquals(vehicleModel.getRouteScheduleNumber(), 1);
		assertEquals(vehicleModel.getSeatingCapacity(), 45);
		assertEquals(vehicleModel.getStandingCapacity(), 20);
		Assertions.assertEquals(vehicleModel.getPurchasePrice(), 20000.00, 0.0001);
	}
	
	@Test
	public void testVehicleDelivered() {
		//Test data.
		LocalDate deliveryDate = LocalDate.of(2014,4,20);
		//Before - not been delivered.
		LocalDate currentDate = LocalDate.of(2013,4,20);
		Assertions.assertFalse(vehicleService.hasBeenDelivered(deliveryDate, currentDate));
		currentDate = LocalDate.of(2014, 3, 20);
		Assertions.assertFalse(vehicleService.hasBeenDelivered(deliveryDate, currentDate));
		currentDate = LocalDate.of(2014, 4, 19);
		Assertions.assertFalse(vehicleService.hasBeenDelivered(deliveryDate, currentDate));
		//Same - been delivered.
		currentDate = LocalDate.of(2014, 4, 20);
		assertTrue(vehicleService.hasBeenDelivered(deliveryDate, currentDate));
		//After - been delivered.
		currentDate = LocalDate.of(2014, 4, 21);
		assertTrue(vehicleService.hasBeenDelivered(deliveryDate, currentDate));
		currentDate = LocalDate.of(2014, 5, 20);
		assertTrue(vehicleService.hasBeenDelivered(deliveryDate, currentDate));
		currentDate = LocalDate.of(2015, 4, 20);
		assertTrue(vehicleService.hasBeenDelivered(deliveryDate, currentDate));
	}
	
	@Test
	public void testValue () {
		//Test data.
		LocalDate deliveryDate = LocalDate.of(2014,4,20);
		double purchasePrice = 20000.00; double depreciationFactor = 0.006;
		//Vehicle is brand new - full value.
		LocalDate currentDate = LocalDate.of(2014,4,20);
		assertEquals(vehicleService.getValue(purchasePrice, depreciationFactor, deliveryDate, currentDate), 20000.00, 0.01);
		//Vehicle is a bit older.
		currentDate = LocalDate.of(2017,5,21);
		assertEquals(vehicleService.getValue(purchasePrice, depreciationFactor, deliveryDate, currentDate), 15560.00, 0.01);
		//Vehicle is practically worthless.
		currentDate = LocalDate.of(2027,5,21);
		assertEquals(vehicleService.getValue(purchasePrice, depreciationFactor, deliveryDate, currentDate), 1160.00, 0.01);
	}
	
	@Test
	public void testAge () {
		//Test data.
		LocalDate deliveryDate = LocalDate.of(2014,4,20);
		//Vehicle is not even bought - age is minus.
		LocalDate currentDate = LocalDate.of(2014,2,20);
		assertEquals(vehicleService.getAge(deliveryDate, currentDate), -1);
		//Vehicle is brand new - age 0.
		currentDate = LocalDate.of(2014, 4, 20);
		assertEquals(vehicleService.getAge(deliveryDate, currentDate), 0);
		//Vehicle is 12 months old.
		currentDate = LocalDate.of(2015, 4, 20);
		assertEquals(vehicleService.getAge(deliveryDate, currentDate), 12);
		//Vehicle is very old!
		currentDate = LocalDate.of(2027, 4, 22);
		assertEquals(vehicleService.getAge(deliveryDate, currentDate), 156);
	}
	
	@Test
	public void testGetVehicleById ( ) {
		//Add a dummy first one in case of running junits tests together instead of apart.
		VehicleModel vehicleModel = new VehicleModel();
		vehicleModel.setRegistrationNumber("CV58 2DX");
		vehicleModel.setDeliveryDate(LocalDate.of(2014,4,20));
		vehicleModel.setDepreciationFactor(0.06);
		vehicleModel.setImagePath("singledecker.png");
		vehicleModel.setModel("Mercedes");
		vehicleModel.setRouteNumber("155");
		vehicleModel.setRouteScheduleNumber(1);
		vehicleModel.setSeatingCapacity(45);
		vehicleModel.setStandingCapacity(20);
		vehicleModel.setPurchasePrice(20000.00);
		//Test begins here.
		Mockito.doNothing().when(restTemplate).delete(anyString());
		vehicleService.deleteAllVehicles("Mustermann GmbH");
		vehicleService.saveVehicle(vehicleModel);
		Mockito.when(restTemplate.getForObject(anyString(), eq(VehiclesResponse.class))).
				thenReturn(VehiclesResponse.builder()
						.vehicleResponses(new VehicleResponse[] { VehicleResponse.builder().company("Mustermann GmbH")
								.additionalTypeInformationMap(Map.of("Registration Number", "DDD2 HJK"))
								.deliveryDate("24-12-2020").build() })
						.build());
		assertEquals(1, vehicleService.getVehicleModels("Mustermann GmbH").length);
		Assertions.assertNotNull(vehicleService.getVehicleByRegistrationNumber("DDD2 HJK", "Mustermann GmbH"));
		Assertions.assertNull(vehicleService.getVehicleByRegistrationNumber("2013-001", "Mustermann GmbH"));
	}

	@Test
	public void testCreateVehicleObject() {
		Mockito.when(restTemplate.getForObject(anyString(), eq(VehiclesResponse.class))).
				thenReturn(VehiclesResponse.builder()
						.vehicleResponses(new VehicleResponse[] { VehicleResponse.builder().company("Mustermann GmbH")
								.additionalTypeInformationMap(Map.of("Registration Number", "DDD2 HJK"))
								.deliveryDate("24-12-2020")
								.modelName("MyBus Single Decker").build() })
						.build());
		assertNotNull(vehicleService.createVehicleObject("MyBus Single Decker", "DDD2 HJK", LocalDate.now(), "Mustermann GmbH"));
		Assertions.assertThrows(NoSuchElementException.class, () -> vehicleService.createVehicleObject("MyTrain", "123", LocalDate.now(), "Mustermann GmbH"));
	}

	@Test
	public void testGetModel() {
		Mockito.when(restTemplate.getForObject(anyString(), eq(VehiclesResponse.class))).
				thenReturn(VehiclesResponse.builder()
						.vehicleResponses(new VehicleResponse[] { VehicleResponse.builder().company("Mustermann GmbH")
								.additionalTypeInformationMap(Map.of("Registration Number", "DDD2 HJK"))
								.deliveryDate("24-12-2020")
								.modelName("MyBus Single Decker").build() })
						.build());
		Assertions.assertNotNull(vehicleService.getFirstVehicleModel("Mustermann GmbH"));
		assertEquals(vehicleService.getFirstVehicleModel("Mustermann GmbH"), "MyBus Single Decker");
	}

	@Test
	public void testVehicleSize() {
		Mockito.when(restTemplate.getForObject(anyString(), eq(VehiclesResponse.class))).
				thenReturn(VehiclesResponse.builder()
						.vehicleResponses(new VehicleResponse[] { VehicleResponse.builder().company("Mustermann GmbH")
								.additionalTypeInformationMap(Map.of("Registration Number", "DDD2 HJK"))
								.deliveryDate("24-12-2020")
								.modelName("MyBus Single Decker").build() })
						.build());
		assertEquals(vehicleService.getNumberVehicleTypes("Mustermann GmbH"), 1);
	}

	private void assertEquals ( final double expected, final double actual, final double delta ) {
		Assertions.assertEquals(expected, actual, delta);
	}

	private void assertEquals ( final String expected, final String actual ) {
		Assertions.assertEquals(expected, actual);
	}

	private void assertEquals ( final int expected, final int actual ) {
		Assertions.assertEquals(expected, actual);
	}

	private void assertTrue ( final boolean condition ) {
		Assertions.assertTrue(condition);
	}

	private void assertNotNull ( final Object actual ){
		Assertions.assertNotNull(actual);
	}

}
