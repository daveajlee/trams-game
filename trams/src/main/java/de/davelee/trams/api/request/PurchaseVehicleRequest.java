package de.davelee.trams.api.request;

import lombok.*;

import java.util.Map;

/**
 * This class represents a request to purchase a vehicle for the particular
 * company fulfilling the details supplied.
 * @author Dave Lee
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class PurchaseVehicleRequest {

    /**
     * The fleet number of this vehicle.
     */
    private String fleetNumber;

    /**
     * The company that owns this vehicle.
     */
    private String company;

    /**
     * The type of this vehicle which is mapped from subclasses as appropriate.
     */
    private String vehicleType;

    /**
     * The livery that this vehicle has.
     */
    private String livery;

    /**
     * The additional parameters relevant to this vehicle type e.g. registration number for buses are stored as key/value pairs.
     */
    private Map<String, String> additionalTypeInformationMap;

    /**
     * The number of seats that this vehicle has.
     */
    private int seatingCapacity;

    /**
     * The number of persons who are allowed to stand in this vehicle.
     */
    private int standingCapacity;

    /**
     * The name of the model of this vehicle.
     */
    private String modelName;

}