package de.davelee.trams.util;

import java.util.*;

import de.davelee.trams.model.JourneyModel;

/**
 * This class sorts journeys by stop times in ascending order for the TraMS program.
 * @author Dave Lee
 */
public class SortedJourneyModels implements Comparator<JourneyModel> {
    
    public int compare (JourneyModel o1, JourneyModel o2 ) {
    	return o1.getJourneyName().compareTo(o2.getJourneyName());
    }

}
