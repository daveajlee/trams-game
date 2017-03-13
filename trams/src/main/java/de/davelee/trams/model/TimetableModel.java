package de.davelee.trams.model;

import java.util.Calendar;

public class TimetableModel {
	
	private String name;
	private Calendar validFromDate;
	private Calendar validToDate;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
