package de.davelee.trams.controllers;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import de.davelee.trams.data.Journey;
import de.davelee.trams.services.JourneyService;

public class JourneyController {
	
	@Autowired
	private JourneyService journeyService;
	
	public Journey getCurrentJourney ( final long routeScheduleId, final Calendar currentTime ) {
		return journeyService.getCurrentJourney(journeyService.getJourneysByRouteScheduleId(routeScheduleId), currentTime);
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
	
}
