package de.davelee.trams.services;

import java.util.Calendar;
import java.util.List;

import de.davelee.trams.data.Scenario;
import de.davelee.trams.data.Vehicle;
import de.davelee.trams.factory.ScenarioFactory;
import de.davelee.trams.factory.VehicleFactory;

public class FactoryService {
	
	private VehicleFactory vehicleFactory;
	private ScenarioFactory scenarioFactory;
	
	public FactoryService() {
	}
	
	public VehicleFactory getVehicleFactory() {
		return vehicleFactory;
	}

	public void setVehicleFactory(VehicleFactory vehicleFactory) {
		this.vehicleFactory = vehicleFactory;
	}

	public ScenarioFactory getScenarioFactory() {
		return scenarioFactory;
	}

	public void setScenarioFactory(ScenarioFactory scenarioFactory) {
		this.scenarioFactory = scenarioFactory;
	}

	public Vehicle createVehicleObject ( String model, String registrationNumber, Calendar deliveryDate ) {
		Vehicle vehicle = vehicleFactory.createVehicleByModel(model);
		if ( vehicle != null ) {
			vehicle.setRegistrationNumber(registrationNumber);
			vehicle.setDeliveryDate(deliveryDate);
		}
		return vehicle;
	}
	
	public Scenario createScenarioObject ( String name ) {
		return scenarioFactory.createScenarioByName(name);
	}
	
	public String getVehicleModel ( int pos ) {
		if ( pos < getNumberAvailableVehicles() ) {
			return vehicleFactory.getAvailableVehicles().get(pos).getModel();
		}
		return null;
	}
	
	public int getNumberAvailableVehicles ( ) {
		return vehicleFactory.getAvailableVehicles().size();
	}
	
	public int getNumberAvailableScenarios ( ) {
		return scenarioFactory.getAvailableScenarios().size();
	}
	
	public String[] getAvailableScenarioNames ( ) {
		List<Scenario> scenarios = scenarioFactory.getAvailableScenarios();
		String[] scenarioNames = new String[scenarios.size()];
		for ( int i = 0; i < scenarios.size(); i++ ) {
			scenarioNames[i] = scenarios.get(i).getScenarioName();
		}
		return scenarioNames;
	}
	
	public String[] getAvailableScenarioCityDescriptions ( ) {
		List<Scenario> scenarios = scenarioFactory.getAvailableScenarios();
		String[] scenarioDescriptions = new String[scenarios.size()];
		for ( int i = 0; i < scenarios.size(); i++ ) {
			scenarioDescriptions[i] = scenarios.get(i).getCityDescription();
		}
		return scenarioDescriptions;
	}
	
}
