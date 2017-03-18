package de.davelee.trams.util;

import java.util.*;

import de.davelee.trams.model.VehicleModel;


/**
 * This class sorts vehicles by vehicle id for the TraMS program.
 * @author Dave Lee
 */
public class SortedVehicleModels implements Comparator<VehicleModel> {
    
    public int compare (VehicleModel o1, VehicleModel o2 ) {
        return new String(o1.getRegistrationNumber()).compareTo(new String(o2.getRegistrationNumber()));
    }

}
