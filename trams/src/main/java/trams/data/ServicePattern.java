package trams.data;

import java.util.*;

/**
 * This class represents a service pattern for a timetable - used by Easy Timetable Generator.
 * @author Dave Lee.
 */
public class ServicePattern {
    
	private int id;
    private String name;
    private String daysOfOperation;
    private String returnTerminus;
    private String outgoingTerminus;
    private Calendar startTime;
    private Calendar endTime;
    private int frequency;
    private int routeDuration;
    
    public ServicePattern ( ) { }
    
    /**
     * Create a new ServicePattern object.
     * @param name a <code>String</code> with the service pattern name.
     * @param days a <code>LinkedList</code> with the days of operation.
     * @param returnTerminus a <code>String</code> with the 1st stop i.e. the return terminus.
     * @param outgoingTerminus a <code>String</code> with the 2nd stop i.e. the outgoing terminus.
     * @param startTime a <code>Calendar</code> with the start time.
     * @param endTime a <code>Calendar</code> with the end time.
     * @param frequency a <code>int</code> with the frequency in minutes.
     * @param duration a <code>int</code> with the route duration.
     */
    public ServicePattern ( String name, String days, String returnTerminus, String outgoingTerminus, Calendar startTime, Calendar endTime, int frequency, int duration ) {
        this.name = name;
        this.daysOfOperation = days;
        this.returnTerminus = returnTerminus;
        this.outgoingTerminus = outgoingTerminus;
        this.startTime = startTime;
        this.endTime = endTime;
        this.frequency = frequency;
        this.routeDuration = duration;
    }
    
    /**
     * Change some of the information in a service pattern.
     * @param name a <code>String</code> with the service pattern name.
     * @param days a <code>LinkedList</code> with the days of operation.
     * @param returnTerminus a <code>String</code> with the 1st stop i.e. the return terminus.
     * @param outgoingTerminus a <code>String</code> with the 2nd stop i.e. the outgoing terminus.
     * @param startTime a <code>Calendar</code> with the start time.
     * @param endTime a <code>Calendar</code> with the end time.
     * @param frequency a <code>int</code> with the frequency in minutes.
     * @param duration a <code>int</code> with the route duration.
     */
    public void editServicePattern ( String name, String days, String returnTerminus, String outgoingTerminus, Calendar startTime, Calendar endTime, int frequency, int duration ) {
        this.name = name;
        this.daysOfOperation = days;
        this.returnTerminus = returnTerminus;
        this.outgoingTerminus = outgoingTerminus;
        this.startTime = startTime;
        this.endTime = endTime;
        this.frequency = frequency;
        this.routeDuration = duration;
    }
    
    /**
     * Get the name of the service pattern.
     * @return a <code>String</code> with the name.
     */
    public String getName ( ) {
        return name;
    }
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRouteDuration() {
		return routeDuration;
	}

	public void setRouteDuration(int routeDuration) {
		this.routeDuration = routeDuration;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDaysOfOperation(String daysOfOperation) {
		this.daysOfOperation = daysOfOperation;
	}

	public void setReturnTerminus(String returnTerminus) {
		this.returnTerminus = returnTerminus;
	}

	public void setOutgoingTerminus(String outgoingTerminus) {
		this.outgoingTerminus = outgoingTerminus;
	}

	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	/**
     * Get the days of operations for this service pattern.
     * @return a <code>LinkedList</code> with the days of operation.
     */
    public String getDaysOfOperation ( ) {
        return daysOfOperation;
    }

    /**
     * Check if the supplied day is a day of operation for this service pattern.
     * @param dayNum a <code>int</code> with the day number in the week.
     * @return a <code>boolean</code> which is true iff this is a day of operation.
     */
    public boolean isDayOfOperation ( int dayNum ) {
    	String[] days = daysOfOperation.split(",");
    	for ( int i = 0; i < days.length; i++ ) {
    		if ( Integer.parseInt(days[i]) == dayNum ) {
    			return true;
    		}
    	}
        return false;
    }
    
    /**
     * Get the return terminus - also the 1st selected stop.
     * @return a <code>String</code> with the return terminus.
     */
    public String getReturnTerminus ( ) {
        return returnTerminus;
    }
    
    /**
     * Get the outgoing terminus - also the 2nd selected stop.
     * @return a <code>String</code> with the outgoing terminus.
     */
    public String getOutgoingTerminus ( ) {
        return outgoingTerminus;
    }
    
    /**
     * Get the start time of this service pattern.
     * @return a <code>Calendar</code> with the start time.
     */
    public Calendar getStartTime ( ) {
        return startTime;
    }
    
    /**
     * Get the end time of this service pattern.
     * @return a <code>Calendar</code> with the end time.
     */
    public Calendar getEndTime ( ) {
        return endTime;
    }
    
    /**
     * Get the frequency of this service pattern.
     * @return a <code>int</code> with the frequency.
     */
    public int getFrequency ( ) {
        return frequency;
    }
    
    /**
     * Get the duration of the route.
     * @return a <code>int</code> with the duration.
     */
    public int getDuration ( ) {
        return routeDuration;
    }

}
