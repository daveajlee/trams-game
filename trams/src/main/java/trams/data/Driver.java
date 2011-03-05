package trams.data;

import java.util.Calendar;

import trams.util.MyCalendarUtils;

/**
 * Class representing a driver in the TraMS program.
 * @author Dave
 */
public class Driver {

	private int id;
	private int idNumber;
    private String name;
    private int contractedHours;
    private int hourlyRate;
    private Calendar startDate;

    /**
     * Create a new driver.
     * @param name a <code>String</code> with the name of the driver.
     * @param contractedHours a <code>int</code> with the number of contracted hours. 
     * @param hourlyRate a <code>int</code> with the hourly rate. 
     * @param startDate a <code>Calendar</code> object with the start date of the driver.
     */
    public Driver ( int idNumber, String name, int contractedHours, int hourlyRate, Calendar startDate ) {
        this.idNumber = idNumber;
    	this.name = name;
        this.contractedHours = contractedHours;
        this.hourlyRate = hourlyRate;
        this.startDate = startDate;
    }
    
    public Driver ( ) { }

    /**
     * Check if the driver has started work or not.
     * @param currentDate a <code>Calendar</code> object with the current date.
     * @return a <code>boolean</code> which is true iff the driver has started work.
     */
    public boolean hasStartedWork ( Calendar currentDate ) {
    	return !currentDate.before(startDate); 
    }
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setContractedHours(int contractedHours) {
		this.contractedHours = contractedHours;
	}

	public String getName ( ) {
    	return name;
    }
    
    public int getContractedHours ( ) {
    	return contractedHours;
    }
    
    public void setHourlyRate(int hourlyRate) {
    	this.hourlyRate = hourlyRate;
    }
    
    public int getHourlyRate ( ) {
    	return hourlyRate;
    }
    
    public void setIdNumber ( int idNumber ) {
    	this.idNumber = idNumber;
    }
    
    public int getIdNumber ( ) {
    	return idNumber;
    }
    
    public String getImageFileName ( ) {
    	return "comingsoon.png";
    }
    
    /**
     * Get the driver length of service based on the difference between startDate and currentDate in months.
     * @param currentDate a <code>Calendar</code> object with the currentDate.
     * @return a <code>int</code> with the driver length of service.
     */
    public int getLengthOfService ( Calendar currentDate ) {
        return MyCalendarUtils.getDiff(currentDate, startDate);
    }

}
