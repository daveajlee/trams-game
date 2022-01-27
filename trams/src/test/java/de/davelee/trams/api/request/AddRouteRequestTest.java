package de.davelee.trams.api.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the AddRouteRequest class and ensures that its works correctly.
 * @author Dave Lee
 */
public class AddRouteRequestTest {

    /**
     * Ensure that a AddRouteRequest class can be correctly instantiated.
     */
    @Test
    public void testCreateRequest() {
        AddRouteRequest addRouteRequest = new AddRouteRequest();
        addRouteRequest.setRouteNumber("1A");
        addRouteRequest.setCompany("Lee Buses");
        assertEquals("1A", addRouteRequest.getRouteNumber());
        assertEquals("Lee Buses", addRouteRequest.getCompany());
    }

}
