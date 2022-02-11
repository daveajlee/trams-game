package de.davelee.trams.api.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the VehicleValueResponse class and ensures that its works correctly.
 * @author Dave Lee
 */
public class VehicleValueResponseTest {

    /**
     * Ensure that a VehicleValueResponse class can be correctly instantiated.
     */
    @Test
    public void testCreateResponse() {
        VehicleValueResponse vehicleValueResponse = new VehicleValueResponse();
        vehicleValueResponse.setValue(665000.0);
        vehicleValueResponse.setCompany("Lee Transport");
        vehicleValueResponse.setFleetNumber("224");
        assertEquals(665000.0, vehicleValueResponse.getValue());
        assertEquals("Lee Transport", vehicleValueResponse.getCompany());
        assertEquals("224", vehicleValueResponse.getFleetNumber());
    }

}
