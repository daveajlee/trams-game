package de.davelee.trams.util;

import java.util.*;

import de.davelee.trams.data.Route;

/**
 * This class sorts routes by route number in ascending order for the TraMS program.
 * @author Dave Lee
 */
public class SortedRoutes implements Comparator<Route> {
    
    public int compare (Route o1, Route o2 ) {
        return o1.getRouteNumber().compareTo(o2.getRouteNumber());
    }

}
