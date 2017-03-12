package de.davelee.trams.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.RouteScheduleService;

public class RouteScheduleController {
	
	 @Autowired
	 private RouteScheduleService routeScheduleService;
	 
	 public int getDelay ( long routeScheduleId ) {
		 return routeScheduleService.getDelay(routeScheduleId);
	 }

	 public long getRouteId ( long routeScheduleId ) {
		 return routeScheduleService.getRouteScheduleById(routeScheduleId).getRouteId();
	 }
	 
}
