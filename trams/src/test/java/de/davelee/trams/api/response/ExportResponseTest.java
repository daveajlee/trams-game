package de.davelee.trams.api.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This class tests the ExportResponse class and ensures that its works correctly.
 * @author Dave Lee
 */
public class ExportResponseTest {

    /**
     * Ensure that a ExportCompanyResponse class can be correctly instantiated.
     */
    @Test
    public void testCreateResponse() {
        ExportResponse exportResponse = new ExportResponse();
        exportResponse.setRouteResponses(new RouteResponse[0]);
        exportResponse.setVehicleResponses(new VehicleResponse[0]);
        assertNotNull(exportResponse.getRouteResponses());
        assertNotNull(exportResponse.getVehicleResponses());
    }
}
