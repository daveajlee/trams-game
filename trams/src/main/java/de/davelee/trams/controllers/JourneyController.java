package de.davelee.trams.controllers;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import de.davelee.trams.data.Stop;
import de.davelee.trams.data.StopTime;
import de.davelee.trams.model.JourneyModel;
import de.davelee.trams.model.JourneyPatternModel;
import de.davelee.trams.model.RouteModel;
import de.davelee.trams.model.TimetableModel;
import de.davelee.trams.util.DateFormats;
import de.davelee.trams.util.SortedJourneyModels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.data.Journey;
import de.davelee.trams.services.JourneyService;

public class JourneyController {

	private static final Logger logger = LoggerFactory.getLogger(JourneyController.class);
	
	@Autowired
	private JourneyService journeyService;

	@Autowired
	private TimetableController timetableController;

	@Autowired
	private JourneyPatternController journeyPatternController;

	
	public Journey getCurrentJourney ( final long routeScheduleId, final Calendar currentTime ) {
		return journeyService.getCurrentJourney(journeyService.getJourneysByRouteScheduleId(routeScheduleId), currentTime);
	}

	public Journey getNextJourney ( final long routeScheduleId, final Calendar currentTime ) {
		return journeyService.getNextJourney(journeyService.getJourneysByRouteScheduleId(routeScheduleId), currentTime);
	}

	public boolean isOutwardJourney ( final long routeScheduleId, final Calendar currentTime, final List<String> stops ) {
		return journeyService.isOutwardJourney(getCurrentJourney(routeScheduleId, currentTime), stops);
	}
	
	public long getStopMaxTimeDiff ( final long routeScheduleId, final Calendar currentTime, final String prevStopName, final String thisStopName ) {
		return journeyService.getStopMaxTimeDiff(getCurrentJourney(routeScheduleId, currentTime), prevStopName, thisStopName);
	}
	
	public int getNumStopTimes ( final long routeScheduleId, final Calendar currentTime ) {
		return journeyService.getStopTimesByJourneyId(getCurrentJourney(routeScheduleId, currentTime).getId()).size();
	}
	
	public String getStopName ( final long routeScheduleId, final Calendar currentTime, final int pos ) {
		return journeyService.getStopNameByStopId(journeyService.getStopTimesByJourneyId(getCurrentJourney(routeScheduleId, currentTime).getId()).get(pos).getStopId());
	}

	public String getStopName ( final long routeScheduleId, final Calendar currentTime ) {
		return journeyService.getCurrentStopName(journeyService.getJourneysByRouteScheduleId(routeScheduleId), currentTime);
	}

	public String getLastStopName ( final long routeScheduleId, final Calendar currentTime ) {
		return journeyService.getLastStopName(journeyService.getJourneysByRouteScheduleId(routeScheduleId), currentTime);
	}

	public long removeStopsFromCurrentJourney ( final long routeScheduleId, final Calendar currentTime, final String stop, final String oldEnd ) {
		return journeyService.removeStopsBetween(getCurrentJourney(routeScheduleId, currentTime), stop, oldEnd, false, true);
	}

	public long removeStopsFromNextJourney ( final long routeScheduleId, final Calendar currentTime, final String stop, final String oldEnd ) {
		return journeyService.removeStopsBetween(getNextJourney(routeScheduleId, currentTime), oldEnd, stop, false, true);
	}

	/**
	 * Get the stop time as hh:mm.
	 * @return a <code>String</code> with the time as hh:mm.
	 */
    public String getDisplayStopTime( final JourneyModel journeyModel, String name ) {
		return DateFormats.HOUR_MINUTE_FORMAT.getFormat().format(getStopTime(journeyModel, name));
	}

	//TODO: Null or exception?
	public Calendar getStopTime ( final JourneyModel journeyModel, String name ) {
		final long journeyId = Long.parseLong(journeyModel.getJourneyName().split("-")[0].substring(1));
		StopTime stopTime = journeyService.getStopTime(journeyId, name);
		if ( stopTime != null ) {
			return stopTime.getTime();
		}
		return null;
	}

	public Calendar getFirstStopTime ( final JourneyModel journeyModel ) {
		final long journeyId = Long.parseLong(journeyModel.getJourneyName().split("-")[0].substring(1));
		return journeyService.getStopTimesByJourneyId(journeyId).get(0).getTime();
	}

	public Calendar getLastStopTime ( final JourneyModel journeyModel ) {
		final long journeyId = Long.parseLong(journeyModel.getJourneyName().split("-")[0].substring(1));
		int position = journeyService.getStopTimesByJourneyId(journeyId).size() -1;
		return journeyService.getStopTimesByJourneyId(journeyId).get(position).getTime();
	}

	public void assignRouteSchedule ( final JourneyModel journeyModel, final long routeScheduleId ) {
		final long journeyId = Long.parseLong(journeyModel.getJourneyName().split("-")[0].substring(1));
		journeyService.getJourneyById(journeyId).setRouteScheduleId(routeScheduleId);
	}

    public List<Journey> getAllJourneys ( ) {
        return journeyService.getAllJourneys();
    }

    public List<Stop> getAllStops ( ) {
        return journeyService.getAllStops();
    }

    public List<StopTime> getAllStopTimes ( ) {
        return journeyService.getAllStopTimes();
    }

	/**
	 * This method generates the route timetables for a particular day - it is a very important method.
	 * @param today a <code>Calendar</code> object with today's date.
	 */
    public List<JourneyModel> generateJourneyTimetables (final RouteModel routeModel, Calendar today, String scenarioName, int direction ) {
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
	
}
