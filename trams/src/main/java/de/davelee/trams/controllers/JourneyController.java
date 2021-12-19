package de.davelee.trams.controllers;

import java.time.LocalTime;
import java.util.NoSuchElementException;

import de.davelee.trams.model.JourneyModel;
import de.davelee.trams.model.RouteScheduleModel;
import de.davelee.trams.model.StopTimeModel;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.JourneyService;
import org.springframework.stereotype.Controller;

@Controller
public class JourneyController {
	
	@Autowired
	private JourneyService journeyService;

	@Autowired
	private ScenarioController scenarioController;

	public LocalTime getStopTime ( final JourneyModel journeyModel, String name ) {
		StopTimeModel stopTime = journeyService.getStopTime(journeyModel, name);
		if ( stopTime != null ) {
			return stopTime.getTime();
		}
		throw new NoSuchElementException();
	}

	public String[] getAllStops ( final String company ) {
        return journeyService.getAllStops(company);
    }

	public int getDistance ( final String scenarioName, final String stop1, final String stop2 ) {
		return journeyService.getDistance(scenarioController.getScenario(scenarioName).getStopDistances(), stop1, stop2);
	}
	
}
