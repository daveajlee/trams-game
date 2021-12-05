package de.davelee.trams.api.response;

import lombok.*;

/**
 * This class represents a response from the server containing details
 * of all matched stops according to specified criteria. As well as containing details about the stops in form of
 * an array of <code>StopResponse</code> objects, the object also contains a simple count of the stops.
 * @author Dave Lee
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StopsResponse {

    //a count of the number of stops which were found by the server.
    private Long count;

    //an array of all stops found by the server.
    private StopResponse[] stopResponses;

}
