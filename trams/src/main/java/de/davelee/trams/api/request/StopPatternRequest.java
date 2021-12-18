package de.davelee.trams.api.request;

import lombok.*;

/**
 * This class is part of a <code>GenerateStopTimesRequest</code>.
 * It contains the list of stops in the order they should be served as well as the number of minutes that a service should stay there
 * and how far each stop is away from the next stop.
 * @author Dave Lee
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
public class StopPatternRequest {

    /**
     * Array of stops that should be served in the order that they should be served.
     */
    private String[] stopNames;

    /**
     * Array of distances between stops e.g. the position 0 indicates the distance between stop name in position 0 and
     * stop name in position 1 - therefore this array always has a size one less than the array of stop names.
     */
    private int[] distances;

    /**
     * Array of times in minutes where the vehicle should stay here (a value of 0 indicates arrival time and departure time
     * can be identical, a minus number is not allowed) - this array must have the same size as the array of stop names.
     */
    private int[] stoppingTimes;

}
