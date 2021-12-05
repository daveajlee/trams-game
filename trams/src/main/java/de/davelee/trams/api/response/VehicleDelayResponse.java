package de.davelee.trams.api.response;

import lombok.*;

/**
 * This class represents a response to a request to adjust the delay of a vehicle
 * and contains the current delay of the vehicle in minutes.
 * @author Dave Lee
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class VehicleDelayResponse {

    //company that owns the vehicle
    private String company;

    //fleet number of the vehicle
    private String fleetNumber;

    //delay of the vehicle in minutes (must be 0 or greater)
    private int delayInMinutes;

}