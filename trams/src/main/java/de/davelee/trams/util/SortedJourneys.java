package de.davelee.trams.util;

import java.util.*;

import de.davelee.trams.data.Journey;

/**
 * This class sorts journeys by stop times in ascending order for the TraMS program.
 * @author Dave Lee
 */
public class SortedJourneys implements Comparator<Journey> {
    
    public int compare (Journey o1, Journey o2 ) {
        for ( int i = 0; i < o1.getJourneyStops().size(); i++ ) {
            for ( int j = 0; j < o2.getJourneyStops().size(); j++ ) {
                if ( o1.getJourneyStops().get(i).getStopName().equalsIgnoreCase(o2.getJourneyStops().get(j).getStopName()) ) {
                    return o1.getJourneyStops().get(i).getStopTime().compareTo(o2.getJourneyStops().get(j).getStopTime());
                }
            }
        }
        //Assume they were in the right order!
        return -1;
    }

}
