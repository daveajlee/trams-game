package de.davelee.trams.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.RouteService;

public class RouteController {
	
	@Autowired
	private RouteService routeService;
	
	@Autowired
	private RouteScheduleController routeScheduleController;
	
	public String getRouteNumber ( long routeScheduleId) {
		return routeService.getRouteById(routeScheduleController.getRouteId(routeScheduleId)).getRouteNumber();
	}

}
