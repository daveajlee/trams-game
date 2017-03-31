package de.davelee.trams.model;

import java.util.HashMap;
import java.util.List;

public class ScenarioModel {

	private String name;
	private String cityDescription;
	private HashMap<String, Integer> suppliedVehicles;
	private List<String> suppliedDrivers;
	private String targets;
	private String description;
	private String[] stopNames;
	private int minimumSatisfaction;
	private String locationMapFileName;
	
	public String getName() {
		return name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public String getCityDescription() {
		return cityDescription;
	}
	
	public void setCityDescription(final String cityDescription) {
		this.cityDescription = cityDescription;
	}

	public HashMap<String, Integer> getSuppliedVehicles() {
		return suppliedVehicles;
	}

	public void setSuppliedVehicles(final HashMap<String, Integer> suppliedVehicles) {
		this.suppliedVehicles = suppliedVehicles;
	}

	public List<String> getSuppliedDrivers() {
		return suppliedDrivers;
	}

	public void setSuppliedDrivers(final List<String> suppliedDrivers) {
		this.suppliedDrivers = suppliedDrivers;
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

	public String[] getStopNames() {
		return stopNames;
	}

	public void setStopNames(final String[] stopNames) {
		this.stopNames = stopNames;
	}

	public int getMinimumSatisfaction() {
		return minimumSatisfaction;
	}

	public void setMinimumSatisfaction(final int minimumSatisfaction) {
		this.minimumSatisfaction = minimumSatisfaction;
	}

	public String getLocationMapFileName() {
		return locationMapFileName;
	}

	public void setLocationMapFileName(final String locationMapFileName) {
		this.locationMapFileName = locationMapFileName;
	}

}
