package de.davelee.trams.api.response;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the StopTimeResponse class and ensures that its works correctly.
 * @author Dave Lee
 */
public class StopTimeResponseTest {

    /**
     * Ensure that a StopTimeResponse class can be correctly instantiated.
     */
    @Test
    public void testCreateResponse() {
        StopTimeResponse stopTimeResponse = StopTimeResponse.builder()
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
                .build();
        assertEquals("Greenfield", stopTimeResponse.getStopName());
        assertEquals("Mustermann Bus GmbH", stopTimeResponse.getCompany());
        assertEquals("101", stopTimeResponse.getRouteNumber());
        assertEquals("102", stopTimeResponse.getJourneyNumber());
        assertEquals("Lakeside", stopTimeResponse.getDestination());
        assertEquals("22:10", stopTimeResponse.getArrivalTime());
        assertEquals("22:11", stopTimeResponse.getDepartureTime());
        assertEquals(2, stopTimeResponse.getOperatingDays().size());
        assertEquals("23-04-2021", stopTimeResponse.getValidFromDate());
        assertEquals("23-10-2021", stopTimeResponse.getValidToDate());
        stopTimeResponse.setCompany("Mustermann Buses GmbH");
        stopTimeResponse.setStopName("Greenerfield");
        stopTimeResponse.setRouteNumber("11");
        stopTimeResponse.setJourneyNumber("101");
        stopTimeResponse.setDestination("Lakeside Central");
        stopTimeResponse.setArrivalTime("22:11");
        stopTimeResponse.setDepartureTime("22:12");
        stopTimeResponse.setOperatingDays(List.of("Friday","Saturday","Sunday"));
        stopTimeResponse.setValidFromDate("23-04-2020");
        stopTimeResponse.setValidToDate("23-10-2020");
        assertEquals("Greenerfield", stopTimeResponse.getStopName());
        assertEquals("Mustermann Buses GmbH", stopTimeResponse.getCompany());
        assertEquals("11", stopTimeResponse.getRouteNumber());
        assertEquals("101", stopTimeResponse.getJourneyNumber());
        assertEquals("Lakeside Central", stopTimeResponse.getDestination());
        assertEquals("22:11", stopTimeResponse.getArrivalTime());
        assertEquals("22:12", stopTimeResponse.getDepartureTime());
        assertEquals(3, stopTimeResponse.getOperatingDays().size());
        assertEquals("23-04-2020", stopTimeResponse.getValidFromDate());
        assertEquals("23-10-2020", stopTimeResponse.getValidToDate());
        assertEquals("StopTimeResponse(stopName=Greenerfield, company=Mustermann Buses GmbH, arrivalTime=22:11, departureTime=22:12, destination=Lakeside Central, routeNumber=11, validFromDate=23-04-2020, validToDate=23-10-2020, operatingDays=[Friday, Saturday, Sunday], journeyNumber=101)", stopTimeResponse.toString());
    }

}
