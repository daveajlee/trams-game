package de.davelee.trams.api.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the AdjustSatisfactionRequest class and ensures that its works correctly.
 * @author Dave Lee
 */
public class AdjustSatisfactionRequestTest {

    /**
     * Ensure that a AdjustSatisfactionRequest class can be correctly instantiated.
     */
    @Test
    public void testCreateRequest() {
        AdjustSatisfactionRequest adjustSatisfactionRequest = new AdjustSatisfactionRequest();
        adjustSatisfactionRequest.setSatisfactionRate(100.0);
        adjustSatisfactionRequest.setCompany("Lee Buses");
        assertEquals("Lee Buses", adjustSatisfactionRequest.getCompany());
        assertEquals(100.0, adjustSatisfactionRequest.getSatisfactionRate());
        assertEquals("AdjustSatisfactionRequest(company=Lee Buses, satisfactionRate=100.0)", adjustSatisfactionRequest.toString());
    }

}
