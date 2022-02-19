package de.davelee.trams.api.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This class tests the GenerateStopTimesRequest class and ensures that its works correctly.
 * @author Dave Lee
 */
public class GenerateStopTimesRequestTest {

    /**
     * Ensure that a GenerateStopTimesRequest class can be correctly instantiated.
     */
    @Test
    public void testCreateRequest() {
        GenerateStopTimesRequest generateStopTimesRequest = new GenerateStopTimesRequest();
        generateStopTimesRequest.setCompany("Lee Company");
        generateStopTimesRequest.setStopNames(new String[] { "City Centre" });
        generateStopTimesRequest.setRouteNumber("1A");
        generateStopTimesRequest.setStartTime("06:00");
        generateStopTimesRequest.setEndTime("23:00");
        generateStopTimesRequest.setFrequency(15);
        generateStopTimesRequest.setValidFromDate("12-12-2020");
        generateStopTimesRequest.setValidToDate("11-12-2021");
        generateStopTimesRequest.setOperatingDays("Monday,Tuesday,Wednesday");
        assertEquals("Lee Company", generateStopTimesRequest.getCompany());
        assertNotNull(generateStopTimesRequest.getStopNames());
        assertEquals("1A", generateStopTimesRequest.getRouteNumber());
        assertEquals("06:00", generateStopTimesRequest.getStartTime());
        assertEquals("23:00", generateStopTimesRequest.getEndTime());
        assertEquals(15, generateStopTimesRequest.getFrequency());
        assertEquals("12-12-2020", generateStopTimesRequest.getValidFromDate());
        assertEquals("11-12-2021", generateStopTimesRequest.getValidToDate());
        assertEquals("Monday,Tuesday,Wednesday", generateStopTimesRequest.getOperatingDays());
        assertEquals("GenerateStopTimesRequest(company=Lee Company, stopNames=[City Centre], routeNumber=1A, startTime=06:00, endTime=23:00, frequency=15, validFromDate=12-12-2020, validToDate=11-12-2021, operatingDays=Monday,Tuesday,Wednesday)", generateStopTimesRequest.toString());
    }

}
