package de.davelee.trams.controllers;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import de.davelee.trams.model.*;
import de.davelee.trams.util.DateFormats;
import de.davelee.trams.util.SortedJourneyModels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.services.JourneyService;
import org.springframework.stereotype.Controller;

@Controller
public class JourneyController {

	private static final Logger logger = LoggerFactory.getLogger(JourneyController.class);
	
	@Autowired
	private JourneyService journeyService;

	@Autowired
	private TimetableController timetableController;

	@Autowired
	private JourneyPatternController journeyPatternController;


	public JourneyModel getCurrentJourney (final RouteScheduleModel routeScheduleModel, final Calendar currentTime ) {
		return journeyService.getCurrentJourney(journeyService.getJourneysByRouteScheduleNumberAndRouteNumber(routeScheduleModel.getScheduleNumber(), routeScheduleModel.getRouteNumber()), currentTime);
	}

	public JourneyModel getNextJourney ( final RouteScheduleModel routeScheduleModel, final Calendar currentTime ) {
		return journeyService.getNextJourney(journeyService.getJourneysByRouteScheduleNumberAndRouteNumber(routeScheduleModel.getScheduleNumber(), routeScheduleModel.getRouteNumber()), currentTime);
	}

	public boolean isOutwardJourney ( final RouteScheduleModel routeScheduleModel, final Calendar currentTime, final List<String> stops ) {
		return journeyService.isOutwardJourney(getCurrentJourney(routeScheduleModel, currentTime), stops);
	}

	public long getStopMaxTimeDiff ( final RouteScheduleModel routeScheduleModel, final Calendar currentTime, final String prevStopName, final String thisStopName ) {
		return journeyService.getStopMaxTimeDiff(getCurrentJourney(routeScheduleModel, currentTime), prevStopName, thisStopName);
	}

	public int getNumStopTimes ( final RouteScheduleModel routeScheduleModel, final Calendar currentTime ) {
		return journeyService.getStopTimesByJourneyNumber(getCurrentJourney(routeScheduleModel, currentTime).getJourneyNumber()).length;
	}

	public String getStopName ( final RouteScheduleModel routeScheduleModel, final Calendar currentTime, final int pos ) {
		return journeyService.getStopTimesByJourneyNumber(getCurrentJourney(routeScheduleModel, currentTime).getJourneyNumber())[pos].getStopName();
	}

	public String getStopName ( final RouteScheduleModel routeScheduleModel, final Calendar currentTime ) {
		return journeyService.getCurrentStopName(journeyService.getJourneysByRouteScheduleNumberAndRouteNumber(routeScheduleModel.getScheduleNumber(), routeScheduleModel.getRouteNumber()), currentTime);
	}

	public String getLastStopName ( final RouteScheduleModel routeScheduleModel, final Calendar currentTime ) {
		return journeyService.getLastStopName(journeyService.getJourneysByRouteScheduleNumberAndRouteNumber(routeScheduleModel.getScheduleNumber(), routeScheduleModel.getRouteNumber()), currentTime);
	}

	public long removeStopsFromCurrentJourney ( final RouteScheduleModel routeScheduleModel, final Calendar currentTime, final String stop, final String oldEnd ) {
		return journeyService.removeStopsBetween(getCurrentJourney(routeScheduleModel, currentTime), stop, oldEnd, false, true);
	}

	public long removeStopsFromNextJourney ( final RouteScheduleModel routeScheduleModel, final Calendar currentTime, final String stop, final String oldEnd ) {
		return journeyService.removeStopsBetween(getNextJourney(routeScheduleModel, currentTime), oldEnd, stop, false, true);
	}

	/**
	 * Get the stop time as hh:mm.
	 * @param journeyModel a <code>JourneyModel</code> object with details of the journey.
	 * @param name a <code>String</code> with the name of the stop to return the time for.
	 * @return a <code>String</code> with the time as hh:mm.
	 */
    public String getDisplayStopTime( final JourneyModel journeyModel, String name ) {
		return DateFormats.HOUR_MINUTE_FORMAT.getFormat().format(getStopTime(journeyModel, name));
	}

