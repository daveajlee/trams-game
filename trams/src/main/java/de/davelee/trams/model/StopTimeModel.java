package de.davelee.trams.model;

import java.util.Calendar;

public class StopTimeModel {
	
	private int journeyNumber;
	private String stopName;
	private Calendar time;
	
	public int getJourneyNumber() {
		return journeyNumber;
	}
	
	public void setJourneyNumber(final int journeyNumber) {
		this.journeyNumber = journeyNumber;
	}
	
	public String getStopName() {
		return stopName;
	}
	
	public void setStopName(final String stopName) {
		this.stopName = stopName;
	}
	
	public Calendar getTime() {
		return time;
	}
	
	public void setTime(final Calendar time) {
		this.time = time;
	}

}
