package de.davelee.trams.api.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the AddTimeRequest class and ensures that its works correctly.
 * @author Dave Lee
 */
public class AddTimeRequestTest {

    /**
     * Ensure that a AddTimeRequest class can be correctly instantiated.
     */
    @Test
    public void testCreateRequest() {
        AddTimeRequest addTimeRequest = new AddTimeRequest();
        addTimeRequest.setCompany("Lee Buses");
        addTimeRequest.setMinutes(15);
        assertEquals("Lee Buses", addTimeRequest.getCompany());
        assertEquals(15, addTimeRequest.getMinutes());
        assertEquals("AddTimeRequest(company=Lee Buses, minutes=15)", addTimeRequest.toString());
    }

}
