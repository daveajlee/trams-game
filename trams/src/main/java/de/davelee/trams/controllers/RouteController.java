package de.davelee.trams.controllers;

import de.davelee.trams.model.RouteModel;
import de.davelee.trams.util.SortedRouteModels;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.RouteService;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;

@Controller
public class RouteController {
	
	@Autowired
	private RouteService routeService;

	public RouteModel[] getRouteModels ( ) {
		RouteModel[] routeModels = routeService.getAllRoutes();
		Arrays.sort(routeModels, new SortedRouteModels());
		return routeModels;
	}

	public int getNumberRoutes ( ) {
		return routeService.getAllRoutes().length;
	}

	/**
	 * Get the route based on comparing the toString method with the supplied text.
	 * @param routeNumber a <code>String</code> with the string representation of the route.
	 * @return a <code>RouteModel</code> object matching the string representation.
	 */
	public RouteModel getRoute ( final String routeNumber ) {
		return routeService.getRoute(routeNumber);
	}

	/**
	 * Add a new route.
	 * @param routeNumber a <code>String</code> with the number for this route.
	 * @param stopNames a <code>String</code> list with the stops served by this route.
	 */
	public void addNewRoute ( final String routeNumber, final List<String> stopNames ) {
		RouteModel routeModel = new RouteModel();
		routeModel.setRouteNumber(routeNumber);
		routeModel.setStopNames(stopNames);
		routeService.saveRoute(routeModel);
	}

	/**
	 * Delete route.
	 * @param routeModel a <code>RouteModel</code> object representing the route to delete.
	 */
	public void deleteRoute ( final RouteModel routeModel ) {
		routeService.removeRoute(routeModel);
	}

	/**
	 * Edit route - replace the two routes.
	 * @param oldRouteModel a <code>RouteModel</code> object with the old route.
	 * @param routeNumber a <code>String</code> with the string representation of the route.
	 * @param stopNames a <code>String</code> list with the name of the stops for this route.
	 */
	public void editRoute ( final RouteModel oldRouteModel, final String routeNumber, final List<String> stopNames ) {
		//Delete old route.
		deleteRoute(oldRouteModel);
		//Add new route.
		addNewRoute(routeNumber, stopNames);
	}

	/**
	 * Load Routes.
	 * @param routeModels an array of <code>RouteModel</code> objects with routes to store and delete all other routes.
	 */
	public void loadRoutes ( final RouteModel[] routeModels ) {
		routeService.deleteAllRoutes();
		for ( RouteModel routeModel : routeModels ) {
			routeService.saveRoute(routeModel);
		}
	}

}
