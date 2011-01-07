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
     * Get the day of the week as a String based on the number.
     * @param day a <code>int</code> with the day number.
     * @return a <code>String</code> with the string representation of the day.
     */
    public String getDay ( int day ) {
        if ( day == Calendar.SUNDAY ) { return "Sunday"; }
        else if ( day == Calendar.MONDAY ) { return "Monday"; }
        else if ( day == Calendar.TUESDAY ) { return "Tuesday"; }
        else if ( day == Calendar.WEDNESDAY ) { return "Wednesday"; }
        else if ( day == Calendar.THURSDAY ) { return "Thursday"; }
        else if ( day == Calendar.FRIDAY ) { return "Friday"; }
        else if ( day == Calendar.SATURDAY ) { return "Saturday"; }
        else { return ""; }
    }
    
    /**
     * Get the month as a String based on the number.
     * @param month a <code>int</code> with the month number.
     * @return a <code>String</code> with the string representation of the month.
     */
    public String getMonth ( int month ) {
        if ( month == Calendar.JANUARY ) { return "January"; }
        else if ( month == Calendar.FEBRUARY ) { return "February"; }
        else if ( month == Calendar.MARCH ) { return "March"; }
        else if ( month == Calendar.APRIL ) { return "April"; }
        else if ( month == Calendar.MAY ) { return "May"; }
        else if ( month == Calendar.JUNE ) { return "June"; }
        else if ( month == Calendar.JULY ) { return "July"; }
        else if ( month == Calendar.AUGUST ) { return "August"; }
        else if ( month == Calendar.SEPTEMBER ) { return "September"; }
        else if ( month == Calendar.OCTOBER ) { return "October"; }
        else if ( month == Calendar.NOVEMBER ) { return "November"; }
        else if ( month == Calendar.DECEMBER ) { return "December"; }
        else { return ""; }
    }
    
    /**
     * Get the day extension for a particular day number.
     * @param dayDate a <code>int</code> with the day number.
     * @return a <code>String</code> with the day extension.
     */
    public String getDateExt ( int dayDate ) {
        if ( dayDate == 1 || dayDate == 21 || dayDate == 31 ) { return "st"; }
        else if ( dayDate == 2 || dayDate == 22 ) { return "nd"; }
        else if ( dayDate == 3 || dayDate == 23 ) { return "rd"; }
        else { return "th"; }
    }
    
    /**
     * Get the hour.
     * @param hour a <code>int</code> with the hour.
     * @param ampm a <code>int</code> with the am/pm.
     * @return a <code>String</code> with the formatted hour.
     */
    private String getHour ( int hour, int ampm ) {
        if ( hour == 0 && ampm == Calendar.PM ) {
            return "" + 12;
        } 
        return "" + hour;
    }
    
    /**
     * Get the minutes.
     * @param minute a <code>int</code> with the minutes.
     * @return a <code>String</code> with the formatted minutes.
     */
    private String getMinute ( int minute ) {
        if ( minute < 10 ) { return "0" + minute; }
        return "" + minute;
    } 
    
    /**
     * Format am/pm.
     * @param ampm a <code>int</code> with the fixed value of am/pm.
     * @return a <code>String</code> with the formatted AM/PM.
     */
    private String getAMPM ( int ampm ) {
        if ( ampm == Calendar.AM ) {
            return "am";
        }
        return "pm";
    }
    
    /**
     * Get the current time.
     * @return a <code>Calendar</code> representing the current time.
     */
    public Calendar getCurrentTime ( ) {
        return theCurrentDateTime;
    }
    
    /**
     * Get the date info.
     * @return a <code>String</code> with the date info.
     */
    public String getDateInfo ( ) {
        return getDay(theCurrentDateTime.get(Calendar.DAY_OF_WEEK)) + " " + theCurrentDateTime.get(Calendar.DATE) + getDateExt(theCurrentDateTime.get(Calendar.DATE)) + " " + getMonth(theCurrentDateTime.get(Calendar.MONTH)) + " " + theCurrentDateTime.get(Calendar.YEAR);
    }
    
    /**
     * Get the short date in the form dd/mm/yy.
     * @return a <code>String</code> with the short date info.
     */
    public String getShortDate ( ) {
        int month = theCurrentDateTime.get(Calendar.MONTH) + 1;
        return theCurrentDateTime.get(Calendar.DATE) + "/" + month + "/" + theCurrentDateTime.get(Calendar.YEAR);
    }
    
    /**
     * Get the short year in the form yy.
     * @return a <code>String</code> with the short date info.
     */
    public String getShortYear ( ) {
        String year = "" + theCurrentDateTime.get(Calendar.YEAR);
        return year.substring(2,4);
    }
    
    /**
     * Get the time info.
     * @return a <code>String</code> with the time info.
     */
    public String getTimeInfo ( ) {
        return getHour(theCurrentDateTime.get(Calendar.HOUR), theCurrentDateTime.get(Calendar.AM_PM)) + "." + getMinute(theCurrentDateTime.get(Calendar.MINUTE)) + getAMPM(theCurrentDateTime.get(Calendar.AM_PM));
    }
    
    /**
     * Return the supplied calendar object as a formatted string.
     * @param calDate a <code>Calendar</code> object to format.
     * @return a <code>String</code> with the formatted string.
     */
    public String formatDateString ( Calendar calDate ) {
        return getDay(calDate.get(Calendar.DAY_OF_WEEK)) + " " + calDate.get(Calendar.DATE) + getDateExt(calDate.get(Calendar.DATE)) + " " + getMonth(calDate.get(Calendar.MONTH)) + " " + calDate.get(Calendar.YEAR);
    }
    
}
