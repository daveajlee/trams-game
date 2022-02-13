package de.davelee.trams.controllers;

import de.davelee.trams.api.request.AddStopRequest;
import de.davelee.trams.api.response.StopResponse;
import de.davelee.trams.api.response.StopsResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * This class enables access to Stop data via REST endpoints to the TraMS Operations microservice in the TraMS Platform.
 * @author Dave Lee
 */
@Controller
public class StopController {

	private RestTemplate restTemplate;

	@Value("${server.operations.url}")
	private String operationsServerUrl;

	private ScenarioController scenarioController;

	/**
	 * Add a new stop.
	 * @param stopName a <code>String</code> with the name for this stop.
	 * @param company a <code>String</code> with the name of the company to create stop for.
	 * @param waitingTime a <code>int</code> with the time that the vehicle should wait at this stop.
	 * @param distances a <code>Map</code> with the distances in minutes between this stop and other stops in the scenario as key/value pair.
	 */
	public void saveStop (final String stopName, final String company, final int waitingTime, final Map<String, Integer> distances) {
		restTemplate.postForObject(operationsServerUrl + "stop/",
				AddStopRequest.builder()
						.company(company)
						.name(stopName)
						.waitingTime(waitingTime)
						.distances(distances)
						.build(),
				Void.class);
	}

	/**
	 * Return all stops that exist for the specified company.
	 * @param company a <code>String</code> containing the name of the company to retrieve routes for.
	 * @return a <code>String</code> array containing all stop names for this company.
	 */
	public StopResponse[] getAllStops (final String company ) {
		StopsResponse stopsResponse = restTemplate.getForObject(operationsServerUrl + "stops/?company=" + company, StopsResponse.class);
		if ( stopsResponse != null ) {
			return stopsResponse.getStopResponses();
		}
		return null;
    }

	/**
	 * Return the distance in minutes between two stops for the specified scenario.
	 * @param scenarioName a <code>String</code> containing the name of the scenario to retrieve distances for.
	 * @param stop1 a <code>String</code> containing the stop to start at.
	 * @param stop2 a <code>String</code> containing the stop to end at.
	 * @return a <code>int</code> containing the distance between stop1 and stop2 in minutes.
	 */
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

	/**
	 * Set the scenario controller object via Spring.
	 * @param scenarioController a <code>ScenarioController</code> object.
	 */
	@Autowired
	public void setScenarioController(final ScenarioController scenarioController) {
		this.scenarioController = scenarioController;
	}

	/**
	 * Set the rest template object via Spring.
	 * @param restTemplate a <code>RestTemplate</code> object.
	 */
	@Autowired
	public void setRestTemplate(final RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
}
