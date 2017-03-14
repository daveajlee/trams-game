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

	/**
	 * Shorten schedule to the specific stop stated and reduce the delay accordingly.
	 * @param stop a <code>String</code> with the stop to terminate at.
	 * @param currentTime a <code>Calendar</code> with the current time.
	 */
	public void shortenSchedule ( long routeScheduleId, String stop, Calendar currentTime ) {
		RouteSchedule schedule = routeScheduleService.getRouteScheduleById(routeScheduleId);
		//Shorten schedule to the specific stop stated and reduce the delay accordingly - for current service remove stops after the specified stop.
		//logger.debug("Service was ending at: " + theAssignedSchedule.getCurrentService().getEndDestination());
		String oldEnd = journeyController.getLastStopName(routeScheduleId, currentTime);
		//Now we need to remove the stops in beteen!
		long timeDiff = journeyController.removeStopsFromCurrentJourney(routeScheduleId, currentTime, stop, oldEnd);
		//Now for the next service we need to remove stops between first stop and stop.
		long timeDiff2 = journeyController.removeStopsFromNextJourney(routeScheduleId, currentTime, stop, oldEnd);
		//Divide both timeDiff's by 60 to convert to minutes and then use that to reduce vehicle delay.
		long delayReduction = (timeDiff/60) + (timeDiff2/60);
		//Reduce delay!
		routeScheduleService.reduceDelay(schedule, (int) delayReduction);
	}

	/**
	 * Put this vehicle out of service from the current stop until the new stop.
	 * @param currentStop a <code>String</code> with the stop to go out of service from.
	 * @param newStop a <code>String</code> with the stop to resume service from.
	 * @param currentTime a <code>Calendar</code> object with the current time.
	 */
	public void outOfService ( long routeScheduleId, String currentStop, String newStop, Calendar currentTime ) {
		//Get the time difference between current stop and new stop.
		RouteSchedule schedule = routeScheduleService.getRouteScheduleById(routeScheduleId);
		long timeDiff = journeyController.getStopMaxTimeDiff(routeScheduleId, currentTime, currentStop, newStop);
		routeScheduleService.reduceDelay(schedule, (int) (timeDiff/2));
		//logger.debug("Vehicle delay reduced from " + oldDelay + " mins to " + getVehicleDelay() + " mins.");
	}
	 
}
