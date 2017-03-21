package de.davelee.trams.controllers;

import de.davelee.trams.model.ScenarioModel;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.ScenarioService;
import org.springframework.stereotype.Controller;

@Controller
public class ScenarioController {
	
	@Autowired
	private ScenarioService scenarioService;

	public ScenarioModel[] getAvailableScenarios ( ) {
		return scenarioService.getAvailableScenarios();
	}

	public ScenarioModel getScenario ( final String scenarioName ) {
		return scenarioService.retrieveScenarioObject(scenarioName);
	}

}
