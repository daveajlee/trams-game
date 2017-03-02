package de.davelee.trams.services;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import de.davelee.trams.data.Journey;
import de.davelee.trams.data.Stop;
import de.davelee.trams.util.DateFormats;
import de.davelee.trams.util.JourneyStatus;

public class JourneyService {
	
	public JourneyService() {
		
	}
	
	/**
     * Get the status of the journey based on the current time - either not yet run, running or finished.
     * @param currentTime a <code>Calendar</code> object.
     * @return a <code>JourneyStatus</code> which has the current status of the journey.
     */
    public JourneyStatus checkJourneyStatus(Journey journey, Calendar currentTime) {
    	//Did the journey start?
        if ( checkTimeDiff(currentTime, journey.getJourneyStops().get(0).getStopTime() ) > -1 ) {
        	//Has the journey already ended?
        	if ( checkTimeDiff(currentTime, getLastStop(journey).getStopTime() ) > 0 ) {
                return JourneyStatus.FINISHED;
            }
            return JourneyStatus.RUNNING;
        }
        return JourneyStatus.YET_TO_RUN;
    }
    
    /**
     * Get the current stop name based on the current time.
     * @param currentTime a <code>Calendar</code> object. 
     * @return a <code>String</code> array with the current stop.
     */
    public String getCurrentStopName(Journey journey, Calendar currentTime) {
        for ( Stop myJourneyStop : journey.getJourneyStops() ) {
            if ( checkTimeDiff(myJourneyStop.getStopTime(), currentTime) >= 0 ) {
                //logger.debug("I will be at " + myServiceStop.getStopName() + " in " + checkTimeDiff(myServiceStop.getStopTime(), currentTime) + " seconds.");
                return myJourneyStop.getStopName();
                //return "I will be at " + myServiceStop.getStopName() + " in " + checkTimeDiff(myServiceStop.getStopTime(), currentTime) + " seconds.";
            }
        }
        return "No Stop Found";
    }
    
    /**
     * Get the start terminus of this journey.
     * @return a <code>String</code> with the start terminus.
     */
    public String getStartTerminus (Journey journey) {
        return journey.getJourneyStops().get(0).getStopName();
    }
    
    /**
     * Get stop object based on stop name.
     * @param name a <code>String</code> with the stop name.
     * @return a <code>Stop</code> object.
     */
    public Stop getStop ( long journeyId, String name ) {
        for ( Stop myStop : getJourneyById(journeyId).getJourneyStops() ) {
            if ( myStop.getStopName().equalsIgnoreCase(name) ) {
                return myStop;
            }
        }
        return null;
    }

    /**
     * Get the last stop.
     * @return a <code>Stop</code> object representing the last stop in this journey.
     */
    public Stop getLastStop ( Journey journey ) {
        return journey.getJourneyStops().get(journey.getJourneyStops().size()-1);
    }
    
    /**
     * Get the number of stops belonging to this journey.
     * @return a <code>int</code> with the number of stops.
     */
    public int getNumStops ( Journey journey ) {
        return journey.getJourneyStops().size();
    }
    
    /**
     * Get stop based on location.
     * @param pos a <code>int</code> with the location. 
     * @return a <code>Stop</code> object.
     */
    public Stop getStop ( long journeyId, int pos ) {
        return getJourneyById(journeyId).getJourneyStops().get(pos);
    }
    
    /**
     * Remove stops between two stops.
     * @param firstStop a <code>String</code> with the first stop.
     * @param secondStop a <code>String</code> with the second stop.
     * @param includeFirst a <code>boolean</code> which is true iff the first stop should be deleted.
     * @param includeLast a <code>boolean</code> which is true iff the second stop should be deleted.
     * @return a <code>long</code> with the amount of minutes saved.
     */
    public long removeStopsBetween ( Journey journey, String firstStop, String secondStop, boolean includeFirst, boolean includeLast ) {
        //Get long to represent time diff between the two stops for delay.
        long timeDiff = checkTimeDiff(getStop(journey.getId(), firstStop).getStopTime(), getStop(journey.getId(), secondStop).getStopTime());
        //Now remove stops between the two and possibly first and last as appropriate.
        boolean removeFlag = false;
        for ( Stop myStop : journey.getJourneyStops() ) {
            if ( myStop.getStopName().equalsIgnoreCase(secondStop) ) {
                if ( includeLast ) { journey.removeStop(myStop); }
                removeFlag = false;
            }
            if ( removeFlag ) {
                journey.removeStop(myStop);
            }
            if ( myStop.getStopName().equalsIgnoreCase(firstStop) ) {
                if ( includeFirst ) { journey.removeStop(myStop); }
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
    public long getStopTimeDifference ( Journey journey, String firstStop, String secondStop ) {
        return checkTimeDiff(getStop(journey.getId(), firstStop).getStopTime(), getStop(journey.getId(), secondStop).getStopTime());
    }
    
    /**
     * Check if this is an outward journey.
     * @param outwardStops a <code>LinkedList</code> with list of outward stops.
     * @return a <code>boolean</code> which is true iff this is an outward journey.
     */
    public boolean isOutwardJourney ( Journey journey, List<String> outwardStops ) {
        //First of all get the index of the first stop of this journey in outwardStops.
        int firstIndex = outwardStops.indexOf(journey.getJourneyStops().get(0).getStopName());
        //Now get the index of the second stop.
        int secondIndex = outwardStops.indexOf(journey.getJourneyStops().get(1).getStopName());
        //If the indexes are consecutive i.e. difference of 1 then it is an outward journey.
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
    public long getStopMaxTimeDiff ( Journey journey, String prevStopName, String thisStopName ) {
        try {
            return checkTimeDiff ( getStop(journey.getId(), prevStopName).getStopTime(), getStop(journey.getId(), thisStopName).getStopTime() );
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
    	return DateFormats.FULL_FORMAT.getFormat().format(stopTime.getTime());
    }
    
    //TODO: Implement properly and not mock!
    public Journey getJourneyById(long id) {
    	return new Journey();
    }
    
    public Journey createJourney ( HashMap<String, Calendar> stops ) {
    	Journey journey = new Journey();
    	Iterator<String> stopKeysIterator = stops.keySet().iterator();
    	while ( stopKeysIterator.hasNext() ) {
    		Stop stop = new Stop();
    		stop.setStopName(stopKeysIterator.next()); stop.setStopTime(stops.get(stop.getStopName()));
    		journey.addStop(stop);
    	}
    	return journey;
    }
    
}
