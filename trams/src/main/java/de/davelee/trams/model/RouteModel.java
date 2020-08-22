package de.davelee.trams.model;

import lombok.Builder;

import java.util.List;

@Builder
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
