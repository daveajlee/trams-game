package de.davelee.trams.api.response;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the VehicleResponse class and ensures that its works correctly.
 * @author Dave Lee
 */
public class VehicleResponseTest {

    /**
     * Ensure that a VehicleResponse class can be correctly instantiated.
     */
    @Test
    public void testCreateResponse() {
        VehicleResponse vehicleResponse = VehicleResponse.builder()
                .livery("Green with red text")
                .fleetNumber("213")
                .allocatedTour("1/1")
                .vehicleType("Bus")
                .additionalTypeInformationMap(Collections.singletonMap("Registration Number", "XXX2 BBB"))
                .inspectionStatus("Inspected")
                .nextInspectionDueInDays(100)
                .company("Lee Buses")
                .build();
        assertEquals("Green with red text", vehicleResponse.getLivery());
        assertEquals("213", vehicleResponse.getFleetNumber());
        assertEquals("1/1", vehicleResponse.getAllocatedTour());
        assertEquals("Bus", vehicleResponse.getVehicleType());
        assertEquals("XXX2 BBB", vehicleResponse.getAdditionalTypeInformationMap().get("Registration Number"));
        assertEquals("Inspected", vehicleResponse.getInspectionStatus());
        assertEquals(100, vehicleResponse.getNextInspectionDueInDays());
        vehicleResponse.setLivery("Blue with orange text");
        vehicleResponse.setFleetNumber("1213");
        vehicleResponse.setAllocatedTour("1/2");
        vehicleResponse.setVehicleType("Tram");
        vehicleResponse.setAdditionalTypeInformationMap(Collections.singletonMap("Bidirectional", "true"));
        vehicleResponse.setInspectionStatus("Inspection Due!");
        vehicleResponse.setNextInspectionDueInDays(0);
        vehicleResponse.setDeliveryDate("25-04-2021");
        vehicleResponse.setInspectionDate("25-05-2021");
        vehicleResponse.setVehicleStatus("DELIVERED");
        vehicleResponse.setModelName("Bendy Bus 2000");
        vehicleResponse.setPurchasePrice(700000.0);
        vehicleResponse.setUserHistory(List.of(VehicleHistoryResponse.builder().vehicleHistoryReason("PURCHASED").date("25-04-2021").comment("Love on first sight").build()));
        vehicleResponse.setTimesheet(Map.of("01-11-2021", 8));
        assertEquals("VehicleResponse(fleetNumber=1213, company=Lee Buses, deliveryDate=25-04-2021, inspectionDate=25-05-2021, vehicleType=Tram, purchasePrice=700000.0, vehicleStatus=DELIVERED, seatingCapacity=0, standingCapacity=0, modelName=Bendy Bus 2000, livery=Blue with orange text, allocatedTour=1/2, delayInMinutes=0, inspectionStatus=Inspection Due!, nextInspectionDueInDays=0, additionalTypeInformationMap={Bidirectional=true}, userHistory=[VehicleHistoryResponse(date=25-04-2021, vehicleHistoryReason=PURCHASED, comment=Love on first sight)], timesheet={01-11-2021=8})", vehicleResponse.toString());
    }

}