	//TODO: Null or exception?
	public Calendar getStopTime ( final JourneyModel journeyModel, String name ) {
		StopTimeModel stopTime = journeyService.getStopTime(journeyModel, name);
		if ( stopTime != null ) {
			return stopTime.getTime();
		}
		return null;
	}

	public Calendar getFirstStopTime ( final JourneyModel journeyModel ) {
		return journeyService.getStopTimesByJourneyNumber(journeyModel.getJourneyNumber())[0].getTime();
	}

	public Calendar getLastStopTime ( final JourneyModel journeyModel ) {
		int position = journeyService.getStopTimesByJourneyNumber(journeyModel.getJourneyNumber()).length -1;
		return journeyService.getStopTimesByJourneyNumber(journeyModel.getJourneyNumber())[position].getTime();
	}

	public void assignRouteSchedule ( final JourneyModel journeyModel, final RouteScheduleModel routeScheduleModel ) {
		journeyService.assignRouteAndRouteSchedule(journeyModel, routeScheduleModel);
	}

	public JourneyModel[] getAllJourneys ( ) {
        return journeyService.getAllJourneys();
    }

	public JourneyModel[] getJourneysByRouteScheduleNumberAndRouteNumber ( final int routeScheduleNumber, final String routeNumber ) {
		return journeyService.getJourneysByRouteScheduleNumberAndRouteNumber(routeScheduleNumber, routeNumber);
	}

	public String[] getAllStops ( ) {
        return journeyService.getAllStops();
    }

	public StopTimeModel[] getAllStopTimes ( ) {
        return journeyService.getAllStopTimes();
    }

	/**
	 * This method generates the route timetables for a particular day - it is a very important method.
	 * @param routeModel a <code>RouteModel</code> object containing the details of the route to generate timetables for.
	 * @param today a <code>Calendar</code> object with today's date.
	 * @param scenarioName a <code>String</code> object with the name of the scenario to generate timeables for.
	 * @param direction a <code>int</code> which determines the direction to generate timetables for.
	 * @return a <code>JourneyModel</code> list of objects representing the timetables generated.
	 */
	public List<JourneyModel> generateJourneyTimetables ( final RouteModel routeModel, final Calendar today, final String scenarioName, final int direction ) {
		logger.debug("I'm generating timetable for route number " + routeModel.getRouteNumber() + " for " + DateFormats.DAY_MONTH_YEAR_FORMAT.getFormat().format(today.getTime()));
		//First of all, get the current timetable.
		TimetableModel currentTimetableModel = timetableController.getCurrentTimetable(routeModel, today);
		JourneyPatternModel[] journeyPatternModels = journeyPatternController.getJourneyPatternModels(currentTimetableModel, routeModel.getRouteNumber());
		//Generate journeys.
		List<JourneyModel> journeyModels = journeyService.generateJourneyTimetables(journeyPatternModels, today, direction, routeModel.getStopNames(), scenarioName);
		//Sort all journeys.
		Collections.sort(journeyModels, new SortedJourneyModels());
		//Return the journeys.
		return journeyModels;
	}

	public int getDistance ( final String scenarioName, final String stop1, final String stop2 ) {
		return journeyService.getDistance(scenarioName, stop1, stop2);
	}

	/**
	 * Load Journeys.
	 * @param journeyModels an array of <code>JourneyModel</code> objects to store and delete all other journeys.
	 */
	public void loadJourneys ( final JourneyModel[] journeyModels ) {
		journeyService.deleteAllJourneys();
		for ( JourneyModel journeyModel : journeyModels ) {
			journeyService.saveJourney(journeyModel);
		}
	}

	/**
	 * Load Stops.
	 * @param stopModels an array of <code>String</code> objects with stops to store and delete all other stops.
	 */
	public void loadStops ( final String[] stopModels ) {
		journeyService.deleteAllStops();
		for ( String stopModel : stopModels ) {
			journeyService.saveStop(stopModel);
		}
	}

	/**
	 * Load Stop Times.
	 * @param stopTimeModels an array of <code>StopTimeModel</code> objects to store and delete all other stop times.
	 */
	public void loadStopTimes ( final StopTimeModel[] stopTimeModels ) {
		journeyService.deleteAllStopTimes();
		for ( StopTimeModel stopTimeModel : stopTimeModels ) {
			journeyService.saveStopTime(stopTimeModel);
		}
	}
	
}
