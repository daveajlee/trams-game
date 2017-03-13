package de.davelee.trams.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.VehicleService;

public class VehicleController {
	
	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private RouteScheduleController routeScheduleController;
	
	public void assignVehicleToRouteSchedule ( long vehicleId, String scheduleNumber ) {
		vehicleService.getVehicleById(vehicleId).setRouteScheduleId(routeScheduleController.getIdFromNumber(scheduleNumber));
	}

}
