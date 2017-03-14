package de.davelee.trams.controllers;

import java.util.Calendar;
import java.util.List;

import de.davelee.trams.data.StopTime;
import de.davelee.trams.util.DateFormats;
import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.data.Journey;
import de.davelee.trams.services.JourneyService;

public class JourneyController {
	
	@Autowired
	private JourneyService journeyService;
	
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
    public String getDisplayStopTime( long journeyId, String name ) {
		return DateFormats.HOUR_MINUTE_FORMAT.getFormat().format(getStopTime(journeyId, name));
	}

	//TODO: Null or exception?
	public Calendar getStopTime ( long journeyId, String name ) {
		StopTime stopTime = journeyService.getStopTime(journeyId, name);
		if ( stopTime != null ) {
			return stopTime.getTime();
		}
		return null;
	}
	
}
