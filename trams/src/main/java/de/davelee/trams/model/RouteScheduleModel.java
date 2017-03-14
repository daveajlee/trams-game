package de.davelee.trams.model;

public class RouteScheduleModel {
	
	private int delay;
	private String image;
	private String registrationNumber;
	
	public int getDelay() {
		return delay;
	}
	
	public void setDelay(final int delay) {
		this.delay = delay;
	}
	
	public String getImage() {
		return image;
	}
	
	public void setImage(final String image) {
		this.image = image;
	}
	
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	
	public void setRegistrationNumber(final String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}	

}
