package de.davelee.trams.model;

import java.util.Calendar;

public class VehicleModel {
	
	private String imagePath;
	private String model;
	private String seatingCapacity;
	private String standingCapacity;
	private double purchasePrice;
	private Calendar deliveryDate;
	private String registrationNumber;
	private long routeScheduleId;
	private double depreciationFactor;
	
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
	
	public Calendar getDeliveryDate() {
		return deliveryDate;
	}
	
	public void setDeliveryDate(final Calendar deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	
	public void setRegistrationNumber(final String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public long getRouteScheduleId() {
		return routeScheduleId;
	}

	public void setRouteScheduleId(final long routeScheduleId) {
		this.routeScheduleId = routeScheduleId;
	}

	public double getDepreciationFactor() {
		return depreciationFactor;
	}

	public void setDepreciationFactor(double depreciationFactor) {
		this.depreciationFactor = depreciationFactor;
	}

}
