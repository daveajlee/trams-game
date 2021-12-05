package de.davelee.trams.api.response;

import lombok.*;

/**
 * This class represents a response from the server containing details
 * of all matched vehicles according to specified criteria. As well as containing details about the vehicles in form of
 * an array of <code>VehicleResponse</code> objects, the object also contains a simple count of the vehicles.
 * @author Dave Lee
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VehiclesResponse {

    //a count of the number of vehicles which were found by the server.
    private Long count;

    //an array of all vehicles found by the server.
    private VehicleResponse[] vehicleResponses;

}
