package de.davelee.trams.api.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the RouteResponse class and ensures that its works correctly.
 * @author Dave Lee
 */
public class RouteResponseTest {

    /**
     * Ensure that a RouteResponse class can be correctly instantiated.
     */
    @Test
    public void testCreateResponse() {
        RouteResponse routeResponse = RouteResponse.builder()
                .company("Mustermann Bus GmbH")
                .routeNumber("405")
                .build();
        assertEquals("405", routeResponse.getRouteNumber());
        assertEquals("Mustermann Bus GmbH", routeResponse.getCompany());
        routeResponse.setCompany("Mustermann Buses GmbH");
        routeResponse.setRouteNumber("405A");
        assertEquals("405A", routeResponse.getRouteNumber());
        assertEquals("Mustermann Buses GmbH", routeResponse.getCompany());
        assertEquals("RouteResponse(routeNumber=405A, company=Mustermann Buses GmbH)", routeResponse.toString());
    }

}
