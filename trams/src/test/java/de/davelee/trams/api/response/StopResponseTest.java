package de.davelee.trams.api.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the StopResponse class and ensures that its works correctly.
 * @author Dave Lee
 */
public class StopResponseTest {

    /**
     * Ensure that a StopResponse class can be correctly instantiated.
     */
    @Test
    public void testCreateResponse() {
        StopResponse stopResponse = StopResponse.builder()
                .company("Mustermann Bus GmbH")
                .name("Greenfield")
                .latitude(50.03)
                .longitude(123.04)
                .build();
        assertEquals("Greenfield", stopResponse.getName());
        assertEquals("Mustermann Bus GmbH", stopResponse.getCompany());
        assertEquals(50.03, stopResponse.getLatitude());
        assertEquals(123.04, stopResponse.getLongitude());
        stopResponse.setCompany("Mustermann Buses GmbH");
        stopResponse.setLatitude(50.04);
        stopResponse.setLongitude(122.04);
        stopResponse.setName("Greenerfield");
        assertEquals("Greenerfield", stopResponse.getName());
        assertEquals("Mustermann Buses GmbH", stopResponse.getCompany());
        assertEquals(50.04, stopResponse.getLatitude());
        assertEquals(122.04, stopResponse.getLongitude());
        assertEquals("StopResponse(name=Greenerfield, company=Mustermann Buses GmbH, latitude=50.04, longitude=122.04)", stopResponse.toString());
    }

}
