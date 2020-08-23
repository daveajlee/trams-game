package de.davelee.trams.model;

import lombok.Builder;

import java.time.LocalTime;

@Builder
public class StopTimeModel {
	
	private int journeyNumber;
	private String routeNumber;
	private int routeScheduleNumber;
	private String stopName;
	private LocalTime time;
	
	public int getJourneyNumber() {
		return journeyNumber;
	}
	
	public void setJourneyNumber(final int journeyNumber) {
		this.journeyNumber = journeyNumber;
	}

	public String getRouteNumber() {
		return routeNumber;
	}

	public void setRouteNumber(final String routeNumber) {
		this.routeNumber = routeNumber;
	}

	public int getRouteScheduleNumber() {
		return routeScheduleNumber;
	}

	public void setRouteScheduleNumber(final int routeScheduleNumber) {
		this.routeScheduleNumber = routeScheduleNumber;
	}
	
	public String getStopName() {
		return stopName;
	}
	
	public void setStopName(final String stopName) {
		this.stopName = stopName;
	}
	
	public LocalTime getTime() {
		return time;
	}
	
	public void setTime(final LocalTime time) {
		this.time = time;
	}

}
