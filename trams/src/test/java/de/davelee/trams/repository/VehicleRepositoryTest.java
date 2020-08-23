package de.davelee.trams.repository;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.davelee.trams.data.Vehicle;
import de.davelee.trams.model.VehicleModel;
import de.davelee.trams.services.VehicleService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class VehicleRepositoryTest {
	
	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private VehicleRepository vehicleRepository;
	
	@Test
	public void vehicleTest() {
		vehicleRepository.deleteAll();
		VehicleModel vehicleModel = new VehicleModel();
		vehicleModel.setRegistrationNumber("CV58 2XD");
		vehicleModel.setDeliveryDate(LocalDate.now());
		vehicleModel.setDepreciationFactor(0.006);
		vehicleModel.setImagePath("image.png");
		vehicleModel.setModel("Mercedes");
		vehicleModel.setRouteNumber("");
		vehicleModel.setRouteScheduleNumber(-1);
		vehicleModel.setSeatingCapacity("40");
		vehicleModel.setStandingCapacity("60");
		vehicleModel.setPurchasePrice(200.99);
		vehicleService.saveVehicle(vehicleModel);
		Vehicle vehicle2 = vehicleRepository.findByRegistrationNumber("CV58 2XD");
		Assertions.assertNotNull(vehicle2);
		assertEquals(vehicle2.getRegistrationNumber(), "CV58 2XD");
		Assertions.assertEquals(vehicle2.getDepreciationFactor(), 0.006, 0.01);
		assertEquals(vehicle2.getImagePath(), "image.png");
		assertEquals(vehicle2.getModel(), "Mercedes");
		Assertions.assertEquals(vehicle2.getSeatingCapacity(), 40);
		Assertions.assertEquals(vehicle2.getStandingCapacity(), 60);
		Assertions.assertEquals(vehicle2.getPurchasePrice(), 200.99, 0.01);
		List<Vehicle> vehicles = vehicleRepository.findAll();
		Assertions.assertEquals(vehicles.size(), 1);
		//Test remove
		vehicleRepository.delete(vehicle2);
		Assertions.assertEquals(vehicleRepository.findAll().size(), 0);
	}

	private void assertEquals ( final String expected, final String actual ) {
		Assertions.assertEquals(expected, actual);
	}


}
