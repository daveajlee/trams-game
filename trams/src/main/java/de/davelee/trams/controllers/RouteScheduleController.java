package de.davelee.trams.controllers;

import de.davelee.trams.data.RouteSchedule;
import de.davelee.trams.model.RouteScheduleModel;
import de.davelee.trams.model.VehicleModel;
import de.davelee.trams.util.DifficultyLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.RouteScheduleService;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class RouteScheduleController {

	private static final Logger logger = LoggerFactory.getLogger(RouteScheduleController.class);
	
	 @Autowired
	 private RouteScheduleService routeScheduleService;

	 @Autowired
	 private RouteController routeController;

	 @Autowired
	 private JourneyController journeyController;

	 @Autowired
	 private VehicleController vehicleController;

	 @Autowired
	 private GameController gameController;

	 private List<Integer> routeDetailPos;

	 public long getRouteId ( final long routeScheduleId ) {
		 return routeScheduleService.getRouteScheduleById(routeScheduleId).getRouteId();
	 }

	public RouteScheduleModel[] getRouteSchedules ( final String routeNumber ) {
		List<RouteSchedule> schedules = routeScheduleService.getRouteSchedulesByRouteId(routeController.getRouteId(routeNumber));
		RouteScheduleModel[] routeScheduleModels = new RouteScheduleModel[schedules.size()];
		for ( int i = 0; i < routeScheduleModels.length; i++ ) {
			routeScheduleModels[i] = retrieveModel(schedules.get(i).getId());
		}
		return routeScheduleModels;
	}

	public long getIdFromNumber ( final int scheduleNumber ) {
		return routeScheduleService.getIdFromScheduleNumber(scheduleNumber);
	}

	/**
	 * Get the current stop name which this route schedule is on based on the current date.
	 * @param currentDate a <code>Calendar</code> object.
	 * @return a <code>String</code> array with the stop details.
	 */
	public String getCurrentStopName ( final RouteScheduleModel routeScheduleModel, Calendar currentDate, DifficultyLevel difficultyLevel ) {
		//Copy current Date to current Time and then use delay to determine position.
		Calendar currentTime = (Calendar) currentDate.clone();
		currentTime.add(Calendar.MINUTE, -routeScheduleModel.getDelay());
		String stopName = journeyController.getStopName(getIdFromNumber(routeScheduleModel.getScheduleNumber()), currentTime);
		if ( !stopName.contentEquals("Depot") ) {
			//Now fiddle delay!
			routeScheduleService.calculateNewDelay(routeScheduleService.getRouteScheduleById(getIdFromNumber(routeScheduleModel.getScheduleNumber())), difficultyLevel);
		}
		return stopName;
	}

	public String getLastStopName ( final RouteScheduleModel routeScheduleModel, Calendar currentDate, DifficultyLevel difficultyLevel) {
		//Copy current Date to current Time and then use delay to determine position.
		Calendar currentTime = (Calendar) currentDate.clone();
		currentTime.add(Calendar.MINUTE, -routeScheduleModel.getDelay());
		String stopName = journeyController.getLastStopName(getIdFromNumber(routeScheduleModel.getScheduleNumber()), currentTime);
		if ( stopName.contentEquals("Depot")) {
			routeScheduleService.getRouteScheduleById(getIdFromNumber(routeScheduleModel.getScheduleNumber())).setDelayInMins(0); //Finished for the day.
		}
		else {
			//Now fiddle delay!
			routeScheduleService.calculateNewDelay(routeScheduleService.getRouteScheduleById(getIdFromNumber(routeScheduleModel.getScheduleNumber())), difficultyLevel);
		}
		return stopName;
	}

	public RouteScheduleModel retrieveModel (long routeScheduleId ) {
		RouteScheduleModel routeScheduleModel = new RouteScheduleModel();
		routeScheduleModel.setDelay(routeScheduleService.getDelay(routeScheduleId));
		VehicleModel vehicleModel = vehicleController.retrieveModel(routeScheduleId);
		routeScheduleModel.setImage(vehicleModel.getImagePath());
		routeScheduleModel.setRegistrationNumber(vehicleModel.getRegistrationNumber());
		routeScheduleModel.setScheduleNumber(routeScheduleService.getRouteScheduleById(routeScheduleId).getScheduleNumber());
		return routeScheduleModel;
	}

	public RouteScheduleModel[] getRouteSchedulesByRouteId ( final long routeId ) {
		List<RouteSchedule> routeSchedules = routeScheduleService.getRouteSchedulesByRouteId(routeId);
		RouteScheduleModel[] routeScheduleModels = new RouteScheduleModel[routeSchedules.size()];
		for ( int i = 0; i < routeScheduleModels.length; i++ ) {
			routeScheduleModels[i] = retrieveModel(routeSchedules.get(i).getId());
		}
		return routeScheduleModels;
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

	public List<RouteSchedule> getAllRouteSchedules ( ) {
		return routeScheduleService.getAllRouteSchedules();
	}

	/**
	 * Set the minimum and maximum display of vehicles.
	 * @param min a <code>int</code> with the minimum.
	 * @param max a <code>int</code> with the maximum.
	 * @param routeNumber a <code>String</code> with the route number.
	 */
	public int setCurrentDisplayMinMax ( final int min, final int max, final String routeNumber ) {
		int returnMax = max;
		//Clear the original matrix for which routes to display.
		routeDetailPos = new LinkedList<Integer>();
		//Store the currentDate - we will need it for display schedules.
		Calendar currentTime = gameController.getCurrentSimTime();
		//Determine the route ids we will display using these parameters.
		logger.debug("Route number is " + routeNumber);
		logger.debug("Number of possible display schedules: " +  routeScheduleService.getRouteSchedulesByRouteId(routeController.getRouteId(routeNumber)).size());
		if ( routeScheduleService.getRouteSchedulesByRouteId(routeController.getRouteId(routeNumber)).size() < max ) { returnMax = routeScheduleService.getRouteSchedulesByRouteId(routeController.getRouteId(routeNumber)).size(); }
		//logger.debug("Max vehicles starts at " + max + " - routeDetails size is " + routeDetails.size());
		logger.debug("Min is " + min + " & Max is " + max);
		if ( min == max ) {
			if ( vehicleController.retrieveModel(routeScheduleService.getRouteSchedulesByRouteId(routeController.getRouteId(routeNumber)).get(min).getId()) == null ) {
				logger.debug("A schedule was null");
			}
			if ( getCurrentStopName(getRouteSchedulesByRouteId(routeController.getRouteId(routeNumber))[min], currentTime, gameController.getDifficultyLevel()).equalsIgnoreCase("Depot") ) {
				logger.debug("Vehicle in depot!");
			}
			if ( vehicleController.retrieveModel(routeScheduleService.getRouteSchedulesByRouteId(routeController.getRouteId(routeNumber)).get(min).getId()) != null && !getCurrentStopName( getRouteSchedulesByRouteId(routeController.getRouteId(routeNumber))[min], currentTime, gameController.getDifficultyLevel()).equalsIgnoreCase("Depot") ) {
				//logger.debug("Adding Route Detail " + routeDetails.get(i).getId());
				routeDetailPos.add(0);
			}
			else {
				returnMax++;
				//logger.debug("Max is now " + max + " - routeDetails size is: " + routeDetails.size());
				if ( routeScheduleService.getRouteSchedulesByRouteId(routeController.getRouteId(routeNumber)).size() < max ) { returnMax = routeScheduleService.getRouteSchedulesByRouteId(routeController.getRouteId(routeNumber)).size(); }
				//logger.debug("Route Detail " + routeDetails.get(i).getId() + " was null - maxVehicles is now " + max);
			}
		}
		for ( int i = min; i < max; i++ ) { //Changed from i = 0; i < routeDetails.size().
			if ( vehicleController.retrieveModel(routeScheduleService.getRouteSchedulesByRouteId(routeController.getRouteId(routeNumber)).get(i).getId()) == null ) {
				logger.debug("A schedule was null");
			}
			if ( getCurrentStopName(getRouteSchedulesByRouteId(routeController.getRouteId(routeNumber))[i], currentTime, gameController.getDifficultyLevel()).equalsIgnoreCase("Depot") ) {
				logger.debug("Vehicle in depot!");
			}
			if ( vehicleController.retrieveModel(routeScheduleService.getRouteSchedulesByRouteId(routeController.getRouteId(routeNumber)).get(i).getId()) != null && !getCurrentStopName(getRouteSchedulesByRouteId(routeController.getRouteId(routeNumber))[i], currentTime, gameController.getDifficultyLevel()).equalsIgnoreCase("Depot") ) {
				//logger.debug("Adding Route Detail " + routeDetails.get(i).getId());
				routeDetailPos.add(i);
			}
			else {
				returnMax++;
				//logger.debug("Max is now " + max + " - routeDetails size is: " + routeDetails.size());
				if ( routeScheduleService.getRouteSchedulesByRouteId(routeController.getRouteId(routeNumber)).size() < max ) { returnMax = routeScheduleService.getRouteSchedulesByRouteId(routeController.getRouteId(routeNumber)).size(); }
				//logger.debug("Route Detail " + routeDetails.get(i).getId() + " was null - maxVehicles is now " + max);
			}
		}
		return returnMax;
	}

	/**
	 * Get the current number of display schedules.
	 * @return a <code>int</code> with the current number of display schedules.
	 */
	public int getNumCurrentDisplaySchedules ( ) {
		return routeDetailPos.size();
	}

	/**
	 * Get the current minimum schedule.
	 * @return a <code>int</code> with the id of the first schedule.
	 */
	public int getCurrentMinSchedule ( ) {
		return routeDetailPos.get(0);
	}

	/**
	 * Get the current maximum schedule.
	 * @return a <code>int</code> with the id of the second schedule.
	 */
	public int getCurrentMaxSchedule ( ) {
		return routeDetailPos.get(routeDetailPos.size()-1);
	}
	 
}
