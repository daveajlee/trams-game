package de.davelee.trams.model;

import java.util.List;

public class RouteModel {
	
	private String routeNumber;
	private List<String> stopNames;

	public String getRouteNumber() {
		return routeNumber;
	}

	public void setRouteNumber(String routeNumber) {
		this.routeNumber = routeNumber;
	}

	public List<String> getStopNames() {
		return stopNames;
	}

	public void setStopNames(List<String> stopNames) {
		this.stopNames = stopNames;
	}

}
