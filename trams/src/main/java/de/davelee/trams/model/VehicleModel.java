package de.davelee.trams.model;

import java.time.LocalDate;

public class VehicleModel {
	
	private String imagePath;
	private String model;
	private String seatingCapacity;
	private String standingCapacity;
	private double purchasePrice;
	private LocalDate deliveryDate;
	private String registrationNumber;
	private double depreciationFactor;
	private String routeNumber;
	private long routeScheduleNumber;
	
	public String getImagePath() {
		return imagePath;
	}
	
	public void setImagePath(final String imagePath) {
		this.imagePath = imagePath;
	}
	
	public String getModel() {
		return model;
	}
	
	public void setModel(final String model) {
		this.model = model;
	}
	
	public String getSeatingCapacity() {
		return seatingCapacity;
	}
	
	public void setSeatingCapacity(final String seatingCapacity) {
		this.seatingCapacity = seatingCapacity;
	}
	
	public String getStandingCapacity() {
		return standingCapacity;
	}
	
	public void setStandingCapacity(final String standingCapacity) {
		this.standingCapacity = standingCapacity;
	}
	
	public double getPurchasePrice() {
		return purchasePrice;
	}
	
	public void setPurchasePrice(final double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	
	public LocalDate getDeliveryDate() {
		return deliveryDate;
	}
	
	public void setDeliveryDate(final LocalDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	
	public void setRegistrationNumber(final String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public double getDepreciationFactor() {
		return depreciationFactor;
	}

	public void setDepreciationFactor(double depreciationFactor) {
		this.depreciationFactor = depreciationFactor;
	}

	public String getRouteNumber() {
		return routeNumber;
	}

	public void setRouteNumber(final String routeNumber) {
		this.routeNumber = routeNumber;
	}

	public long getRouteScheduleNumber() {
		return routeScheduleNumber;
	}

	public void setRouteScheduleNumber(final long routeScheduleNumber) {
		this.routeScheduleNumber = routeScheduleNumber;
	}

}
