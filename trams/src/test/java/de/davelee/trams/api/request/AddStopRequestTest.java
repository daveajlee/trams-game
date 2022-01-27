package de.davelee.trams.api.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the AddStopRequest class and ensures that its works correctly.
 * @author Dave Lee
 */
public class AddStopRequestTest {

    /**
     * Ensure that a AddStopRequest class can be correctly instantiated.
     */
    @Test
    public void testCreateRequest() {
        AddStopRequest addStopRequest = new AddStopRequest();
        addStopRequest.setName("City Centre");
        addStopRequest.setLatitude(1.23);
        addStopRequest.setLongitude(1.25);
        addStopRequest.setCompany("Lee Buses");
        assertEquals("City Centre", addStopRequest.getName());
        assertEquals("Lee Buses", addStopRequest.getCompany());
        assertEquals(1.23, addStopRequest.getLatitude());
        assertEquals(1.25, addStopRequest.getLongitude());
    }

}
