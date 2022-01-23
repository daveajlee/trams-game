package de.davelee.trams.api.response;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the VehicleResponses class and ensures that its works correctly.
 * @author Dave Lee
 */
public class VehiclesResponseTest {

    @Test
    public void testSetters() {
        VehiclesResponse vehiclesResponse = new VehiclesResponse();
        vehiclesResponse.setCount(1L);
        vehiclesResponse.setVehicleResponses(new VehicleResponse[] {
                VehicleResponse.builder()
                        .livery("Green with red text")
                        .fleetNumber("213")
                        .allocatedTour("1/1")
                        .vehicleType("Bus")
                        .additionalTypeInformationMap(Collections.singletonMap("Registration Number", "XXX2 BBB"))
                        .inspectionStatus("Inspected")
                        .nextInspectionDueInDays(100)
                        .company("Lee Buses")
                        .build()
        });
        assertEquals(1L, vehiclesResponse.getCount());
        assertEquals(1, vehiclesResponse.getVehicleResponses().length);
    }

}
