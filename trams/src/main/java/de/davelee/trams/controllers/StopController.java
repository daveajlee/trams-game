package de.davelee.trams.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.StopService;
import org.springframework.stereotype.Controller;

@Controller
public class StopController {
	
	@Autowired
	private StopService stopService;

	@Autowired
	private ScenarioController scenarioController;

	public String[] getAllStops ( final String company ) {
        return stopService.getAllStops(company);
    }

	public int getDistance ( final String scenarioName, final String stop1, final String stop2 ) {
		return stopService.getDistance(scenarioController.getScenario(scenarioName).getStopDistances(), stop1, stop2);
	}
	
}
