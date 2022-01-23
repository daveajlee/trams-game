package de.davelee.trams.api.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the SatisfactionRateResponse class and ensures that its works correctly.
 * @author Dave Lee
 */
public class SatisfactionRateResponseTest {

    /**
     * Ensure that a SatisfactionRateResponse class can be correctly instantiated.
     */
    @Test
    public void testCreateResponse() {
        SatisfactionRateResponse satisfactionRateResponse = new SatisfactionRateResponse();
        satisfactionRateResponse.setSatisfactionRate(15.0);
        satisfactionRateResponse.setCompany("Lee Transport");
        assertEquals(15.0, satisfactionRateResponse.getSatisfactionRate());
        assertEquals("Lee Transport", satisfactionRateResponse.getCompany());
        assertEquals("SatisfactionRateResponse(company=Lee Transport, satisfactionRate=15.0)", satisfactionRateResponse.toString());
    }

}
