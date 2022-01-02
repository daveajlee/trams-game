package de.davelee.trams.controllers;

import de.davelee.trams.api.request.AddStopRequest;
import de.davelee.trams.api.response.StopsResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

@Controller
public class StopController {

	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${server.operations.url}")
	private String operationsServerUrl;

	@Autowired
	private ScenarioController scenarioController;

	public void saveStop ( final String stopName, final String company ) {
		restTemplate.postForObject(operationsServerUrl + "stop/",
				AddStopRequest.builder()
						.company(company)
						.name(stopName)
						.build(),
				Void.class);
	}

	public String[] getAllStops ( final String company ) {
		StopsResponse stopsResponse = restTemplate.getForObject(operationsServerUrl + "stops/?company=" + company, StopsResponse.class);
		String[] stopNames = new String[stopsResponse.getStopResponses().length];
		for ( int i = 0; i < stopNames.length; i++ ) {
			stopNames[i] = stopsResponse.getStopResponses()[i].getName();
		}
		return stopNames;
    }

	public int getDistance ( final String scenarioName, final String stop1, final String stop2 ) {
		int stop1Pos = -1; int stop2Pos = -1; int count = 0;
		for ( String stopDistance : scenarioController.getScenario(scenarioName).getStopDistances() ) {
			String stopName = stopDistance.split(":")[0];
			if ( stopName.equalsIgnoreCase(stop1) ) { stop1Pos = count; }
			else if ( stopName.equalsIgnoreCase(stop2) ) { stop2Pos = count; }
			count++;
		}
		return Integer.parseInt(scenarioController.getScenario(scenarioName).getStopDistances().get(stop1Pos).split(":")[1].split(",")[stop2Pos]);
	}
	
}
