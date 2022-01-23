package de.davelee.trams.api.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests the PurchaseVehicleResponse class and ensures that its works correctly.
 * @author Dave Lee
 */
public class PurchaseVehicleResponseTest {

    /**
     * Ensure that a PurchaseVehicleResponse class can be correctly instantiated.
     */
    @Test
    public void testCreateResponse() {
        PurchaseVehicleResponse purchaseVehicleResponse = new PurchaseVehicleResponse();
        purchaseVehicleResponse.setPurchased(true);
        purchaseVehicleResponse.setPurchasePrice(200000);
        assertTrue(purchaseVehicleResponse.isPurchased());
        assertEquals(200000, purchaseVehicleResponse.getPurchasePrice());
    }

}
