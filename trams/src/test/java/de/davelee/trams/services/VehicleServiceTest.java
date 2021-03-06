package de.davelee.trams.services;

import java.time.LocalDate;
import java.time.Month;
import java.util.NoSuchElementException;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.model.VehicleModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class VehicleServiceTest {
	
	@Autowired
	private VehicleService vehicleService;
	
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
		vehicleModel.setSeatingCapacity("45");
		vehicleModel.setStandingCapacity("20");
		vehicleModel.setPurchasePrice(20000.00);
		assertEquals(vehicleModel.getRegistrationNumber(), "CV58 2DX");
		Assertions.assertEquals(vehicleModel.getDeliveryDate().getYear(), 2014);
		Assertions.assertEquals(vehicleModel.getDeliveryDate().getMonth(), Month.APRIL);
		Assertions.assertEquals(vehicleModel.getDeliveryDate().getDayOfMonth(), 20);
		Assertions.assertEquals(vehicleModel.getDepreciationFactor(), 0.06, 0.0001);
		assertEquals(vehicleModel.getImagePath(), "singledecker.png");
		assertEquals(vehicleModel.getModel(), "Mercedes");
		Assertions.assertEquals(vehicleModel.getRouteScheduleNumber(), 1);
		assertEquals(vehicleModel.getSeatingCapacity(), "45");
		assertEquals(vehicleModel.getStandingCapacity(), "20");
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
		vehicleModel.setSeatingCapacity("45");
		vehicleModel.setStandingCapacity("20");
		vehicleModel.setPurchasePrice(20000.00);
		//Test begins here.
		vehicleService.deleteAllVehicles();
		vehicleService.saveVehicle(vehicleModel);
		assertEquals(1, vehicleService.getVehicleModels().length);
		Assertions.assertNotNull(vehicleService.getVehicleByRegistrationNumber("CV58 2DX"));
		assertEquals(vehicleService.getVehicleByRegistrationNumber("CV58 2DX").getImagePath(), "singledecker.png");
		Assertions.assertNull(vehicleService.getVehicleByRegistrationNumber("2013-001"));
	}

	@Test
	public void testCreateVehicleObject() {
		assertNotNull(vehicleService.createVehicleObject("MyBus Single Decker", "CV58 2DD", LocalDate.now()));
		Assertions.assertThrows(NoSuchElementException.class, () -> vehicleService.createVehicleObject("MyTrain", "123", LocalDate.now()));
	}

	@Test
	public void testGetModel() {
		Assertions.assertNotNull(vehicleService.getFirstVehicleModel());
		assertEquals(vehicleService.getFirstVehicleModel(), "MyBus Single Decker");
	}

	@Test
	public void testVehicleSize() {
		assertEquals(vehicleService.getNumberVehicleTypes(), 4);
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
