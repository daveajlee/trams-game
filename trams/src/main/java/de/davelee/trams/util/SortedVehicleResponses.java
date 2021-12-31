package de.davelee.trams.util;

import java.util.*;

import de.davelee.trams.api.response.VehicleResponse;


/**
 * This class sorts vehicles by vehicle id for the TraMS program.
 * @author Dave Lee
 */
public class SortedVehicleResponses implements Comparator<VehicleResponse> {
    
    public int compare (VehicleResponse o1, VehicleResponse o2 ) {
        return new String(o1.getFleetNumber()).compareTo(new String(o2.getFleetNumber()));
    }

}
