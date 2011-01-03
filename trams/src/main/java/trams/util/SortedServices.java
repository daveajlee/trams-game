package trams.util;

import java.util.*;

import trams.data.Service;


/**
 * This class sorts services by stop times in ascending order for the TraMS program.
 * @author Dave Lee
 */
public class SortedServices implements Comparator<Service> {
    
    public int compare (Service o1, Service o2 ) {
        for ( int i = 0; i < o1.getNumStops(); i++ ) {
            for ( int j = 0; j < o2.getNumStops(); j++ ) {
                if ( o1.getStop(i).getStopName().equalsIgnoreCase(o2.getStop(j).getStopName()) ) {
                    return o1.getStop(i).getStopTime().compareTo(o2.getStop(j).getStopTime());
                }
            }
        }
        //Assume they were in the right order!
        return -1;
    }

}
