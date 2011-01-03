package trams.data;

import java.util.*;

/**
 * Class to represent a service (i.e. one run of a route from terminus to terminus) in the TraMS program.
 * @author Dave
 */
public class Service {
    
    /**
	 * 
	 */
	private int serviceId;
    private List<Stop> serviceStops;
    
    /**
     * Create a new service.
     * @param serviceId a <code>int</code> with the service id.
     */
    public Service ( ) {
        serviceStops = new ArrayList<Stop>();
    }
    
    /**
     * Get the service id.
     * @return a <code>int</code> with the service id.
     */
    public int getServiceId () {
        return serviceId;
    }
    
    public List<Stop> getServiceStops() {
		return serviceStops;
	}

	public void setServiceStops(List<Stop> serviceStops) {
		this.serviceStops = serviceStops;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	/**
     * Check if the service has started based on the current time.
     * @param currentTime a <code>Calendar</code> object.
     * @return a <code>boolean</code> which is true iff the service has started.
     */
    public boolean hasServiceStarted(Calendar currentTime) {
        if ( checkTimeDiff(currentTime, serviceStops.get(0).getStopTime() ) > -1 ) {
            return true;
        }
        return false;
    }
    
    /**
     * Check if the service has ended based on the current time.
     * @param currentTime a <code>Calendar</code> object.
     * @return a <code>boolean</code> which is true iff the service has ended.
     */
    public boolean hasServiceEnded(Calendar currentTime) {
        if ( checkTimeDiff(currentTime, getLastStop().getStopTime() ) > 0 ) {
            return true;
        }
        return false;
    }
    
    /**
     * Get the current stop based on the current time.
     * @param currentTime a <code>Calendar</code> object. 
     * @return a <code>String</code> array with the current stop.
     */
    public String[] getCurrentStop(Calendar currentTime) {
        for ( Stop myServiceStop : serviceStops ) {
            if ( checkTimeDiff(myServiceStop.getStopTime(), currentTime) >= 0 ) {
                //System.out.println("I will be at " + myServiceStop.getStopName() + " in " + checkTimeDiff(myServiceStop.getStopTime(), currentTime) + " seconds.");
                return new String[] { myServiceStop.getStopName(), "" + checkTimeDiff(myServiceStop.getStopTime(), currentTime) } ;
                //return "I will be at " + myServiceStop.getStopName() + " in " + checkTimeDiff(myServiceStop.getStopTime(), currentTime) + " seconds.";
            }
        }
        return new String[] { "No Stop Found", "" + 0 };
    }
    
    /**
     * Get the start terminus of this service.
     * @param currentTime a <code>Calendar</code> object.
     * @return a <code>String</code> with what should be displayed on screen.
     */
    public String getStartTerminus(Calendar currentTime) {
        if ( serviceStops.get(0).getStopName().equalsIgnoreCase("Depot") ) {
            return "Depot";
        }
        return "I'm at " + serviceStops.get(0).getStopName() + " Terminus and will be here for " + checkTimeDiff(serviceStops.get(0).getStopTime(), currentTime) + " seconds.";
    }
    
    /**
     * Get the start terminus of this service.
     * @return a <code>String</code> with the start terminus.
     */
    public String getStartTerminus () {
        return serviceStops.get(0).getStopName();
    }
    
    /**
     * Get the end terminus of this service.
     * @return a <code>String</code> with the end terminus.
     */
    public String getEndDestination () {
        return getLastStop().getStopName();
    }
    
    /**
     * Add stop.
     * @param newStop a <code>Stop</code> object with the stop to add.
     */
    public void addStop ( Stop newStop ) {
        serviceStops.add(newStop);
    }
    
    /**
     * Delete stop.
     * @param oldStop a <code>Stop</code> object with the stop to delete.
     */
    public void removeStop ( Stop oldStop ) {
        serviceStops.remove(oldStop);
    }
    
    /**
     * Get stop object based on stop name.
     * @param name a <code>String</code> with the stop name.
     * @return a <code>Stop</code> object.
     */
    public Stop getStop ( String name ) {
        for ( Stop myStop : serviceStops ) {
            if ( myStop.getStopName().equalsIgnoreCase(name) ) {
                return myStop;
            }
        }
        return null;
    }

    /**
     * Get the last stop.
     * @return a <code>Stop</code> object representing the last stop in this service.
     */
    public Stop getLastStop ( ) {
        return serviceStops.get(serviceStops.size()-1);
    }
    
    /**
     * Get the number of stops belonging to this service.
     * @return a <code>int</code> with the number of stops.
     */
    public int getNumStops ( ) {
        return serviceStops.size();
    }
    
    /**
     * Get stop based on location.
     * @param pos a <code>int</code> with the location. 
     * @return a <code>Stop</code> object.
     */
    public Stop getStop ( int pos ) {
        return serviceStops.get(pos);
    }
    
    /**
     * Remove stops between two stops.
     * @param firstStop a <code>String</code> with the first stop.
     * @param secondStop a <code>String</code> with the second stop.
     * @param includeFirst a <code>boolean</code> which is true iff the first stop should be deleted.
     * @param includeLast a <code>boolean</code> which is true iff the second stop should be deleted.
     * @return a <code>long</code> with the amount of minutes saved.
     */
    public long removeStopsBetween ( String firstStop, String secondStop, boolean includeFirst, boolean includeLast ) {
        //Get long to represent time diff between the two stops for delay.
        long timeDiff = checkTimeDiff(getStop(firstStop).getStopTime(), getStop(secondStop).getStopTime());
        //Now remove stops between the two and possibly first and last as appropriate.
        boolean removeFlag = false;
        for ( Stop myStop : serviceStops ) {
            if ( myStop.getStopName().equalsIgnoreCase(secondStop) ) {
                if ( includeLast ) { serviceStops.remove(myStop); }
                removeFlag = false;
            }
            if ( removeFlag ) {
                serviceStops.remove(myStop);
            }
            if ( myStop.getStopName().equalsIgnoreCase(firstStop) ) {
                if ( includeFirst ) { serviceStops.remove(myStop); }
                removeFlag = true;
            }
        }
        //Now return time difference.
        return timeDiff;
    }
    
    /**
     * Get the time difference between two stops.
     * @param firstStop a <code>String</code> with the first stop.
     * @param secondStop a <code>String</code> with the second stop.
     * @return a <code>long</code> with the time difference.
     */
    public long getStopTimeDifference ( String firstStop, String secondStop ) {
        return checkTimeDiff(getStop(firstStop).getStopTime(), getStop(secondStop).getStopTime());
    }
    
    /**
     * Get all display stops for this service.
     * @return a <code>String</code> with all display stops.
     */
    public String getAllDisplayStops ( ) {
        String stops = "";
        for ( Stop myStop : serviceStops ) {
            stops += "[" + myStop.getStopName() + "," + getDateInfo(myStop.getStopTime()) + "]";
        }
        return stops;
    }
    
    /**
     * Check if this is an outward service.
     * @param outwardStops a <code>LinkedList</code> with list of outward stops.
     * @return a <code>boolean</code> which is true iff this is an outward service.
     */
    public boolean isOutwardService ( LinkedList<String> outwardStops ) {
        //First of all get the index of the first stop of this service in outwardStops.
        int firstIndex = outwardStops.indexOf(serviceStops.get(0).getStopName());
        //Now get the index of the second stop.
        int secondIndex = outwardStops.indexOf(serviceStops.get(1).getStopName());
        //If the indexes are consecutive i.e. difference of 1 then it is an outward service.
        if ( secondIndex - firstIndex == 1 ) { return true; }
        //Otherwise it is not.
        return false;
    }
    
    /**
     * Check the time diff between two calendar objects.
     * @param firstTime a <code>Calendar</code> object with the first time.
     * @param secondTime a <code>Calendar</code> object with the second time.
     * @return a <code>long</code> with the time difference.
     */
    private long checkTimeDiff(Calendar firstTime, Calendar secondTime) {
        //Store time diff.
        long timeDiff = 0;
        if ( firstTime.get(Calendar.AM_PM) == Calendar.AM && secondTime.get(Calendar.AM_PM) == Calendar.PM ) {
            timeDiff = -(12 * (60 * 60));
        }
        else if ( secondTime.get(Calendar.AM_PM) == Calendar.AM && firstTime.get(Calendar.AM_PM) == Calendar.PM ) {
            timeDiff = 12 *(60 * 60);
        }
        long diffHour = (firstTime.get(Calendar.HOUR) - secondTime.get(Calendar.HOUR) ) * (60 * 60);
        long diffMins = (firstTime.get(Calendar.MINUTE) - secondTime.get(Calendar.MINUTE)) * 60;
        long diffSecs = (firstTime.get(Calendar.SECOND) - secondTime.get(Calendar.SECOND));
        //Calculate timeDiff.
        timeDiff += diffHour + diffMins + diffSecs;
        return timeDiff;
    }
    
    /**
     * Get the maximum time difference between two stops. 
     * @param prevStopName a <code>String</code> with first stop.
     * @param thisStopName a <code>String</code> with second stop.
     * @return a <code>long</code> with the time difference.
     */
    public long getStopMaxTimeDiff ( String prevStopName, String thisStopName ) {
        try {
            return checkTimeDiff ( getStop(prevStopName).getStopTime(), getStop(thisStopName).getStopTime() );
        } catch ( NullPointerException npe ) {
            return Integer.MAX_VALUE;
        }
    }
    
    /**
     * Get date info based on Calendar object.
     * @param stopTime a <code>Calendar</code> object with stop time.
     * @return a <code>String</code> with a formatted time String.
     */
    public String getDateInfo ( Calendar stopTime ) {
        return getDay(stopTime.get(Calendar.DAY_OF_WEEK)) + " " + stopTime.get(Calendar.DATE) + getDateExt(stopTime.get(Calendar.DATE)) + " " + getMonth(stopTime.get(Calendar.MONTH)) + " " + stopTime.get(Calendar.YEAR) + " " + getHour(stopTime.get(Calendar.HOUR), stopTime.get(Calendar.AM_PM)) + "." + getMinute(stopTime.get(Calendar.MINUTE)) + getAMPM(stopTime.get(Calendar.AM_PM));
    }
    
    /**
     * Return the day based on the number.
     * @param day a <code>int</code> with the day number.
     * @return a <code>String</code> with the formatted day.
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
     * Get the month based on the number.
     * @param month a <code>int</code> with the month number.
     * @return a <code>String</code> with the formatted month.
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
     * Get the date extension  (e.g. th).
     * @param dayDate a <code>int</code> with the number of day.
     * @return a <code>String</code> with the correct extension.
     */
    private String getDateExt ( int dayDate ) {
        if ( dayDate == 1 || dayDate == 21 || dayDate == 31 ) { return "st"; }
        else if ( dayDate == 2 || dayDate == 22 ) { return "nd"; }
        else if ( dayDate == 3 || dayDate == 23 ) { return "rd"; }
        else { return "th"; }
    }
    
    /**
     * Get formatted hour.
     * @param hour a <code>int</code> with the hour.
     * @param ampm a <code>int</code> with am/pm.
     * @return a <code>String</code> with formatted hour.
     */
    private String getHour ( int hour, int ampm ) {
        if ( hour == 0 && ampm == Calendar.PM ) {
            return "" + 12;
        } 
        return "" + hour;
    }
    
    /**
     * Get formatted minute (with leading 0).
     * @param minute a <code>int</code> with the minute.
     * @return a <code>String</code> with formatted minute.
     */
    private String getMinute ( int minute ) {
        if ( minute < 10 ) { return "0" + minute; }
        return "" + minute;
    } 
    
    /**
     * Get formatted AM/PM.
     * @param ampm a <code>int</code> with am/pm.
     * @return a <code>String</code> with formatted am/pm.
     */
    private String getAMPM ( int ampm ) {
        if ( ampm == Calendar.AM ) {
            return "am";
        }
        return "pm";
    }
    
}
