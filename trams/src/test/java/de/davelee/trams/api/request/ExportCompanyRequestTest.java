package de.davelee.trams.api.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the ExportCompanyRequest class and ensures that its works correctly.
 * @author Dave Lee
 */
public class ExportCompanyRequestTest {

    /**
     * Ensure that a ExportCompanyRequest class can be correctly instantiated.
     */
    @Test
    public void testCreateRequest() {
        ExportCompanyRequest exportCompanyRequest = new ExportCompanyRequest();
        exportCompanyRequest.setCompany("Lee Buses");
        exportCompanyRequest.setPlayerName("Dave Lee");
        exportCompanyRequest.setDrivers("{}");
        exportCompanyRequest.setMessages("{}");
        exportCompanyRequest.setVehicles("{}");
        exportCompanyRequest.setRoutes("{}");
        assertEquals("ExportCompanyRequest(company=Lee Buses, playerName=Dave Lee, routes={}, drivers={}, vehicles={}, messages={})", exportCompanyRequest.toString());
    }

}
