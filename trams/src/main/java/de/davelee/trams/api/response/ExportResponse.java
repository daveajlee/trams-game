package de.davelee.trams.api.response;

import lombok.*;

/**
 * This class represents a response to a request to export all routes and vehicles.
 * @author Dave Lee
 */
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class ExportResponse {

    private RouteResponse[] routeResponses;

    private VehicleResponse[] vehicleResponses;

}
