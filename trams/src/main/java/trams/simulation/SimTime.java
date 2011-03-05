package trams.simulation;

import java.util.*;

/**
 * This class represents simulated time in the TraMS program.
 * @author Dave Lee.
 */
public class SimTime implements java.io.Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Calendar theCurrentDateTime;
    private int theTimeIncrement;
    
    private static final int MAX_SINGLE_DIGIT = 10;
    private static final int NUM_AM_HOURS = 12;
    
    /**
     * Create a new simulated time.
     * @param timeIncrement a <code>int</code> with the time increment.
     */
    public SimTime ( int timeIncrement ) {
        //Set time to defaults for starting new game.
        theCurrentDateTime = new GregorianCalendar(2011,Calendar.JANUARY,1,3,0,0);
        //Set time increment.
        theTimeIncrement = timeIncrement;
    }
    
    /**
     * Create a new simulated time with an initial time.
     * @param initialTime a <code>Calendar</code> representing the initial time.
     * @param timeIncrement a <code>int</code> with the time increment.
     */
    public SimTime ( Calendar initialTime, int timeIncrement ) {
        //Save time.
        theCurrentDateTime = initialTime;
        //Set time increment
        theTimeIncrement = timeIncrement;
    }
    
    /**
     * Increment time according to the Gregorian calendar.
     */
    public void incrementTime ( ) {
        //Increment time following gregorian rules!
        theCurrentDateTime.add(Calendar.MINUTE, theTimeIncrement);
    }
    
    /**
     * Get the time increment.
     * @return a <code>int</code> with the time increment.
     */
    public int getIncrement ( ) {
        return theTimeIncrement;
    }
    
    /**
     * Set a new time increment
     * @param newIncrement a <code>int</code> with the new time increment.
     */
    public void setIncrement ( int newIncrement ) {
        theTimeIncrement = newIncrement;
    }    
        
    /**
     * Get the current time.
     * @return a <code>Calendar</code> representing the current time.
     */
    public Calendar getCurrentTime ( ) {
        return theCurrentDateTime;
    }
    
}
