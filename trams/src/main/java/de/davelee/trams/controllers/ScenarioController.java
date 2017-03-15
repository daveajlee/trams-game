package de.davelee.trams.controllers;

import de.davelee.trams.model.ScenarioModel;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.ScenarioService;

public class ScenarioController {
	
	@Autowired
	private ScenarioService scenarioService;
	
	@Autowired
	private GameController gameController;
	
	public String[] getStopNames ( ) {
		return scenarioService.getStopNames(gameController.getScenarioName());
	}

	public ScenarioModel[] getAvailableScenarios ( ) {
		return scenarioService.getAvailableScenarios();
	}

}
