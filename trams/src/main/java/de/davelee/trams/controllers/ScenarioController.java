package de.davelee.trams.controllers;

import de.davelee.trams.model.ScenarioModel;
import de.davelee.trams.services.ScenarioService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@ConfigurationProperties(prefix="scenarios")
@PropertySource("classpath:scenarios.properties")
@Getter
@Setter
public class ScenarioController {

	private List<String> scenarioList;

	@Autowired
	private ScenarioService scenarioService;

	public List<String> getAvailableScenarios ( ) {
		return scenarioList;
	}

	public ScenarioModel getScenario ( final String scenarioName ) {
		return scenarioService.retrieveScenarioFile(scenarioName);
	}

}
