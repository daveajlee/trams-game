package de.davelee.trams.util;

import java.util.*;

import de.davelee.trams.model.RouteModel;

/**
 * This class sorts routes by route number in ascending order for the TraMS program.
 * @author Dave Lee
 */
public class SortedRouteModels implements Comparator<RouteModel> {
    
    public int compare (RouteModel o1, RouteModel o2 ) {
        return o1.getRouteNumber().compareTo(o2.getRouteNumber());
    }

}
