package de.davelee.trams.util;

import java.util.*;

import de.davelee.trams.api.response.RouteResponse;

/**
 * This class sorts routes by route number in ascending order for the TraMS program.
 * @author Dave Lee
 */
public class SortedRouteResponses implements Comparator<RouteResponse> {
    
    public int compare (RouteResponse o1, RouteResponse o2 ) {
        return o1.getRouteNumber().compareTo(o2.getRouteNumber());
    }

}
