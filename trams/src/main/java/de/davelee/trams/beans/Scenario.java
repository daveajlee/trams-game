package de.davelee.trams.beans;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class representing a scenario (i.e. transport company in TraMS).
 * @author Dave Lee
 */
@Getter
@Setter
@NoArgsConstructor
public class Scenario {

    private String scenarioName;

    private String description;

	private String cityDescription;

    private String targets;

    private int minimumSatisfaction;

    private double minimumBalance;

    private String locationMapFileName;

    private HashMap<String, Integer> suppliedVehicles;

    private List<String> suppliedDrivers;

    private List<String> stopDistances = new ArrayList<>();

}
