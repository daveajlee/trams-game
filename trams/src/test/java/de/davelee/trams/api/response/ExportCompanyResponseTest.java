package de.davelee.trams.api.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the ExportCompanyResponse class and ensures that its works correctly.
 * @author Dave Lee
 */
public class ExportCompanyResponseTest {

    /**
     * Ensure that a ExportCompanyResponse class can be correctly instantiated.
     */
    @Test
    public void testCreateResponse() {
        ExportCompanyResponse exportCompanyResponse = new ExportCompanyResponse();
        exportCompanyResponse.setBalance(80000.0);
        exportCompanyResponse.setDifficultyLevel("EASY");
        exportCompanyResponse.setDrivers("{}");
        exportCompanyResponse.setMessages("{}");
        exportCompanyResponse.setRoutes("{}");
        exportCompanyResponse.setName("Mustermann GmbH");
        exportCompanyResponse.setPlayerName("Dave Lee");
        exportCompanyResponse.setTime("01-01-2020 04:44");
        exportCompanyResponse.setSatisfactionRate(100.0);
        exportCompanyResponse.setVehicles("{}");
        exportCompanyResponse.setScenarioName("Landuff");
        assertEquals("ExportCompanyResponse(name=Mustermann GmbH, balance=80000.0, playerName=Dave Lee, satisfactionRate=100.0, time=01-01-2020 04:44, scenarioName=Landuff, difficultyLevel=EASY, routes={}, drivers={}, vehicles={}, messages={})", exportCompanyResponse.toString());
    }

}
