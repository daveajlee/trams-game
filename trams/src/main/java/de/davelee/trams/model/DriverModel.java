package de.davelee.trams.model;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public class DriverModel {
	
	private String name;
	private int contractedHours;
	private LocalDate startDate;
	
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
	
	public LocalDate getStartDate() {
		return startDate;
	}
	
	public void setStartDate(final LocalDate startDate) {
		this.startDate = startDate;
	}

}
