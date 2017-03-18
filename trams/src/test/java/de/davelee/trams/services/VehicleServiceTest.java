package de.davelee.trams.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.davelee.trams.data.Vehicle;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class VehicleServiceTest {
	
	@Autowired
	private VehicleService vehicleService;
	
	@Test
	public void testCreateVehicle() {
		//Test data.
		Calendar deliveryDate = Calendar.getInstance();
		deliveryDate.set(2014, 4, 20);
		//Add vehicle.
		Vehicle vehicle = vehicleService.createVehicle("CV58 2DX", deliveryDate, 0.06, "singledecker.png", "Mercedes", 1, 45, 20, 20000.00);
		assertEquals(vehicle.getRegistrationNumber(), "CV58 2DX");
		assertEquals(vehicle.getDeliveryDate().get(Calendar.YEAR), 2014);
		assertEquals(vehicle.getDeliveryDate().get(Calendar.MONTH), 4);
		assertEquals(vehicle.getDeliveryDate().get(Calendar.DAY_OF_MONTH), 20);
		assertEquals(vehicle.getDepreciationFactor(), 0.06, 0.0001);
		assertEquals(vehicle.getImagePath(), "singledecker.png");
		assertEquals(vehicle.getModel(), "Mercedes");
		assertEquals(vehicle.getRouteScheduleId(), 1);
		assertEquals(vehicle.getSeatingCapacity(), 45);
		assertEquals(vehicle.getStandingCapacity(), 20);
		assertEquals(vehicle.getPurchasePrice(), 20000.00, 0.0001);
	}
	
	@Test
	public void testVehicleDelivered() {
		//Test data.
		Calendar deliveryDate = Calendar.getInstance();
		deliveryDate.set(2014, 4, 20);
		//Before - not been delivered.
		Calendar currentDate = Calendar.getInstance(); currentDate.set(2013, 4, 20);
		assertFalse(vehicleService.hasBeenDelivered(deliveryDate, currentDate));
		currentDate.set(2014, 3, 20);
		assertFalse(vehicleService.hasBeenDelivered(deliveryDate, currentDate));
		currentDate.set(2014, 4, 19);
		assertFalse(vehicleService.hasBeenDelivered(deliveryDate, currentDate));
		//Same - been delivered.
		currentDate.set(2014, 4, 20);
		assertTrue(vehicleService.hasBeenDelivered(deliveryDate, currentDate));
		//After - been delivered.
		currentDate.set(2014, 4, 21);
		assertTrue(vehicleService.hasBeenDelivered(deliveryDate, currentDate));
		currentDate.set(2014, 5, 20);
		assertTrue(vehicleService.hasBeenDelivered(deliveryDate, currentDate));
		currentDate.set(2015, 4, 20);
		assertTrue(vehicleService.hasBeenDelivered(deliveryDate, currentDate));
	}
	
	@Test
	public void testValue () {
		//Test data.
		Calendar deliveryDate = Calendar.getInstance(); deliveryDate.set(2014, 4, 20);
		double purchasePrice = 20000.00; double depreciationFactor = 0.006;
		//Vehicle is brand new - full value.
		Calendar currentDate = Calendar.getInstance(); currentDate.set(2014, 4, 20);
		assertEquals(vehicleService.getValue(purchasePrice, depreciationFactor, deliveryDate, currentDate), 20000.00, 0.01);
		//Vehicle is a bit older.
		currentDate.set(2017,5,21);
		assertEquals(vehicleService.getValue(purchasePrice, depreciationFactor, deliveryDate, currentDate), 15560.00, 0.01);
		//Vehicle is practically worthless.
		currentDate.set(2027,5,21);
		assertEquals(vehicleService.getValue(purchasePrice, depreciationFactor, deliveryDate, currentDate), 1160.00, 0.01);
	}
	
	@Test
	public void testAge () {
		//Test data.
		Calendar deliveryDate = Calendar.getInstance(); deliveryDate.set(2014, 4, 20);
		//Vehicle is not even bought - age is minus.
		Calendar currentDate = Calendar.getInstance(); currentDate.set(2014, 2, 20);
		assertEquals(vehicleService.getAge(deliveryDate, currentDate), -1);
		//Vehicle is brand new - age 0.
		currentDate.set(2014, 4, 20);
		assertEquals(vehicleService.getAge(deliveryDate, currentDate), 0);
		//Vehicle is 12 months old.
		currentDate.set(2015, 4, 20);
		assertEquals(vehicleService.getAge(deliveryDate, currentDate), 12);
		//Vehicle is very old!
		currentDate.set(2027, 4, 22);
		assertEquals(vehicleService.getAge(deliveryDate, currentDate), 156);
	}
	
	@Test
	public void testGetVehicleById ( ) {
		Calendar deliveryDate = Calendar.getInstance();
		deliveryDate.set(2014, 4, 20);
		//Add a dummy first one in case of running junits tests together instead of apart.
		if ( vehicleService.getVehicleById(1) == null )  {
			vehicleService.saveVehicle(vehicleService.createVehicle("CV58 2XD", deliveryDate, 0.06, "singledecker.png", "Mercedes", 1, 45, 20, 20000.00));
		}
		if ( vehicleService.getVehicleById(2) == null )  {
			vehicleService.saveVehicle(vehicleService.createVehicle("CV58 2XD", deliveryDate, 0.06, "singledecker.png", "Mercedes", 1, 45, 20, 20000.00));
		}
		//Test begins here.
		vehicleService.saveVehicle(vehicleService.createVehicle("CV58 2DX", deliveryDate, 0.06, "singledecker.png", "Mercedes", 1, 45, 20, 20000.00));
		assertNotNull(vehicleService.getVehicleById(3));
		assertEquals(vehicleService.getVehicleById(3).getImagePath(), "singledecker.png");
		assertNull(vehicleService.getVehicleById(20));
	}
	
	@Test
	public void testGetAllVehicles ( ) {
		assertEquals(vehicleService.getAllVehicles().size(), 3);
	}

	@Test
	public void testCreateVehicleObject() {
		assertNotNull(vehicleService.createVehicleObject("MyBus Single Decker", "CV58 2DD", Calendar.getInstance()));
		assertNull(vehicleService.createVehicleObject("MyTrain", "123", Calendar.getInstance()));
	}

	@Test
	public void testGetModelPos() {
		assertNotNull(vehicleService.getVehicleModel(1));
		assertEquals(vehicleService.getVehicleModel(1), "MyBus Double Decker");
		assertNull(vehicleService.getVehicleModel(7));
	}

	@Test
	public void testVehicleSize() {
		assertEquals(vehicleService.getNumberVehicleTypes(), 4);
	}

}
