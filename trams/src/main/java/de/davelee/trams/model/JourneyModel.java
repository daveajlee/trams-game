package de.davelee.trams.model;

public class JourneyModel {

	private int journeyNumber;
	private int routeScheduleNumber;
	private String routeNumber;

	public int getJourneyNumber() {
		return journeyNumber;
	}

	public void setJourneyNumber(final int journeyNumber) {
		this.journeyNumber = journeyNumber;
	}

	public int getRouteScheduleNumber() {
		return routeScheduleNumber;
	}

	public void setRouteScheduleNumber(final int routeScheduleNumber) {
		this.routeScheduleNumber = routeScheduleNumber;
	}

	public String getRouteNumber() {
		return routeNumber;
	}

	public void setRouteNumber(final String routeNumber) {
		this.routeNumber = routeNumber;
	}

}
