package de.davelee.trams.api.response;

import lombok.*;

/**
 * This class represents a response from the server containing details
 * of all matched routes according to specified criteria. As well as containing details about the routes in form of
 * an array of <code>RouteResponse</code> objects, the object also contains a simple count of the routes.
 * @author Dave Lee
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoutesResponse {

    //a count of the number of routes which were found by the server.
    private Long count;

    //an array of all routes found by the server.
    private RouteResponse[] routeResponses;

}