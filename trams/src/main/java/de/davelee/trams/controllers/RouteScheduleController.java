package de.davelee.trams.controllers;

import de.davelee.trams.data.RouteSchedule;
import de.davelee.trams.model.RouteScheduleModel;
import de.davelee.trams.model.VehicleModel;
import de.davelee.trams.util.DifficultyLevel;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.RouteScheduleService;

import java.util.Calendar;
import java.util.List;

public class RouteScheduleController {
	
	 @Autowired
	 private RouteScheduleService routeScheduleService;

	 @Autowired
	 private RouteController routeController;

	 @Autowired
	 private JourneyController journeyController;

	 @Autowired
	 private VehicleController vehicleController;

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

	/**
	 * Get the current stop name which this route schedule is on based on the current date.
	 * @param currentDate a <code>Calendar</code> object.
	 * @return a <code>String</code> array with the stop details.
	 */
	public String getCurrentStopName ( long routeScheduleId, Calendar currentDate, DifficultyLevel difficultyLevel ) {
		//Copy current Date to current Time and then use delay to determine position.
		Calendar currentTime = (Calendar) currentDate.clone();
		currentTime.add(Calendar.MINUTE, -routeScheduleService.getRouteScheduleById(routeScheduleId).getDelayInMins());
		String stopName = journeyController.getStopName(routeScheduleId, currentTime);
		if ( stopName.contentEquals("Depot") ) {
			routeScheduleService.getRouteScheduleById(routeScheduleId).setDelayInMins(0); //Finished for the day or not started.
		}
		else {
			//Now fiddle delay!
			routeScheduleService.calculateNewDelay(routeScheduleService.getRouteScheduleById(routeScheduleId), difficultyLevel);
		}
		return stopName;
	}

	public String getLastStopName ( long routeScheduleId, Calendar currentDate, DifficultyLevel difficultyLevel) {
		//Copy current Date to current Time and then use delay to determine position.
		Calendar currentTime = (Calendar) currentDate.clone();
		currentTime.add(Calendar.MINUTE, -routeScheduleService.getRouteScheduleById(routeScheduleId).getDelayInMins());
		String stopName = journeyController.getLastStopName(routeScheduleId, currentTime);
		if ( stopName.contentEquals("Depot")) {
			routeScheduleService.getRouteScheduleById(routeScheduleId).setDelayInMins(0); //Finished for the day.
		}
		else {
			//Now fiddle delay!
			routeScheduleService.calculateNewDelay(routeScheduleService.getRouteScheduleById(routeScheduleId), difficultyLevel);
		}
		return stopName;
	}

	public RouteScheduleModel retrieveModel (long routeScheduleId ) {
		RouteScheduleModel routeScheduleModel = new RouteScheduleModel();
		routeScheduleModel.setDelay(routeScheduleService.getDelay(routeScheduleId));
		VehicleModel vehicleModel = vehicleController.retrieveModel(routeScheduleId);
		routeScheduleModel.setImage(vehicleModel.getImagePath());
		routeScheduleModel.setRegistrationNumber(vehicleModel.getRegistrationNumber());
		return routeScheduleModel;
	}
	 
}
