package de.davelee.trams.api.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the AdjustBalanceRequest class and ensures that its works correctly.
 * @author Dave Lee
 */
public class AdjustBalanceRequestTest {

    /**
     * Ensure that a AdjustBalanceRequest class can be correctly instantiated.
     */
    @Test
    public void testCreateRequest() {
        AdjustBalanceRequest adjustBalanceRequest = new AdjustBalanceRequest();
        adjustBalanceRequest.setCompany("Lee Buses");
        adjustBalanceRequest.setValue(-40.00);
        assertEquals("Lee Buses", adjustBalanceRequest.getCompany());
        assertEquals(-40.00, adjustBalanceRequest.getValue());
        assertEquals("AdjustBalanceRequest(company=Lee Buses, value=-40.0)", adjustBalanceRequest.toString());
    }

}
