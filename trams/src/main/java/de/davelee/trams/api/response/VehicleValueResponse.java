package de.davelee.trams.api.response;

import lombok.*;

/**
 * This class represents a response to a request to calculate the current value of a vehicle and contains the
 * current value of the vehicle as well as the company and fleet number.
 * @author Dave Lee
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class VehicleValueResponse {

    //company that owns the vehicle
    private String company;

    //fleet number of the vehicle
    private String fleetNumber;

    //value of the vehicle
    private double value;

}
