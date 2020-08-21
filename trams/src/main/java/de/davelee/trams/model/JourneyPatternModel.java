package de.davelee.trams.model;

import lombok.Builder;

import java.util.Calendar;
import java.util.List;

@Builder
public class JourneyPatternModel {
	
	private String name;
	private List<Integer> daysOfOperation;
	private String returnTerminus;
	private String outgoingTerminus;
	private Calendar startTime;
	private Calendar endTime;
	private int frequency;
	private int duration;
	private String routeNumber;
	private String timetableName;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public List<Integer> getDaysOfOperation() {
		return daysOfOperation;
	}

	public void setDaysOfOperation(final List<Integer> daysOfOperation) {
		this.daysOfOperation = daysOfOperation;
	}

	public String getReturnTerminus() {
		return returnTerminus;
	}

	public void setReturnTerminus(final String returnTerminus) {
		this.returnTerminus = returnTerminus;
	}

	public String getOutgoingTerminus() {
		return outgoingTerminus;
	}

	public void setOutgoingTerminus(final String outgoingTerminus) {
		this.outgoingTerminus = outgoingTerminus;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(final Calendar startTime) {
		this.startTime = startTime;
	}

	public Calendar getEndTime() {
		return endTime;
	}

	public void setEndTime(final Calendar endTime) {
		this.endTime = endTime;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(final int frequency) {
		this.frequency = frequency;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(final int duration) {
		this.duration = duration;
	}

	public String getRouteNumber() {
		return routeNumber;
	}

	public void setRouteNumber(final String routeNumber) {
		this.routeNumber = routeNumber;
	}

	public String getTimetableName() {
		return timetableName;
	}

	public void setTimetableName(final String timetableName) {
		this.timetableName = timetableName;
	}

}
