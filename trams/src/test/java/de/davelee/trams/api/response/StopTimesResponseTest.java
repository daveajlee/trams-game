package de.davelee.trams.api.response;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the StopTimesResponse class and ensures that its works correctly.
 * @author Dave Lee
 */
public class StopTimesResponseTest {

    @Test
    public void testSetters() {
        StopTimesResponse stopTimesResponse = new StopTimesResponse();
        stopTimesResponse.setCount(1L);
        stopTimesResponse.setStopTimeResponses(new StopTimeResponse[] {
                StopTimeResponse.builder()
                        .company("Mustermann Bus GmbH")
                        .stopName("Greenfield")
                        .routeNumber("101")
                        .journeyNumber("102")
                        .destination("Lakeside")
                        .arrivalTime("22:10")
                        .departureTime("22:11")
                        .operatingDays(List.of("Friday","Saturday"))
                        .validFromDate("23-04-2021")
                        .validToDate("23-10-2021")
                        .build()
        });
        assertEquals(1L, stopTimesResponse.getCount());
        assertEquals("Mustermann Bus GmbH", stopTimesResponse.getStopTimeResponses()[0].getCompany());
    }

}
