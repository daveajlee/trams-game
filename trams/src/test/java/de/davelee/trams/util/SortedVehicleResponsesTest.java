package de.davelee.trams.util;

import de.davelee.trams.api.response.RouteResponse;
import de.davelee.trams.api.response.VehicleResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the SortedVehicleResponses class and ensures that its works correctly.
 * @author Dave Lee
 */
public class SortedVehicleResponsesTest {

    @Test
    public void sortVehicleResponses() {
        SortedVehicleResponses sortedVehicleResponses = new SortedVehicleResponses();
        assertEquals(-2, sortedVehicleResponses.compare(VehicleResponse.builder().fleetNumber("101").build(),
                VehicleResponse.builder().fleetNumber("103").build()));
        assertEquals(0, sortedVehicleResponses.compare(VehicleResponse.builder().fleetNumber("101").build(),
                VehicleResponse.builder().fleetNumber("101").build()));
        assertEquals(2, sortedVehicleResponses.compare(VehicleResponse.builder().fleetNumber("103").build(),
                VehicleResponse.builder().fleetNumber("101").build()));
    }

}
