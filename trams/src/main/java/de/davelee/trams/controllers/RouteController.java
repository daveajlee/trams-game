package de.davelee.trams.controllers;

import de.davelee.trams.data.Route;
import de.davelee.trams.model.RouteModel;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.RouteService;

import java.util.ArrayList;
import java.util.List;

public class RouteController {
	
	@Autowired
	private RouteService routeService;
	
	@Autowired
	private RouteScheduleController routeScheduleController;
	
	public String getRouteNumber ( final long routeScheduleId) {
		return routeService.getRouteById(routeScheduleController.getRouteId(routeScheduleId)).getRouteNumber();
	}

	public String getRouteNumberByPosition ( final int position ) {
		return routeService.getAllRoutes().get(position).getRouteNumber();
	}

	public int getNumberRoutes ( ) {
		return routeService.getAllRoutes().size();
	}

	/**
	 * Get the route based on comparing the toString method with the supplied text.
	 * @param routeNumber a <code>String</code> with the string representation of the route.
	 * @return a <code>RouteModel</code> object matching the string representation.
	 */
	public RouteModel getRoute ( final String routeNumber ) {
		return convertToRouteModel(routeService.getRoute(routeNumber));
	}

	public long getRouteId ( final String routeNumber ) {
		return routeService.getRoute(routeNumber).getId();
	}

	private RouteModel convertToRouteModel ( final Route route ) {
		RouteModel routeModel = new RouteModel();
		routeModel.setRouteNumber(route.getRouteNumber());
		List<String> stopNames = new ArrayList<String>();
		for ( int i = 0; i < route.getStops().size(); i++ ) {
			stopNames.add(route.getStops().get(i).getStopName());
		}
		routeModel.setStopNames(stopNames);
		return routeModel;
	}

	public int getDistance ( final String scenarioName, final String stop1, final String stop2 ) {
		return routeService.getDistance(scenarioName, stop1, stop2);
	}

	/**
	 * Add a new route.
	 * @param r a <code>Route</code> object.
	 */
    public void addNewRoute ( String routeNumber, String[] stopNames ) {
		routeService.saveRoute(routeService.createRoute(routeNumber, stopNames));
	}

	/**
	 * Delete route.
	 * @param r a <code>Route</code> object to delete.
	 */
	public void deleteRoute ( long routeId ) {
		routeService.removeRoute(routeService.getRouteById(routeId));
	}

	public List<Route> getAllRoutes ( ) {
		return routeService.getAllRoutes();
	}

	/**
	 * Edit route - replace the two routes.
	 * @param oldRoute a <code>Route</code> object with the old route.
	 * @param newRoute a <code>Route</code> object with the new route.
	 */
    public void editRoute ( long routeId, String routeNumber, String[] stopNames ) {
		//Delete old route.
		deleteRoute(routeId);
		//Add new route.
		addNewRoute(routeNumber, stopNames);
	}

}
