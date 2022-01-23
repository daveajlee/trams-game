package de.davelee.trams.api.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the TimeResponse class and ensures that its works correctly.
 * @author Dave Lee
 */
public class TimeResponseTest {

    /**
     * Ensure that a TimeResponse class can be correctly instantiated.
     */
    @Test
    public void testCreateResponse() {
        TimeResponse timeResponse = new TimeResponse();
        timeResponse.setTime("03-12-2020 08:20");
        timeResponse.setCompany("Lee Transport");
        assertEquals("03-12-2020 08:20", timeResponse.getTime());
        assertEquals("Lee Transport", timeResponse.getCompany());
        assertEquals("TimeResponse(company=Lee Transport, time=03-12-2020 08:20)", timeResponse.toString());
    }

}
