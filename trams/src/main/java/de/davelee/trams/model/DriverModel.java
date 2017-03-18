package de.davelee.trams.model;

import java.util.Calendar;

public class DriverModel {
	
	private String name;
	private int contractedHours;
	private Calendar startDate;
	
	public String getName() {
		return name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public int getContractedHours() {
		return contractedHours;
	}
	
	public void setContractedHours(final int contractedHours) {
		this.contractedHours = contractedHours;
	}
	
	public Calendar getStartDate() {
		return startDate;
	}
	
	public void setStartDate(final Calendar startDate) {
		this.startDate = startDate;
	}

}
