package de.davelee.trams.api.request;

import lombok.*;

/**
 * This class represents a request to allocate a vehicle to a particular tour / timetable.
 * @author Dave Lee
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class AllocateVehicleRequest {

    /**
     * The fleet number of this vehicle.
     */
    private String fleetNumber;

    /**
     * The company that owns this vehicle.
     */
    private String company;

    /**
     * The allocated tour for this vehicle.
     */
    private String allocatedTour;

}

