package de.davelee.trams.services;

import java.util.List;

import de.davelee.trams.beans.Scenario;
import de.davelee.trams.factory.ScenarioFactory;
import de.davelee.trams.model.ScenarioModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScenarioService {

	@Autowired
	private ScenarioFactory scenarioFactory;
	
	public ScenarioService() {
		
	}

	/**
     * Get the stop names as a String array plus a - and return it.
     * @return a <code>String</code> array with the stop names plus a -. 
     */
	private String[] getStopNames ( final List<String> stopDistances ) {
		String[] possStops = new String[stopDistances.size() + 1];
        //Add all stops.
		for ( int i = 0; i < stopDistances.size(); i++ ) {
			possStops[i] = stopDistances.get(i).split(":")[0];
		}
        //Return stop names.
        return possStops;
    }

	public ScenarioModel retrieveScenarioObject ( String name ) {
		if ( scenarioFactory.createScenarioByName(name) != null ) {
			return convertToScenarioModel(scenarioFactory.createScenarioByName(name));
		}
		return null;
	}

	public ScenarioModel[] getAvailableScenarios ( ) {
		List<Scenario> scenarios = scenarioFactory.getAvailableScenarios();
		ScenarioModel[] scenarioModels = new ScenarioModel[scenarios.size()];
		for ( int i = 0; i < scenarioModels.length; i++ ) {
			scenarioModels[i] = convertToScenarioModel(scenarios.get(i));
		}
		return scenarioModels;
	}

	private ScenarioModel convertToScenarioModel ( final Scenario scenario ) {
		ScenarioModel scenarioModel = new ScenarioModel();
		scenarioModel.setCityDescription(scenario.getCityDescription());
		scenarioModel.setName(scenario.getScenarioName());
		scenarioModel.setSuppliedVehicles(scenario.getSuppliedVehicles());
		scenarioModel.setTargets(scenario.getTargets());
		scenarioModel.setDescription(scenario.getDescription());
		scenarioModel.setStopNames(getStopNames(scenario.getStopDistances()));
		scenarioModel.setMinimumSatisfaction(scenario.getMinimumSatisfaction());
		scenarioModel.setLocationMapFileName(scenario.getLocationMapFileName());
		return scenarioModel;
	}

}
