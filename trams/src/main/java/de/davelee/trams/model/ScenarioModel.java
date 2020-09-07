package de.davelee.trams.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
	private List<String> stopDistances;

}
