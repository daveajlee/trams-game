package trams.data;

import java.util.*;

/**
 * This class represents timetable outlines for the Easy Timetable Generator in TraMS.
 * @author Dave Lee
 */
public class Timetable {

	private int id;
    private String name;
    private Calendar validFromDate;
    private Calendar validToDate;
    private Map<String, ServicePattern> servicePatterns;
    
    public Timetable() {
    	this.servicePatterns = new HashMap<String, ServicePattern>();
    }
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Calendar getValidFromDate() {
		return validFromDate;
	}

	public void setValidFromDate(Calendar validFromDate) {
		this.validFromDate = validFromDate;
	}

	public Calendar getValidToDate() {
		return validToDate;
	}

	public void setValidToDate(Calendar validToDate) {
		this.validToDate = validToDate;
	}

	public Map<String, ServicePattern> getServicePatterns() {
		return servicePatterns;
	}

	public void setServicePatterns(Map<String, ServicePattern> servicePatterns) {
		this.servicePatterns = servicePatterns;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
     * Create a new Timetable object.
     * @param name a <code>String</code> with the name of the timetable.
     * @param validFromDate a <code>Calendar</code> with the valid from date.
     * @param validToDate a <code>Calendar</code> with the valid to date.
     */
    public Timetable ( String name, Calendar validFromDate, Calendar validToDate ) {
        this.name = name;
        this.validFromDate = validFromDate;
        this.validToDate = validToDate;
        //Also initialise hashtable.
        this.servicePatterns = new HashMap<String, ServicePattern>();
    }
    
    /**
     * Method to add a service pattern to this timetable.
     * @param name a <code>String</code> with the service pattern name.
     * @param sp a <code>ServicePattern</code> object representing the service pattern.
     */
    public void addServicePattern ( String name, ServicePattern sp ) {
        System.out.println("Current service pattern size is: " + servicePatterns.size() + " and new name is " + name);
        servicePatterns.put(name, sp);
    }
    
    /**
     * Get service pattern by the supplied name.
     * @param name a <code>String</code> with the service pattern name.
     * @return a <code>ServicePattern</code> object with the the supplied name.
     */
    public ServicePattern getServicePattern ( String name ) {
        return servicePatterns.get(name);
    }
    
    /**
     * Get the list of service pattern names to iterate through.
     * @return a <code>Set</code> with service pattern list.
     */
    public Set<String> getServicePatternNames ( ) {
        return servicePatterns.keySet();
    }
    
    /**
     * Delete service pattern with the supplied name.
     * @param name a <code>String</code> with the service pattern name to delete.
     */
    public void deleteServicePattern ( String name ) {
        servicePatterns.remove(name);
    }
    
    /**
     * Method to return the name of this timetable.
     * @return a <code>String</code> with the timetable name.
     */
    public String getName () {
        return name;
    }
    
    /**
     * Method to set the valid from date of this timetable.
     * @param newValidFromDate a <code>Calendar</code> object with the new valid from date.
     */
    public void setValidFrom ( Calendar newValidFromDate ) {
        validFromDate = newValidFromDate;
    }
    
    /**
     * Method to return the valid from date of this timetable.
     * @return a <code>Calendar</code> object with the valid from date.
     */
    public Calendar getValidFrom ( ) {
        return validFromDate;
    }

    /**
     * Get starting date of this timetable as a String.
     * @return a <code>String</code> object.
     */
    public String getValidFromDateInfo ( ) {
        return getDay(validFromDate.get(Calendar.DAY_OF_WEEK)) + " " + validFromDate.get(Calendar.DAY_OF_MONTH) + getDateExt(validFromDate.get(Calendar.DAY_OF_MONTH)) + " " + getMonth(validFromDate.get(Calendar.MONTH)) + " " + validFromDate.get(Calendar.YEAR);
    }
    
    /**
     * Method to set the valid to date of this timetable.
     * @param newValidToDate a <code>Calendar</code> object with the new valid to date.
     */
    public void setValidTo ( Calendar newValidToDate ) {
        validToDate = newValidToDate;
    }
    
    /**
     * Method to return the valid to date of this timetable.
     * @return a <code>Calendar</code> object with the valid to date.
     */
    public Calendar getValidTo ( ) {
        return validToDate;
    }

    /**
     * Get ending date of this timetable as a String.
     * @return a <code>String</code> object.
     */
    public String getValidToDateInfo ( ) {
        return getDay(validToDate.get(Calendar.DAY_OF_WEEK)) + " " + validToDate.get(Calendar.DAY_OF_MONTH) + getDateExt(validToDate.get(Calendar.DAY_OF_MONTH)) + " " + getMonth(validToDate.get(Calendar.MONTH)) + " " + validToDate.get(Calendar.YEAR);
    }

    /**
     * Get the day of the week as a String based on the number.
     * @param day a <code>int</code> with the day number.
     * @return a <code>String</code> with the string representation of the day.
     */
    private String getDay ( int day ) {
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
     * @param day a <code>int</code> with the month number.
     * @return a <code>String</code> with the string representation of the month.
     */
    private String getMonth ( int month ) {
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
    private String getDateExt ( int dayDate ) {
        if ( dayDate == 1 || dayDate == 21 || dayDate == 31 ) { return "st"; }
        else if ( dayDate == 2 || dayDate == 22 ) { return "nd"; }
        else if ( dayDate == 3 || dayDate == 23 ) { return "rd"; }
        else { return "th"; }
    }
    
}
