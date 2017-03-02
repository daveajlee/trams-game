package de.davelee.trams.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * Class representing a scenario (i.e. transport company in TraMS).
 * @author Dave Lee
 */
@Entity
@Table(name="SCENARIO")
public class Scenario {
    
	@Id
	@GeneratedValue
	@Column(name="STOP_ID", nullable=false)
	private long id;
	
	@Column(name="SCENARIO_NAME")
    private String scenarioName;
	
	@Column(name="DESCRIPTION")
    private String description;
	
	@Column(name="CITY_DESCRIPTION")
	private String cityDescription;
	
	@Column(name="TARGETS")
    private String targets;
	
	@Column(name="MINIMUM_SATISFACTION")
    private int minimumSatisfaction;
	
	@Column(name="MINIMUM_BALANCE")
    private double minimumBalance;
	
	@Column(name="LOCATION_MAP_FILE_NAME")
    private String locationMapFileName;
	
	@Column(name="SUPPLIED_VEHICLES")
    private HashMap<String, Integer> suppliedVehicles;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="STOP_DISTANCES")
	@Fetch(value = FetchMode.SUBSELECT)
	@Column(name="STOP_DISTANCE")
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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
    	scenario.setScenarioName(this.scenarioName);
    	scenario.setStopDistances(this.stopDistances);
    	scenario.setTargets(this.targets);
    	return scenario;
    }

}
