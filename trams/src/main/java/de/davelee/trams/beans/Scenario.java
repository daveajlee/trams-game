package de.davelee.trams.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class representing a scenario (i.e. transport company in TraMS).
 * @author Dave Lee
 */
public class Scenario {

	private long id;

    private String scenarioName;

    private String description;

	private String cityDescription;

    private String targets;

    private int minimumSatisfaction;

    private double minimumBalance;

    private String locationMapFileName;

    private HashMap<String, Integer> suppliedVehicles;

    private List<String> suppliedDrivers;

    private List<String> stopDistances;
    
    public Scenario ( ) {
    	stopDistances = new ArrayList<String>();
    }
    
    public HashMap<String, Integer> getSuppliedVehicles() {
		return suppliedVehicles;
	}

	public void setSuppliedVehicles(HashMap<String, Integer> suppliedVehicles) {
		this.suppliedVehicles = suppliedVehicles;
	}

	public List<String> getSuppliedDrivers() {
		return suppliedDrivers;
	}

	public void setSuppliedDrivers(final List<String> suppliedDrivers) {
		this.suppliedDrivers = suppliedDrivers;
	}

	public String getLocationMapFileName() {
		return locationMapFileName;
	}

	public void setLocationMapFileName(String locationMapFileName) {
		this.locationMapFileName = locationMapFileName;
	}

	public int getMinimumSatisfaction() {
		return minimumSatisfaction;
	}

	public void setMinimumSatisfaction(int minimumSatisfaction) {
		this.minimumSatisfaction = minimumSatisfaction;
	}
	
	public double getMinimumBalance() {
		return minimumBalance;
	}
	
	public void setMinimumBalance(double minimumBalance) {
		this.minimumBalance = minimumBalance;
	}

	public String getTargets() {
		return targets;
	}

	public void setTargets(String targets) {
		this.targets = targets;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}
    
    public List<String> getStopDistances() {
    	return stopDistances;
    }
    
    public void setStopDistances(List<String> stopDistances) {
    	this.stopDistances = stopDistances;
    }
    
    public String getCityDescription() {
		return cityDescription;
	}

	public void setCityDescription(String cityDescription) {
		this.cityDescription = cityDescription;
	}

	public Scenario clone() {
    	Scenario scenario = new Scenario();
    	scenario.setCityDescription(this.cityDescription);
    	scenario.setDescription(this.description);
    	scenario.setLocationMapFileName(this.locationMapFileName);
    	scenario.setMinimumBalance(this.minimumBalance);
    	scenario.setMinimumSatisfaction(this.minimumSatisfaction);
    	scenario.setSuppliedVehicles(this.suppliedVehicles);
    	scenario.setSuppliedDrivers(this.suppliedDrivers);
    	scenario.setScenarioName(this.scenarioName);
    	scenario.setStopDistances(this.stopDistances);
    	scenario.setTargets(this.targets);
    	return scenario;
    }

}
