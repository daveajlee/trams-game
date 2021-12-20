package de.davelee.trams.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@ToString
public class VehicleResponse {

    /**
     * The fleet number of this vehicle.
     */
    private String fleetNumber;

    /**
     * The company that owns this vehicle.
     */
    private String company;

    /**
     * The date that the vehicle was delivered to its current company in the format dd-MM-yyyy.
     */
    private String deliveryDate;

    /**
     * The date that the vehicle last went through an inspection in the format dd-MM-yyyy.
     */
    private String inspectionDate;

    /**
     * The type of this vehicle which is mapped from subclasses as appropriate.
     */
    private String vehicleType;

    /**
     * The current status of the vehicle which is mapped from the Enum.
     */
    private String vehicleStatus;

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

    /**
     * The livery that this vehicle has.
     */
    private String livery;

    /**
     * The allocated tour for this vehicle.
     */
    private String allocatedTour;

    /**
     * The current delay of this vehicle in minutes.
     */
    private int delayInMinutes;

    /**
     * The current status of inspection for this vehicle.
     */
    private String inspectionStatus;

    /**
     * The number of days until the next inspection is due.
     */
    private long nextInspectionDueInDays;

    /**
     * The additional parameters relevant to this vehicle type e.g. registration number for buses are stored as key/value pairs.
     */
    private Map<String, String> additionalTypeInformationMap;

    /**
     * The list of entries in the log history of this vehicle.
     */
    private List<VehicleHistoryResponse> userHistory;

    /**
     * The number of hours that a vehicle was in service on a particular day.
     */
    private Map<String, Integer> timesheet;

}
