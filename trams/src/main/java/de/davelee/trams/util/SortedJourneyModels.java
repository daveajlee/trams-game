package de.davelee.trams.util;

import java.util.*;

import de.davelee.trams.model.JourneyModel;

/**
 * This class sorts journeys by stop times in ascending order for the TraMS program.
 * @author Dave Lee
 */
public class SortedJourneyModels implements Comparator<JourneyModel> {

    public int compare (final JourneyModel o1, final JourneyModel o2 ) {
        return ((Integer) o1.getJourneyNumber()).compareTo((Integer) o2.getJourneyNumber());
    }

}
