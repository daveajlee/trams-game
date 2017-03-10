package de.davelee.trams.services;

import java.util.List;

import de.davelee.trams.beans.Scenario;
import de.davelee.trams.factory.ScenarioFactory;

public class ScenarioService {

	private ScenarioFactory scenarioFactory;
	
	public ScenarioService() {
		
	}

	public ScenarioFactory getScenarioFactory() {
		return scenarioFactory;
	}

	public void setScenarioFactory(ScenarioFactory scenarioFactory) {
		this.scenarioFactory = scenarioFactory;
	}

	/**
     * Get the stop names as a String array plus a - and return it.
     * @return a <code>String</code> array with the stop names plus a -. 
     */
    public String[] getStopNames ( String scenarioName ) {
		Scenario scenario = retrieveScenarioObject(scenarioName);
        String[] possStops = new String[scenario.getStopDistances().size() + 1];
        //Add all stops.
        for ( int i = 0; i < scenario.getStopDistances().size(); i++ ) {
            possStops[i] = scenario.getStopDistances().get(i).split(":")[0];
        }
        //Add dash at end.
        possStops[scenario.getStopDistances().size()] = "-";
        //Return stop names.
        return possStops;
    }

	public Scenario retrieveScenarioObject ( String name ) {
		return scenarioFactory.createScenarioByName(name);
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
