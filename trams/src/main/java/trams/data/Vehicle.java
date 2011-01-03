package trams.data;

import java.util.Calendar;

import trams.main.MyCalendarUtils;
import trams.simulation.Simulator;


public class Vehicle {
	
	private int id;
	private String registrationNumber;
    private Calendar deliveryDate;
    private RouteSchedule assignedSchedule;
    
    private double depreciationFactor;
    private String imagePath;
    private String model;
    private int seatingNum;
    private int standingNum;
    private double purchasePrice;
    
    public Vehicle() {
    	
    }
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public int getSeatingNum() {
		return seatingNum;
	}

	public void setSeatingNum(int seatingNum) {
		this.seatingNum = seatingNum;
	}

	public int getStandingNum() {
		return standingNum;
	}

	public void setStandingNum(int standingNum) {
		this.standingNum = standingNum;
	}

	public RouteSchedule getAssignedSchedule() {
		return assignedSchedule;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public void setDeliveryDate(Calendar deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	/**
     * Default constructor to create a new vehicle. 
     * @param id a <code>String</code> with the vehicle id.
     * @param purchaseDate a <code>Calendar</code> with the purchase date.
     */
    public Vehicle ( String registrationNumber, Calendar purchaseDate, String imagePath, String model, int seatingNum, int standingNum, double purchasePrice ) {
        this.registrationNumber = registrationNumber;
        deliveryDate = (Calendar) purchaseDate.clone();
        assignedSchedule = null;
        this.imagePath = imagePath;
        this.model = model;
        this.seatingNum = seatingNum;
        this.standingNum = standingNum;
        this.purchasePrice = purchasePrice;
        this.depreciationFactor = 0.006;
    }
    
    /**
     * Get the vehicle registration number. 
     * @return a <code>String</code> with the vehicle registration number.
     */
    public String getRegistrationNumber () {
        return registrationNumber;
    }
    
    /**
     * Get the vehicle model.
     * @return a <code>String</code> with the vehicle model.
     */
    public String getModel () {
    	return model;
    }
    
    /**
     * Get the vehicle's current delay. 
     * @return a <code>int</code> with the vehicle delay.
     */
    public int getDelay () {
        return assignedSchedule.getDelayInMins();
    }
    
    /**
     * Check if the vehicle has been delivered yet!
     * @param currentDate a <code>Calendar</code> object with the currentDate.
     * @return a <code>boolean</code> which is true iff the vehicle has been delivered.
     */
    public boolean hasBeenDelivered ( Calendar currentDate ) {
    	if ( MyCalendarUtils.getDiff(currentDate, deliveryDate) > 0 ) {
    		return true;
    	}
    	return false;
    }
    
    /**
     * Get the vehicle's purchase price.
     * @return a <code>double</code> with the purchase price.
     */
    public double getPurchasePrice ( ) {
    	return purchasePrice;
    }
    
    /**
     * Get the vehicle's value after taking depreciation into account.
     * @param currentDate a <code>Calendar</code> object with the currentDate.
     * @return a <code>double</code> with the value.
     */
    public double getValue ( Calendar currentDate ) {
        return getPurchasePrice() - ((depreciationFactor * getAge(currentDate)) * getPurchasePrice());
    }
    
    /**
     * Get the vehicle age based on the difference between deliveryDate and currentDate in months.
     * @param currentDate a <code>Calendar</code> object with the currentDate.
     * @return a <code>int</code> with the vehicle age.
     */
    public int getAge ( Calendar currentDate ) {
        return MyCalendarUtils.getDiff(currentDate, deliveryDate);
    }
    
    /**
     * Get the vehicle's seating capacity.
     * @return a <code>int</code> with the seating capacity.
     */
    public int getSeatingCapacity ( ) {
    	return seatingNum;
    }
    
    /**
     * Get the vehicle's standing capacity.
     * @return a <code>int</code> with the standing capacity.
     */
    public int getStandingCapacity ( ) {
    	return standingNum;
    }
    
    /**
     * Get the image file name of the vehicle.
     * @return a <code>String</code> with the image file name.
     */
    public String getImageFileName ( ) {
    	return imagePath;
    }
    
    /**
     * Return a String representation of this vehicle.
     * @return a <code>String</code> with the string representation of this vehicle.
     */
    public String toString () {
        return getRegistrationNumber();
    }
    
    /**
     * Get the id of the assigned schedule.
     * @return a <code>String</code> with the schedule id.
     */
    public String getAssignedScheduleId ( ) {
        try {
            return assignedSchedule.toString();
        } catch ( NullPointerException npe ) {
            return "Not Assigned";
        }
    }
    
    /**
     * Get the vehicle's current position based on the current time.
     * @param time a <code>Calendar</code> object with the current time.
     * @return a <code>String</code> array of current positions.
     */
    public String[] getCurrentPosition ( Calendar time, Simulator simulator ) {
        //This is now all done by route detail so call its method.
        if ( assignedSchedule != null ) {
            return assignedSchedule.getCurrentStop(time, simulator);
        } else {
            return new String[] { "Depot", "" + 0 };
        }
    }
    
    /**
     * Set the assigned route schedule.
     * @param rd a <code>RouteSchedule</code> object with the route schedule.
     */
    public void setAssignedSchedule ( RouteSchedule rd ) {
        assignedSchedule = rd;
    }
    
    public Calendar getDeliveryDate ( ) {
    	return deliveryDate;
    }
    
    /**
     * Check if this vehicle has an assigned schedule.
     * @return a <code>boolean</code> which is true iff this vehicle has an assigned schedule.
     */
    public boolean hasAssignedSchedule ( ) {
        if ( assignedSchedule == null ) { return false; }
        return true;
    }
    
    /**
     * Shorten schedule to the specific stop stated and reduce the delay accordingly.
     * @param stop a <code>String</code> with the stop to terminate at.
     * @param currentTime a <code>Calendar</code> with the current time.
     */
    public void shortenSchedule ( String stop, Calendar currentTime ) {
        assignedSchedule.shortenSchedule(stop, currentTime);
    }
    
    /**
     * Put this vehicle out of service from the current stop until the new stop.
     * @param currentStop a <code>String</code> with the stop to go out of service from.
     * @param newStop a <code>String</code> with the stop to resume service from.
     * @param currentTime a <code>Calendar</code> object with the current time.
     */
    public void outOfService ( String currentStop, String newStop, Calendar currentTime ) {
        assignedSchedule.outOfService(currentStop, newStop, currentTime);
    }

}
