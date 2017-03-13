package de.davelee.trams.controllers;

import de.davelee.trams.data.RouteSchedule;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.RouteScheduleService;

import java.util.List;

public class RouteScheduleController {
	
	 @Autowired
	 private RouteScheduleService routeScheduleService;

	 @Autowired
	 private RouteController routeController;
	 
	 public int getDelay ( final long routeScheduleId ) {
		 return routeScheduleService.getDelay(routeScheduleId);
	 }

	 public long getRouteId ( final long routeScheduleId ) {
		 return routeScheduleService.getRouteScheduleById(routeScheduleId).getRouteId();
	 }

	public String[] getRouteScheduleNames ( final String routeNumber ) {
		List<RouteSchedule> schedules = routeScheduleService.getRouteSchedulesByRouteId(routeController.getRouteId(routeNumber));
		String[] names = new String[schedules.size()];
		for ( int i = 0; i < names.length; i++ ) {
			names[i] = "" + schedules.get(i).getScheduleNumber();
		}
		return names;
	}

	public long getIdFromNumber ( final String scheduleNumber ) {
		return routeScheduleService.getIdFromScheduleNumber(Integer.parseInt(scheduleNumber));
	}
	 
}
