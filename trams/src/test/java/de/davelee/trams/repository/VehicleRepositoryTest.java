package de.davelee.trams.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.davelee.trams.data.Vehicle;
import de.davelee.trams.model.VehicleModel;
import de.davelee.trams.repository.VehicleRepository;
import de.davelee.trams.services.VehicleService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class VehicleRepositoryTest {
	
	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private VehicleRepository vehicleRepository;
	
	@Test
	public void vehicleTest() {
		VehicleModel vehicleModel = new VehicleModel();
		vehicleModel.setRegistrationNumber("CV58 2XD");
		vehicleModel.setDeliveryDate(Calendar.getInstance());
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
		assertNotNull(vehicle2);
		assertEquals(vehicle2.getRegistrationNumber(), "CV58 2XD");
		assertEquals(vehicle2.getDepreciationFactor(), 0.006, 0.01);
		assertEquals(vehicle2.getImagePath(), "image.png");
		assertEquals(vehicle2.getModel(), "Mercedes");
		assertEquals(vehicle2.getSeatingCapacity(), 40);
		assertEquals(vehicle2.getStandingCapacity(), 60);
		assertEquals(vehicle2.getPurchasePrice(), 200.99, 0.01);
		List<Vehicle> vehicles = vehicleRepository.findAll();
		assertEquals(vehicles.size(), 2);
		//Test remove
		vehicleRepository.delete(vehicle2);
		assertEquals(vehicleRepository.findAll().size(), 1);
	}

}
