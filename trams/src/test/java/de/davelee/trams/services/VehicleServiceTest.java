package de.davelee.trams.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Calendar;

import de.davelee.trams.model.VehicleModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


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
		VehicleModel vehicleModel = new VehicleModel();
		vehicleModel.setRegistrationNumber("CV58 2DX");
		vehicleModel.setDeliveryDate(deliveryDate);
		vehicleModel.setDepreciationFactor(0.06);
		vehicleModel.setImagePath("singledecker.png");
		vehicleModel.setModel("Mercedes");
		vehicleModel.setRouteNumber("155");
		vehicleModel.setRouteScheduleNumber(1);
		vehicleModel.setSeatingCapacity("45");
		vehicleModel.setStandingCapacity("20");
		vehicleModel.setPurchasePrice(20000.00);
		assertEquals(vehicleModel.getRegistrationNumber(), "CV58 2DX");
		assertEquals(vehicleModel.getDeliveryDate().get(Calendar.YEAR), 2014);
		assertEquals(vehicleModel.getDeliveryDate().get(Calendar.MONTH), 4);
		assertEquals(vehicleModel.getDeliveryDate().get(Calendar.DAY_OF_MONTH), 20);
		assertEquals(vehicleModel.getDepreciationFactor(), 0.06, 0.0001);
		assertEquals(vehicleModel.getImagePath(), "singledecker.png");
		assertEquals(vehicleModel.getModel(), "Mercedes");
		assertEquals(vehicleModel.getRouteScheduleNumber(), 1);
		assertEquals(vehicleModel.getSeatingCapacity(), "45");
		assertEquals(vehicleModel.getStandingCapacity(), "20");
		assertEquals(vehicleModel.getPurchasePrice(), 20000.00, 0.0001);
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
		VehicleModel vehicleModel = new VehicleModel();
		vehicleModel.setRegistrationNumber("CV58 2DX");
		vehicleModel.setDeliveryDate(deliveryDate);
		vehicleModel.setDepreciationFactor(0.06);
		vehicleModel.setImagePath("singledecker.png");
		vehicleModel.setModel("Mercedes");
		vehicleModel.setRouteNumber("155");
		vehicleModel.setRouteScheduleNumber(1);
		vehicleModel.setSeatingCapacity("45");
		vehicleModel.setStandingCapacity("20");
		vehicleModel.setPurchasePrice(20000.00);
		//Test begins here.
		vehicleService.saveVehicle(vehicleModel);
		assertNotNull(vehicleService.getVehicleByRegistrationNumber("CV58 2DX"));
		assertEquals(vehicleService.getVehicleByRegistrationNumber("CV58 2DX").getImagePath(), "singledecker.png");
		assertNull(vehicleService.getVehicleByRegistrationNumber("2013-001"));
	}
	
	@Test
	public void testGetAllVehicles ( ) {
		assertEquals(vehicleService.getVehicleModels().length, 1);
	}

	@Test
	public void testCreateVehicleObject() {
		assertNotNull(vehicleService.createVehicleObject("MyBus Single Decker", "CV58 2DD", Calendar.getInstance()));
		assertNull(vehicleService.createVehicleObject("MyTrain", "123", Calendar.getInstance()));
	}

	@Test
	public void testGetModel() {
		assertNotNull(vehicleService.getFirstVehicleModel());
		assertEquals(vehicleService.getFirstVehicleModel(), "MyBus Single Decker");
	}

	@Test
	public void testVehicleSize() {
		assertEquals(vehicleService.getNumberVehicleTypes(), 4);
	}

}
