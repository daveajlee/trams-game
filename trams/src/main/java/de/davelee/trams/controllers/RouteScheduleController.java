package de.davelee.trams.controllers;

import de.davelee.trams.model.GameModel;
import de.davelee.trams.model.JourneyModel;
import de.davelee.trams.model.RouteModel;
import de.davelee.trams.model.RouteScheduleModel;
import de.davelee.trams.util.DifficultyLevel;
import de.davelee.trams.util.TramsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.RouteScheduleService;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Controller
public class RouteScheduleController {

	private static final Logger logger = LoggerFactory.getLogger(RouteScheduleController.class);
	
	 @Autowired
	 private RouteScheduleService routeScheduleService;

	 @Autowired
	 private JourneyController journeyController;

	 @Autowired
	 private VehicleController vehicleController;

	 @Autowired
	 private GameController gameController;

	 private List<Integer> routeDetailPos = new ArrayList<Integer>();

	/**
	 * Get the current stop name which this route schedule is on based on the current date.
	 * @param routeScheduleModel a <code>RouteScheduleModel</code> object representing the route schedules.
	 * @param currentDateTime a <code>LocalDateTime</code> object.
	 * @param difficultyLevel a <code>DifficultyLevel</code> object representing the difficulty level.
	 * @return a <code>String</code> array with the stop details.
	 */
	public String getCurrentStopName (final RouteScheduleModel routeScheduleModel, final LocalDateTime currentDateTime, final DifficultyLevel difficultyLevel ) {
		//Copy current Date to current Time and then use delay to determine position.
		currentDateTime.minusMinutes(routeScheduleModel.getDelay());
		String stopName = journeyController.getStopName(routeScheduleModel, currentDateTime.toLocalTime());
		if ( !stopName.contentEquals("Depot") ) {
			//Now fiddle delay!
			routeScheduleService.calculateNewDelay(routeScheduleModel, difficultyLevel);
		}
		return stopName;
	}

	public String getLastStopName ( final RouteScheduleModel routeScheduleModel, LocalDateTime currentDateTime, DifficultyLevel difficultyLevel) {
		//Copy current Date to current Time and then use delay to determine position.
		currentDateTime.minusMinutes(routeScheduleModel.getDelay());
		String stopName = journeyController.getLastStopName(routeScheduleModel, currentDateTime.toLocalTime());
		if ( stopName.contentEquals("Depot")) {
			routeScheduleService.reduceDelay(routeScheduleModel, routeScheduleModel.getDelay()); //Finished for the day.
		}
		else {
			//Now fiddle delay!
			routeScheduleService.calculateNewDelay(routeScheduleModel, difficultyLevel);
		}
		return stopName;
	}

	public RouteScheduleModel[] getRouteSchedulesByRouteNumber ( final String routeNumber ) {
		return routeScheduleService.getRouteSchedulesByRouteNumber(routeNumber);
	}


	/**
	 * Shorten schedule to the specific stop stated and reduce the delay accordingly.
	 * @param routeScheduleModel a <code>RouteSchedule</code> representing the route schedules.
	 * @param stop a <code>String</code> with the stop to terminate at.
	 * @param currentTime a <code>LocalTime</code> object with the current time.
	 */
	public void shortenSchedule ( final RouteScheduleModel routeScheduleModel, final String stop, final LocalTime currentTime ) {
		//Shorten schedule to the specific stop stated and reduce the delay accordingly - for current service remove stops after the specified stop.
		//logger.debug("Service was ending at: " + theAssignedSchedule.getCurrentService().getEndDestination());
		String oldEnd = journeyController.getLastStopName(routeScheduleModel, currentTime);
		//Now we need to remove the stops in beteen!
		long timeDiff = journeyController.removeStopsFromCurrentJourney(routeScheduleModel, currentTime, stop, oldEnd);
		//Now for the next service we need to remove stops between first stop and stop.
		long timeDiff2 = journeyController.removeStopsFromNextJourney(routeScheduleModel, currentTime, stop, oldEnd);
		//Divide both timeDiff's by 60 to convert to minutes and then use that to reduce vehicle delay.
		long delayReduction = (timeDiff/60) + (timeDiff2/60);
		//Reduce delay!
		routeScheduleService.reduceDelay(routeScheduleModel, (int) delayReduction);
	}

	/**
	 * Put this vehicle out of service from the current stop until the new stop.
	 * @param routeScheduleModel a <code>RouteSchedule</code> representing the route schedules.
	 * @param currentStop a <code>String</code> with the stop to go out of service from.
	 * @param newStop a <code>String</code> with the stop to resume service from.
	 * @param currentTime a <code>LocalTime</code> object with the current time.
	 */
	public void outOfService ( final RouteScheduleModel routeScheduleModel, final String currentStop, final String newStop, final LocalTime currentTime ) {
		//Get the time difference between current stop and new stop.
		long timeDiff = journeyController.getStopMaxTimeDiff(routeScheduleModel, currentTime, currentStop, newStop);
		routeScheduleService.reduceDelay(routeScheduleModel, (int) (timeDiff/2));
		//logger.debug("Vehicle delay reduced from " + oldDelay + " mins to " + getVehicleDelay() + " mins.");
	}

