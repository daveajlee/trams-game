package de.davelee.trams.factory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.davelee.trams.data.Vehicle;

import java.util.NoSuchElementException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class VehicleFactoryTest {

	@Autowired
	private VehicleFactory vehicleFactory;
	
	@Test
	public void testNumberVehicles() {
		Assertions.assertNotNull(vehicleFactory.getAvailableVehicles());
		Assertions.assertEquals(vehicleFactory.getAvailableVehicles().size(), 4);
	}
	
	@Test
	public void testSingleDecker() {
		Vehicle vehicle = vehicleFactory.createVehicleByModel("MyBus Single Decker");
		Assertions.assertNotNull(vehicle);
		Assertions.assertEquals(vehicle.getSeatingCapacity(), 44);
		Assertions.assertEquals(vehicle.getStandingCapacity(), 36);
	}
	
	@Test
	public void testDoubleDecker() {
		Vehicle vehicle = vehicleFactory.createVehicleByModel("MyBus Double Decker");
		Assertions.assertNotNull(vehicle);
		Assertions.assertEquals(vehicle.getSeatingCapacity(), 78);
		Assertions.assertEquals(vehicle.getStandingCapacity(), 25);
	}
	
	@Test
	public void testBendy() {
		Vehicle vehicle = vehicleFactory.createVehicleByModel("MyBus Bendy");
		Assertions.assertNotNull(vehicle);
		Assertions.assertEquals(vehicle.getSeatingCapacity(), 48);
		Assertions.assertEquals(vehicle.getStandingCapacity(), 97);
	}
	
	@Test
	public void testTram() {
		Vehicle vehicle = vehicleFactory.createVehicleByModel("MyTram Tram1");
		Assertions.assertNotNull(vehicle);
		Assertions.assertEquals(vehicle.getSeatingCapacity(), 104);
		Assertions.assertEquals(vehicle.getStandingCapacity(), 83);
	}

	@Test
	public void testNull() {
		Assertions.assertThrows(NoSuchElementException.class, () -> vehicleFactory.createVehicleByModel("MyTrain"));
	}
	
}
