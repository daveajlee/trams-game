package de.davelee.trams.util;

import de.davelee.trams.api.response.RouteResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the SortedRouteResponses class and ensures that its works correctly.
 * @author Dave Lee
 */
public class SortedRouteResponsesTest {

    @Test
    public void sortRouteResponses() {
        SortedRouteResponses sortedRouteResponses = new SortedRouteResponses();
        assertEquals(-1, sortedRouteResponses.compare(RouteResponse.builder().routeNumber("1A").build(),
                RouteResponse.builder().routeNumber("2A").build()));
        assertEquals(0, sortedRouteResponses.compare(RouteResponse.builder().routeNumber("1A").build(),
                RouteResponse.builder().routeNumber("1A").build()));
        assertEquals(1, sortedRouteResponses.compare(RouteResponse.builder().routeNumber("2A").build(),
                RouteResponse.builder().routeNumber("1A").build()));
    }

}
