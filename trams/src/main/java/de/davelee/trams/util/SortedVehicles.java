package de.davelee.trams.util;

import java.util.*;

import de.davelee.trams.data.Vehicle;


/**
 * This class sorts vehicles by vehicle id for the TraMS program.
 * @author Dave Lee
 */
public class SortedVehicles implements Comparator<Vehicle> {
    
    public int compare (Vehicle o1, Vehicle o2 ) {
        return new String(o1.getRegistrationNumber()).compareTo(new String(o2.getRegistrationNumber()));
    }

}
