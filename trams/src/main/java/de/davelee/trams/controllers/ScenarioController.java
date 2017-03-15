package de.davelee.trams.controllers;

import de.davelee.trams.model.ScenarioModel;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.ScenarioService;

public class ScenarioController {
	
	@Autowired
	private ScenarioService scenarioService;
	
	@Autowired
	private GameController gameController;

	public ScenarioModel[] getAvailableScenarios ( ) {
		return scenarioService.getAvailableScenarios();
	}

	public ScenarioModel getScenario ( final String scenarioName ) {
		return scenarioService.retrieveScenarioObject(scenarioName);
	}

}
