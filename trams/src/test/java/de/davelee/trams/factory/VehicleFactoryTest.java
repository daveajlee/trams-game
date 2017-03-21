package de.davelee.trams.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.davelee.trams.data.Vehicle;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class VehicleFactoryTest {

	@Autowired
	private VehicleFactory vehicleFactory;
	
	@Test
	public void testNumberVehicles() {
		assertNotNull(vehicleFactory.getAvailableVehicles());
		assertEquals(vehicleFactory.getAvailableVehicles().size(), 4);
	}
	
	@Test
	public void testSingleDecker() {
		Vehicle vehicle = vehicleFactory.createVehicleByModel("MyBus Single Decker");
		assertNotNull(vehicle);
		assertEquals(vehicle.getSeatingCapacity(), 44);
		assertEquals(vehicle.getStandingCapacity(), 36);
	}
	
	@Test
	public void testDoubleDecker() {
		Vehicle vehicle = vehicleFactory.createVehicleByModel("MyBus Double Decker");
		assertNotNull(vehicle);
		assertEquals(vehicle.getSeatingCapacity(), 78);
		assertEquals(vehicle.getStandingCapacity(), 25);
	}
	
	@Test
	public void testBendy() {
		Vehicle vehicle = vehicleFactory.createVehicleByModel("MyBus Bendy");
		assertNotNull(vehicle);
		assertEquals(vehicle.getSeatingCapacity(), 48);
		assertEquals(vehicle.getStandingCapacity(), 97);
	}
	
	@Test
	public void testTram() {
		Vehicle vehicle = vehicleFactory.createVehicleByModel("MyTram Tram1");
		assertNotNull(vehicle);
		assertEquals(vehicle.getSeatingCapacity(), 104);
		assertEquals(vehicle.getStandingCapacity(), 83);
	}
	
	@Test
	public void testNull() {
		Vehicle vehicle = vehicleFactory.createVehicleByModel("MyTrain");
		assertNull(vehicle);
	}
	
}
