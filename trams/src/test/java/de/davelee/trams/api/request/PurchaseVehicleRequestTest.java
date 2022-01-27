package de.davelee.trams.api.request;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This class tests the PurchaseVehicleRequest class and ensures that its works correctly.
 * @author Dave Lee
 */
public class PurchaseVehicleRequestTest {

    /**
     * Ensure that a PurchaseVehicleRequest class can be correctly instantiated.
     */
    @Test
    public void testCreateRequest() {
        PurchaseVehicleRequest purchaseVehicleRequest = new PurchaseVehicleRequest();
        purchaseVehicleRequest.setVehicleType("BUS");
        purchaseVehicleRequest.setCompany("Lee Buses");
        purchaseVehicleRequest.setLivery("Green with red text");
        purchaseVehicleRequest.setFleetNumber("101");
        purchaseVehicleRequest.setModelName("BendyBus 2000");
        purchaseVehicleRequest.setAdditionalTypeInformationMap(Map.of("Registration Number", "2021-TEST"));
        purchaseVehicleRequest.setSeatingCapacity(50);
        purchaseVehicleRequest.setStandingCapacity(80);
        assertEquals("BUS", purchaseVehicleRequest.getVehicleType());
        assertEquals("Lee Buses", purchaseVehicleRequest.getCompany());
        assertEquals("Green with red text", purchaseVehicleRequest.getLivery());
        assertEquals("101", purchaseVehicleRequest.getFleetNumber());
        assertEquals("BendyBus 2000", purchaseVehicleRequest.getModelName());
        assertEquals(50, purchaseVehicleRequest.getSeatingCapacity());
        assertEquals(80, purchaseVehicleRequest.getStandingCapacity());
        assertNotNull(purchaseVehicleRequest.getAdditionalTypeInformationMap());
    }

}
