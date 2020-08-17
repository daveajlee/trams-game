package de.davelee.trams.data;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;

@Entity
@Table(name="VEHICLE", uniqueConstraints=@UniqueConstraint(columnNames = {"routeNumber", "routeScheduleNumber"}))
public class Vehicle {
	
	@Id
	@GeneratedValue
	private long id;

	@Column(unique=true)
	private String registrationNumber;
	
	@Column
    private Calendar deliveryDate;

	@Column
	private String routeNumber;
	
	@Column
	private long routeScheduleNumber;
    
	@Column
    private double depreciationFactor;
	
	@Column
    private String imagePath;
	
	@Column
    private String model;
	
	@Column
    private int seatingCapacity;
	
	@Column
    private int standingCapacity;
	
	@Column
    private double purchasePrice;
    
    public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public double getDepreciationFactor() {
		return depreciationFactor;
	}

	public void setDepreciationFactor(final double depreciationFactor) {
		this.depreciationFactor = depreciationFactor;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(final String imagePath) {
		this.imagePath = imagePath;
	}

	public int getSeatingCapacity() {
		return seatingCapacity;
	}

	public void setSeatingCapacity(final int seatingCapacity) {
		this.seatingCapacity = seatingCapacity;
	}

	public int getStandingCapacity() {
		return standingCapacity;
	}

	public void setStandingCapacity(final int standingCapacity) {
		this.standingCapacity = standingCapacity;
	}

	public long getRouteScheduleNumber() {
		return routeScheduleNumber;
	}

	public void setRouteScheduleNumber(final long routeScheduleNumber) {
		this.routeScheduleNumber = routeScheduleNumber;
	}
	
	/**
     * Get the vehicle registration number. 
     * @return a <code>String</code> with the vehicle registration number.
     */
    public String getRegistrationNumber () {
        return registrationNumber;
    }

	public void setRegistrationNumber(final String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	
	public Calendar getDeliveryDate ( ) {
    	return deliveryDate;
    }

	public void setDeliveryDate(final Calendar deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	/**
     * Get the vehicle model.
     * @return a <code>String</code> with the vehicle model.
     */
    public String getModel () {
    	return model;
    }

	public void setModel(final String model) {
		this.model = model;
	}
	
	/**
     * Get the vehicle's purchase price.
     * @return a <code>double</code> with the purchase price.
     */
    public double getPurchasePrice ( ) {
    	return purchasePrice;
    }

	public void setPurchasePrice(final double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getRouteNumber() {
		return routeNumber;
	}

	public void setRouteNumber(final String routeNumber) {
		this.routeNumber = routeNumber;
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
		vehicle.setRouteNumber(this.routeNumber);
		vehicle.setRouteScheduleNumber(this.routeScheduleNumber);
    	vehicle.setSeatingCapacity(this.seatingCapacity);
    	vehicle.setStandingCapacity(this.standingCapacity);
    	return vehicle;
    }

}
