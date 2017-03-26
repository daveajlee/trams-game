package de.davelee.trams.controllers;

import java.util.Calendar;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.davelee.trams.model.ScenarioModel;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/de/davelee/trams/spring/test-context.xml")
public class VehicleControllerTest {
	
	@Autowired
	private VehicleController vehicleController;
	
	@Test
	public void testSuppliedVehicles ( ) {
		ScenarioModel scenarioModel = new ScenarioModel();
		scenarioModel.setName("Landuff Transport Company");
		HashMap<String, Integer> suppliedVehicles = new HashMap<String, Integer>();
		suppliedVehicles.put("MyBus Single Decker", new Integer(1));
		scenarioModel.setSuppliedVehicles(suppliedVehicles);
		Calendar currentDate = Calendar.getInstance(); currentDate.set(Calendar.YEAR, 2014);
		vehicleController.createSuppliedVehicles(scenarioModel, currentDate);
	}

}
