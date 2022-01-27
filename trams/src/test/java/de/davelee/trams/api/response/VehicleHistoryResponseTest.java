package de.davelee.trams.api.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the VehicleHistoryResponse class and ensures that its works correctly.
 * @author Dave Lee
 */
public class VehicleHistoryResponseTest {

    /**
     * Ensure that a VehicleHistoryResponse class can be correctly instantiated.
     */
    @Test
    public void testCreateResponse() {
        VehicleHistoryResponse vehicleHistoryResponse = new VehicleHistoryResponse();
        vehicleHistoryResponse.setVehicleHistoryReason("SOLD");
        vehicleHistoryResponse.setComment("Vehicle Sold");
        vehicleHistoryResponse.setDate("27-01-2020");
        assertEquals("SOLD", vehicleHistoryResponse.getVehicleHistoryReason());
        assertEquals("Vehicle Sold", vehicleHistoryResponse.getComment());
        assertEquals("27-01-2020", vehicleHistoryResponse.getDate());
    }

}
