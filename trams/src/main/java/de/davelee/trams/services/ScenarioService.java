package de.davelee.trams.services;

import java.util.List;

import de.davelee.trams.beans.Scenario;
import de.davelee.trams.factory.ScenarioFactory;
import de.davelee.trams.model.ScenarioModel;

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

	public ScenarioModel[] getAvailableScenarios ( ) {
		List<Scenario> scenarios = scenarioFactory.getAvailableScenarios();
		ScenarioModel[] scenarioModels = new ScenarioModel[scenarios.size()];
		for ( int i = 0; i < scenarioModels.length; i++ ) {
			scenarioModels[i] = new ScenarioModel();
			scenarioModels[i].setCityDescription(scenarios.get(i).getCityDescription());
			scenarioModels[i].setName(scenarios.get(i).getScenarioName());
			scenarioModels[i].setSuppliedVehicles(scenarios.get(i).getSuppliedVehicles());
			scenarioModels[i].setTargets(scenarios.get(i).getTargets());
			scenarioModels[i].setDescription(scenarios.get(i).getDescription());
		}
		return scenarioModels;
	}

}
