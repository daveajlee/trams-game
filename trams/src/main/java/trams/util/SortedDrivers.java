package trams.util;

import java.util.Comparator;

import trams.data.Driver;



/**
 * This class sorts drivers by driver id for the TraMS program.
 * @author Dave Lee
 */
public class SortedDrivers implements Comparator<Driver> {
    
    public int compare (Driver o1, Driver o2 ) {
        return new Integer(o1.getIdNumber()).compareTo(new Integer(o2.getIdNumber()));
    }

}
