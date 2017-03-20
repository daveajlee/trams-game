package de.davelee.trams.model;

public class RouteScheduleModel {
	
	private int delay;
	private int scheduleNumber;
	private String routeNumber;
	
	public int getDelay() {
		return delay;
	}
	
	public void setDelay(final int delay) {
		this.delay = delay;
	}

	public int getScheduleNumber() {
		return scheduleNumber;
	}

	public void setScheduleNumber(final int scheduleNumber) {
		this.scheduleNumber = scheduleNumber;
	}

	public String getRouteNumber() {
		return routeNumber;
	}

	public void setRouteNumber(final String routeNumber) {
		this.routeNumber = routeNumber;
	}

}
