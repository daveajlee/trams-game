package de.davelee.trams.model;

import java.util.Calendar;
import java.util.List;

public class JourneyPatternModel {
	
	private String name;
	private List<Integer> daysOfOperation;
	private String returnTerminus;
	private String outgoingTerminus;
	public Calendar startTime;
	public Calendar endTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Integer> getDaysOfOperation() {
		return daysOfOperation;
	}

	public void setDaysOfOperation(List<Integer> daysOfOperation) {
		this.daysOfOperation = daysOfOperation;
	}

	public String getReturnTerminus() {
		return returnTerminus;
	}

	public void setReturnTerminus(String returnTerminus) {
		this.returnTerminus = returnTerminus;
	}

	public String getOutgoingTerminus() {
		return outgoingTerminus;
	}

	public void setOutgoingTerminus(String outgoingTerminus) {
		this.outgoingTerminus = outgoingTerminus;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}

	public Calendar getEndTime() {
		return endTime;
	}

	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}

}