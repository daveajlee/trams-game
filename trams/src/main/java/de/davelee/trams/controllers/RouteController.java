package de.davelee.trams.controllers;

import de.davelee.trams.api.request.AddRouteRequest;
import de.davelee.trams.api.response.RouteResponse;
import de.davelee.trams.util.SortedRouteResponses;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.RouteService;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;

@Controller
public class RouteController {
	
	@Autowired
	private RouteService routeService;

	public RouteResponse[] getRoutes (final String company ) {
		RouteResponse[] routeResponses = routeService.getAllRoutes(company);
		Arrays.sort(routeResponses, new SortedRouteResponses());
		return routeResponses;
	}

	public int getNumberRoutes ( final String company ) {
		RouteResponse[] routeModels = routeService.getAllRoutes(company);
		return routeModels != null ? routeService.getAllRoutes(company).length : 0;
	}

	/**
	 * Get the route based on comparing the toString method with the supplied text.
	 * @param routeNumber a <code>String</code> with the string representation of the route.
	 * @return a <code>RouteResponse</code> object matching the string representation.
	 */
	public RouteResponse getRoute ( final String routeNumber, final String company ) {
		return routeService.getRoute(routeNumber, company);
	}

	/**
	 * Add a new route.
	 * @param routeNumber a <code>String</code> with the number for this route.
	 * @param stopNames a <code>String</code> list with the stops served by this route.
	 */
	public void addNewRoute ( final String routeNumber, final List<String> stopNames, final String company ) {
		routeService.saveRoute(AddRouteRequest.builder()
				.company(company)
				.routeNumber(routeNumber)
				.build());
	}

	/**
	 * Delete route.
	 * @param routeNumber a <code>String</code> with the number for this route.
	 */
	public void deleteRoute ( final String company, final String routeNumber ) {
		routeService.removeRoute(company, routeNumber);
	}

	/**
	 * Edit route - replace the two routes.
	 * @param company a <code>String</code> with the name of the company running .
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
	 */
	public void loadRoutes ( final RouteResponse[] routeResponses, final String company ) {
		routeService.deleteAllRoutes(company);
		for ( RouteResponse routeResponse : routeResponses ) {
			routeService.saveRoute(AddRouteRequest.builder()
					.company(company)
					.routeNumber(routeResponse.getRouteNumber())
					.build());
		}
	}

}
