package de.davelee.trams.controllers;

import java.time.LocalDate;
import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import de.davelee.trams.model.ScenarioModel;

@ExtendWith(SpringExtension.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class VehicleControllerTest {
	
	@Autowired
	private VehicleController vehicleController;
	
	@Test
	public void testSuppliedVehicles ( ) {
		ScenarioModel scenarioModel = new ScenarioModel();
		scenarioModel.setName("Landuff Transport Company");
		HashMap<String, Integer> suppliedVehicles = new HashMap<String, Integer>();
		suppliedVehicles.put("MyBus Single Decker", Integer.valueOf(1));
		scenarioModel.setSuppliedVehicles(suppliedVehicles);
		LocalDate currentDate = LocalDate.of(2014,1,1);
		assertEquals(1, vehicleController.createSuppliedVehicles(scenarioModel, currentDate));
	}

	private void assertEquals ( final int expected, final int actual ) {
		Assertions.assertEquals(expected, actual);
	}

}
