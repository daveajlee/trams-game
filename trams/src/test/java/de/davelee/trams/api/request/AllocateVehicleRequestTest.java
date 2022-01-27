package de.davelee.trams.api.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the AllocateVehicleRequest class and ensures that its works correctly.
 * @author Dave Lee
 */
public class AllocateVehicleRequestTest {

    /**
     * Ensure that a AllocateVehicleRequest class can be correctly instantiated.
     */
    @Test
    public void testCreateRequest() {
        AllocateVehicleRequest allocateVehicleRequest = new AllocateVehicleRequest();
        allocateVehicleRequest.setAllocatedTour("1/1");
        allocateVehicleRequest.setCompany("Lee Buses");
        allocateVehicleRequest.setFleetNumber("101");
        assertEquals("1/1", allocateVehicleRequest.getAllocatedTour());
        assertEquals("Lee Buses", allocateVehicleRequest.getCompany());
        assertEquals("101", allocateVehicleRequest.getFleetNumber());
    }

}
