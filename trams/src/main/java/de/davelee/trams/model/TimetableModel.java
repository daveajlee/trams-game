package de.davelee.trams.model;

import lombok.Builder;

import java.util.Calendar;

@Builder
public class TimetableModel {
	
	private String name;
	private String routeNumber;
	private Calendar validFromDate;
	private Calendar validToDate;
	
	public String getName() {
		return name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}

	public String getRouteNumber() {
		return routeNumber;
	}

	public void setRouteNumber(final String routeNumber) {
		this.routeNumber = routeNumber;
	}

	public Calendar getValidFromDate() {
		return validFromDate;
	}
	
	public void setValidFromDate(final Calendar validFromDate) {
		this.validFromDate = validFromDate;
	}
	
	public Calendar getValidToDate() {
		return validToDate;
	}
	
	public void setValidToDate(final Calendar validToDate) {
		this.validToDate = validToDate;
	}

}
