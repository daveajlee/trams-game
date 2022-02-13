package de.davelee.trams.api.request;

import lombok.*;

import java.util.Map;

/**
 * This class represents a request to add a stop.
 * @author Dave Lee
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class AddStopRequest {

    /**
     * The name of the stop.
     */
    private String name;

    /**
     * The name of the company serving this stop.
     */
    private String company;

    /**
     * The waiting time at the stop for a vehicle.
     */
    private int waitingTime;

    /**
     * The distances between this stop and other stops as key/value pair with stop name and distance in minutes.
     */
    private Map<String, Integer> distances;

    /**
     * The latitude location of the stop which should be in a valid format for a latitude e.g. 50.0200004
     */
    private double latitude;

    /**
     * The longitude location of the stop which should be in a valid format for a longitude e.g. 50.0200004
     */
    private double longitude;

}
