package de.davelee.trams.controllers;

import java.util.HashMap;

import de.davelee.trams.TramsGameApplication;
import de.davelee.trams.beans.Scenario;
import de.davelee.trams.services.VehicleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class VehicleControllerTest {

	@Mock
	private VehicleService vehicleService;
	
	@InjectMocks
	private VehicleController vehicleController;
	
	@Test
	public void testSuppliedVehicles ( ) {
		Scenario scenario = new Scenario();
		scenario.setScenarioName("Landuff Transport Company");
		HashMap<String, Integer> suppliedVehicles = new HashMap<String, Integer>();
		suppliedVehicles.put("MyBus Single Decker", Integer.valueOf(1));
		scenario.setSuppliedVehicles(suppliedVehicles);
		Mockito.when(vehicleService.generateRandomReg(2014, "Mustermann GmbH")).thenReturn("DDD4 JKL");
		Mockito.when(vehicleService.saveVehicle(any())).thenReturn(10000.0);
		assertEquals(1, vehicleController.createSuppliedVehicles(scenario, "01-01-2014", "Mustermann GmbH"));
	}

	private void assertEquals ( final int expected, final int actual ) {
		Assertions.assertEquals(expected, actual);
	}

}
