package de.davelee.trams.data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Vehicle {

	private long id;
	private String registrationNumber;
    private LocalDate deliveryDate;
	private String routeNumber;
	private long routeScheduleNumber;
    private double depreciationFactor;
    private String imagePath;
    private String model;
    private int seatingCapacity;
    private int standingCapacity;
    private double purchasePrice;

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
