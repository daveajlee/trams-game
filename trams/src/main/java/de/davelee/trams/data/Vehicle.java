package de.davelee.trams.data;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="VEHICLE")
public class Vehicle {
	
	@Id
	@GeneratedValue
	@Column(name="VEHICLE_ID")
	private long id;
	
	@Column(name="REGISTRATION_NUMBER")
	private String registrationNumber;
	
	@Column(name="DELIVERY_DATE")
    private Calendar deliveryDate;
	
	@Column(name="ROUTE_SCHEDULE_ID")
    private long routeScheduleId;
    
	@Column(name="DEPRECIATION_FACTOR")
    private double depreciationFactor;
	
	@Column(name="IMAGE_PATH")
    private String imagePath;
	
	@Column(name="MODEL")
    private String model;
	
	@Column(name="SEATING_CAPACITY")
    private int seatingCapacity;
	
	@Column(name="STANDING_CAPACITY")
    private int standingCapacity;
	
	@Column(name="PURCHASE_PRICE")
    private double purchasePrice;
    
    public Vehicle() {
    }
    
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getDepreciationFactor() {
		return depreciationFactor;
	}

	public void setDepreciationFactor(double depreciationFactor) {
		this.depreciationFactor = depreciationFactor;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getSeatingCapacity() {
		return seatingCapacity;
	}

	public void setSeatingCapacity(int seatingCapacity) {
		this.seatingCapacity = seatingCapacity;
	}

	public int getStandingCapacity() {
		return standingCapacity;
	}

	public void setStandingCapacity(int standingCapacity) {
		this.standingCapacity = standingCapacity;
	}

	public long getRouteScheduleId() {
		return routeScheduleId;
	}
	
	public void setRouteScheduleId(long routeScheduleId) {
		this.routeScheduleId = routeScheduleId;
	}
	
	/**
     * Get the vehicle registration number. 
     * @return a <code>String</code> with the vehicle registration number.
     */
    public String getRegistrationNumber () {
        return registrationNumber;
    }

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	
	public Calendar getDeliveryDate ( ) {
    	return deliveryDate;
    }

	public void setDeliveryDate(Calendar deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	/**
     * Get the vehicle model.
     * @return a <code>String</code> with the vehicle model.
     */
    public String getModel () {
    	return model;
    }

	public void setModel(String model) {
		this.model = model;
	}
	
	/**
     * Get the vehicle's purchase price.
     * @return a <code>double</code> with the purchase price.
     */
    public double getPurchasePrice ( ) {
    	return purchasePrice;
    }

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
    
    /**
     * Return a String representation of this vehicle.
     * @return a <code>String</code> with the string representation of this vehicle.
     */
    public String toString () {
        return getRegistrationNumber();
    }
    
    public Vehicle clone() {
    	Vehicle vehicle = new Vehicle();
    	vehicle.setDeliveryDate(this.deliveryDate);
    	vehicle.setDepreciationFactor(this.depreciationFactor);
    	vehicle.setImagePath(this.imagePath);
    	vehicle.setModel(this.model);
    	vehicle.setPurchasePrice(this.purchasePrice);
    	vehicle.setRegistrationNumber(this.registrationNumber);
    	vehicle.setRouteScheduleId(this.routeScheduleId);
    	vehicle.setSeatingCapacity(this.seatingCapacity);
    	vehicle.setStandingCapacity(this.standingCapacity);
    	return vehicle;
    }

}
