package de.davelee.trams.controllers;

import de.davelee.trams.api.request.AddRouteRequest;
import de.davelee.trams.api.response.RouteResponse;
import de.davelee.trams.api.response.RoutesResponse;
import de.davelee.trams.util.SortedRouteResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * This class enables access to Route data via REST endpoints to the TraMS Operations microservice in the TraMS Platform.
 * @author Dave Lee
 */
@Controller
public class RouteController {

	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${server.operations.url}")
	private String operationsServerUrl;

	/**
	 * Return all routes that exist for the specified company.
	 * @param company a <code>String</code> containing the name of the company to retrieve routes for.
	 * @return a <code>RoutesResponse</code> object containing details of all routes including a count of the number of routes available.
	 */
	public RoutesResponse getRoutes (final String company ) {
		RoutesResponse routesResponse = restTemplate.getForObject(operationsServerUrl + "routes/?company=" + company, RoutesResponse.class);
		if ( routesResponse != null && routesResponse.getRouteResponses() != null ) {
			Arrays.sort(routesResponse.getRouteResponses(), new SortedRouteResponses());
			return routesResponse;
		}
		return null;
	}

	/**
	 * Get the route based on comparing the toString method with the supplied text.
	 * @param routeNumber a <code>String</code> with the string representation of the route.
	 * @param company a <code>String</code> with the name of the company to return route for.
	 * @return a <code>RouteResponse</code> object matching the string representation.
	 */
	public RouteResponse getRoute ( final String routeNumber, final String company ) {
		return restTemplate.getForObject(operationsServerUrl + "route/?company=" + company + "&routeNumber=" + routeNumber, RouteResponse.class);
	}

	/**
	 * Add a new route.
	 * @param routeNumber a <code>String</code> with the number for this route.
	 * @param stopNames a <code>String</code> list with the stops served by this route.
	 * @param company a <code>String</code> with the name of the company to create route for.
	 */
	public void addNewRoute ( final String routeNumber, final List<String> stopNames, final String company ) {
		restTemplate.postForObject(operationsServerUrl + "route/", AddRouteRequest.builder()
				.company(company)
				.routeNumber(routeNumber)
				.build(), Void.class);
	}

	/**
	 * Delete route.
	 * @param company a <code>String</code> with the name of the company.
	 * @param routeNumber a <code>String</code> with the number for this route.
	 */
	public void deleteRoute ( final String company, final String routeNumber ) {
		restTemplate.delete(operationsServerUrl + "route/?company=" + company + "&routeNumber=" + routeNumber);
	}

	/**
	 * Edit route - replace the two routes.
	 * @param company a <code>String</code> with the name of the company.
	 * @param routeNumber a <code>String</code> with the string representation of the route.
	 * @param stopNames a <code>String</code> list with the name of the stops for this route.
	 */
	public void editRoute ( final String company, final String routeNumber, final List<String> stopNames ) {
		//Delete old route.
		deleteRoute(company, routeNumber);
		//Add new route.
		addNewRoute(routeNumber, stopNames, company);
	}

	/**
	 * Load Routes.
	 * @param routeResponses an array of <code>RouteResponse</code> objects with routes to store and delete all other routes.
	 * @param company a <code>String</code> with the name of the company.
	 */
	public void loadRoutes ( final RouteResponse[] routeResponses, final String company ) {
		restTemplate.delete(operationsServerUrl + "routes/?company=" + company);
		for ( RouteResponse routeResponse : routeResponses ) {
			restTemplate.postForObject(operationsServerUrl + "route/", AddRouteRequest.builder()
					.company(company)
					.routeNumber(routeResponse.getRouteNumber())
					.build(), Void.class);
		}
	}

}
