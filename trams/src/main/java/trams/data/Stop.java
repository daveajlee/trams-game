package trams.data;

import java.util.Calendar;

/**
 * This class represents a Stop in the TraMS program.
 * @author Dave Lee
 */
public class Stop {
    
    /**
	 * 
	 */
	private int id;
	private String stopName;
    private Calendar stopTime;
    
    private static final int MAX_SINGLE_DIGIT = 10;
    
    public Stop ( ) {
    	
    }
    
    /**
     * Create a new stop. 
     * @param stopName a <code>String</code> with the stop name.
     */
    public Stop ( String stopName ) {
        this.stopName = stopName;
    }
    
    /**
     * Create a new stop.
     * @param stopName a <code>String</code> with the stop name.
     * @param stopTime a <code>Calendar</code> object with the stop time.
     */
    public Stop ( String stopName, Calendar stopTime ) {
        this.stopName = stopName;
        this.stopTime = stopTime;
    }
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setStopName(String stopName) {
		this.stopName = stopName;
	}

	public void setStopTime(Calendar stopTime) {
		this.stopTime = stopTime;
	}

	/**
     * Get the stop name.
     * @return a <code>String</code> with the stop name.
     */
    public String getStopName() {
        return stopName;
    }
    
    /**
     * Get the stop time.
     * @return a <code>Calendar</code> object with the stop time.
     */
    public Calendar getStopTime( ) {
        return stopTime;
    }
    
    /**
     * Get the stop time as hh:mm.
     * @return a <code>String</code> with the time as hh:mm.
     */
    public String getDisplayStopTime( ) {
        int minute = stopTime.get(Calendar.MINUTE);
        String min;
        if ( minute < MAX_SINGLE_DIGIT ) {
            min = "0" + minute;
        } else {
            min = "" + minute;
        }
        return stopTime.get(Calendar.HOUR_OF_DAY) + ":" + min;
    }
    
    /**
     * Return a string representation of this Stop object.
     * @return a <code>String</code> with the stop name.
     */
    public String toString ( ) {
        return stopName;
    }

}
