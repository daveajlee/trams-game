package de.davelee.trams.model;

import java.util.HashMap;

public class ScenarioModel {

	private String name;
	private String cityDescription;
	private HashMap<String, Integer> suppliedVehicles;
	private String targets;
	private String description;
	
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

}