	public RouteScheduleModel[] getAllRouteSchedules ( ) {
		return routeScheduleService.getAllRouteSchedules();
	}

	/**
	 * Set the minimum and maximum display of vehicles.
	 * @param min a <code>int</code> with the minimum.
	 * @param max a <code>int</code> with the maximum.
	 * @param routeNumber a <code>String</code> with the route number.
	 * @return a <code>int</code> with the maximum display of vehicles.
	 */
	public int setCurrentDisplayMinMax ( final int min, final int max, final String routeNumber ) {
		int returnMax = max;
		//Clear the original matrix for which routes to display.
		routeDetailPos = new LinkedList<Integer>();
		//Store the currentDate - we will need it for display schedules.
		GameModel gameModel = gameController.getGameModel();
		//Determine the route ids we will display using these parameters.
		logger.debug("Route number is " + routeNumber);
		RouteScheduleModel[] routeScheduleModels = routeScheduleService.getRouteSchedulesByRouteNumber(routeNumber);
		logger.debug("Number of possible display schedules: " +  routeScheduleModels.length);
		if ( routeScheduleModels.length < max ) { returnMax = routeScheduleModels.length; }
		//logger.debug("Max vehicles starts at " + max + " - routeDetails size is " + routeDetails.size());
		logger.debug("Min is " + min + " & Max is " + max);
		if ( min == max ) {
			if ( vehicleController.getVehicleByRouteNumberAndRouteScheduleNumber(routeNumber, "" + routeScheduleModels[min].getScheduleNumber()) == null ) {
				logger.debug("A schedule was null");
			}
			if ( getCurrentStopName(getRouteSchedulesByRouteNumber(routeNumber)[min], gameModel.getCurrentDateTime(), gameModel.getDifficultyLevel()).equalsIgnoreCase("Depot") ) {
				logger.debug("Vehicle in depot!");
			}
			if ( vehicleController.getVehicleByRouteNumberAndRouteScheduleNumber(routeNumber, "" + routeScheduleModels[min].getScheduleNumber()) != null && !getCurrentStopName( getRouteSchedulesByRouteNumber(routeNumber)[min], gameModel.getCurrentDateTime(), gameModel.getDifficultyLevel()).equalsIgnoreCase("Depot") ) {
				//logger.debug("Adding Route Detail " + routeDetails.get(i).getId());
				routeDetailPos.add(0);
			}
			else {
				returnMax++;
				//logger.debug("Max is now " + max + " - routeDetails size is: " + routeDetails.size());
				if ( routeScheduleModels.length < max ) { returnMax = routeScheduleModels.length; }
				//logger.debug("Route Detail " + routeDetails.get(i).getId() + " was null - maxVehicles is now " + max);
			}
		}
		for ( int i = 0; i < routeScheduleModels.length; i++ ) { //Changed from i = 0; i < routeDetails.size().
			if ( vehicleController.getVehicleByRouteNumberAndRouteScheduleNumber(routeNumber, "" + routeScheduleModels[i].getScheduleNumber()) == null ) {
				logger.debug("A schedule was null");
			}
			if ( getCurrentStopName(getRouteSchedulesByRouteNumber(routeNumber)[i], gameModel.getCurrentDateTime(), gameModel.getDifficultyLevel()).equalsIgnoreCase("Depot") ) {
				logger.debug("Vehicle in depot!");
			}
			if ( vehicleController.getVehicleByRouteNumberAndRouteScheduleNumber(routeNumber, "" + routeScheduleModels[i].getScheduleNumber()) != null && !getCurrentStopName(getRouteSchedulesByRouteNumber(routeNumber)[i], gameModel.getCurrentDateTime(), gameModel.getDifficultyLevel()).equalsIgnoreCase("Depot") ) {
				//logger.debug("Adding Route Detail " + routeDetails.get(i).getId());
				routeDetailPos.add(i);
			}
			else {
				returnMax++;
				//logger.debug("Max is now " + max + " - routeDetails size is: " + routeDetails.size());
				if ( routeScheduleModels.length < max ) { returnMax = routeScheduleModels.length; }
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


	public List<RouteScheduleModel> generateRouteSchedules (final RouteModel routeModel, final LocalDate today, final String scenarioName ) {
		//Initialise list.
		List<RouteScheduleModel> routeScheduleModels = new ArrayList<RouteScheduleModel>();
		//Initialise parameters.
		List<JourneyModel> outgoingJourneyModels = journeyController.generateJourneyTimetables(routeModel, today, scenarioName, TramsConstants.OUTWARD_DIRECTION, 0);
		List<JourneyModel> returnJourneyModels = journeyController.generateJourneyTimetables(routeModel, today, scenarioName, TramsConstants.RETURN_DIRECTION, (outgoingJourneyModels.get(outgoingJourneyModels.size()-1).getJourneyNumber()+1));
		//We need to repeat this loop until both outgoingJourneys and returnJourneys are empty!
		int counter = 1;
		while ( outgoingJourneyModels.size() > 0 || returnJourneyModels.size() > 0 ) {
			//Control what journey we want - initially we don't care.
			boolean wantOutgoing = true; boolean wantReturn = true;
			//Create a new route schedule.
			RouteScheduleModel mySchedule = RouteScheduleModel.builder()
					.scheduleNumber(counter)
					.routeNumber(routeModel.getRouteNumber())
					.build();
			//Create our local time object and set it to midnight.
			LocalTime myTime = journeyController.getFirstStopTime(outgoingJourneyModels.get(0));
			//Find whether the first outgoing journey time is before the first return journey time.
			if ( returnJourneyModels.size() > 0 && outgoingJourneyModels.size() > 0 && journeyController.getFirstStopTime(outgoingJourneyModels.get(0)).isAfter(journeyController.getFirstStopTime(returnJourneyModels.get(0))) ) {
				myTime = journeyController.getFirstStopTime(returnJourneyModels.get(0));
			}
			else if ( outgoingJourneyModels.size() == 0 ) {
				myTime = journeyController.getFirstStopTime(returnJourneyModels.get(0));
			}
			//Here's the loop.
			while ( true ) {
				//logger.debug("Schedule " + counter + " Time is now " + myCal.get(Calendar.HOUR_OF_DAY) + ":" + myCal.get(Calendar.MINUTE));
				if ( outgoingJourneyModels.size() > 0 && returnJourneyModels.size() > 0 ) {
					if ( myTime.isAfter(journeyController.getFirstStopTime(outgoingJourneyModels.get(outgoingJourneyModels.size()-1))) && myTime.isAfter(journeyController.getFirstStopTime(returnJourneyModels.get(returnJourneyModels.size()-1)))) {
						break;
					}
				} else if ( outgoingJourneyModels.size() > 0 ) {
					if ( myTime.isAfter(journeyController.getFirstStopTime(outgoingJourneyModels.get(outgoingJourneyModels.size()-1))) ) {
						break;
					}
				} else if ( returnJourneyModels.size() > 0 ) {
					if ( myTime.isAfter(journeyController.getFirstStopTime(returnJourneyModels.get(returnJourneyModels.size()-1))) ) {
						break;
					}
				} else {
					break; //Both outgoing journeys and return journeys were 0 so finished.
				}
				int loopPos = 0;
				while ( true ) {
					if ( loopPos >= outgoingJourneyModels.size() && loopPos >= returnJourneyModels.size() ) { break; }
					if ( loopPos < outgoingJourneyModels.size() && wantOutgoing && journeyController.getFirstStopTime(outgoingJourneyModels.get(loopPos)).compareTo(myTime) == 0) {
							//logger.debug("Adding service " + outgoingServices.get(loopPos).getAllDisplayStops() + " to route schedule " + counter);
							//We have found our journey - its an outgoing one!!!
							journeyController.assignRouteSchedule(outgoingJourneyModels.get(loopPos),mySchedule);
							//Set time equal to last stop time.
							myTime = journeyController.getLastStopTime(outgoingJourneyModels.get(loopPos));
							//Remove this journey from the list.
							outgoingJourneyModels.remove(loopPos);
							//Note that we next want a return one.
							wantOutgoing = false; wantReturn = true;
							//Continue loop.
							continue;
					}
					if ( loopPos < returnJourneyModels.size() && wantReturn && journeyController.getFirstStopTime(returnJourneyModels.get(loopPos)).compareTo(myTime) == 0) {
							//logger.debug("Adding service " + returnServices.get(loopPos).getAllDisplayStops() + " to route schedule " + counter);
							//We have found our journey - its a return one!!!
							journeyController.assignRouteSchedule(returnJourneyModels.get(loopPos), mySchedule);
							//Set time equal to last stop time.
							myTime = journeyController.getLastStopTime(returnJourneyModels.get(loopPos));
							//Remove this journey from the list.
							returnJourneyModels.remove(loopPos);
							//Note that we next want an outgoing one.
							wantReturn = false; wantOutgoing = true;
							//Continue loop.
							continue;
					}
					//Increment loopPos.
					loopPos++;
				}
				//Increment minutes and loopPos.
				myTime = myTime.plusMinutes(1);
			}
			//Add route schedule to database.
			routeScheduleService.saveRouteSchedule(mySchedule);
			routeScheduleModels.add(mySchedule);
			//Increment counter.
			counter++;
		}
		return routeScheduleModels;
	}

	/**
	 * Get list of today's allocations.
	 * @param currentDate a <code>String</code> with the current date.
	 * @return a <code>LinkedList</code> of allocations.
	 */
	public List<String> getTodayAllocations ( final String currentDate ) {
		return vehicleController.getAllocations();
	}

	/**
	 * Load Route Schedules.
	 * @param routeScheduleModels an array of <code>RouteScheduleModel</code> objects with route schedules to store and delete all other route schedules.
	 */
	public void loadRouteSchedules ( final RouteScheduleModel[] routeScheduleModels ) {
		routeScheduleService.deleteAllRouteSchedules();
		for ( RouteScheduleModel routeScheduleModel : routeScheduleModels ) {
			routeScheduleService.saveRouteSchedule(routeScheduleModel);
		}
	}
	 
}
