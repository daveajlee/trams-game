package de.davelee.trams.util;

import java.util.*;

import de.davelee.trams.data.Journey;

/**
 * This class sorts journeys by stop times in ascending order for the TraMS program.
 * @author Dave Lee
 */
public class SortedJourneys implements Comparator<Journey> {
    
    public int compare (Journey o1, Journey o2 ) {
        return ((int) o1.getRouteScheduleId() - (int) o2.getRouteScheduleId());
    }

}
