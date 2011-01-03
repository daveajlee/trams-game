package trams.data;

import java.util.*;

/**
 * Class representing a driver in the TraMS program.
 * @author Dave
 */
public class Driver {

	private int id;
    private String name;
    private int contractedHours;
    private Calendar startDate;

    /**
     * Create a new driver.
     * @param name a <code>String</code> with the name of the driver.
     * @param contractedHours a <code>int</code> with the number of contracted hours.  
     * @param startDate a <code>Calendar</code> object with the start date of the driver.
     */
    public Driver ( String name, int contractedHours, Calendar startDate ) {
        this.name = name;
        this.contractedHours = contractedHours;
        this.startDate = startDate;
    }
    
    public Driver ( ) { }

    /**
     * Check if the driver has started work or not.
     * @param currentDate a <code>Calendar</code> object with the current date.
     * @return a <code>boolean</code> which is true iff the driver has started work.
     */
    public boolean hasStartedWork ( Calendar currentDate ) {
        //Check year...
        int yearDiff = currentDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR);
        if ( yearDiff < 0 ) { return false; } if ( yearDiff > 0 ) { return true; }
        //Check month...
        int monthDiff = currentDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH);
        if ( monthDiff < 0 ) { return false; } if ( monthDiff > 0 ) { return true; }
        //Check date...
        int dateDiff = currentDate.get(Calendar.DATE) - startDate.get(Calendar.DATE);
        if ( dateDiff < 0 ) { return false; }
        return true;
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

}
