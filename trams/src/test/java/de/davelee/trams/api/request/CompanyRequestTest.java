package de.davelee.trams.api.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the CompanyRequest class and ensures that its works correctly.
 * @author Dave Lee
 */
public class CompanyRequestTest {

    /**
     * Ensure that a CompanyRequest class can be correctly instantiated.
     */
    @Test
    public void testCreateRequest() {
        CompanyRequest companyRequest = new CompanyRequest();
        companyRequest.setName("Lee Buses");
        companyRequest.setDifficultyLevel("EASY");
        companyRequest.setPlayerName("Dave Lee");
        companyRequest.setScenarioName("Landuff");
        companyRequest.setStartingBalance(100000.0);
        companyRequest.setStartingTime("27-01-2020 21:00");
        assertEquals("Lee Buses", companyRequest.getName());
        assertEquals("EASY", companyRequest.getDifficultyLevel());
        assertEquals("Dave Lee", companyRequest.getPlayerName());
        assertEquals("Landuff", companyRequest.getScenarioName());
        assertEquals(100000.0, companyRequest.getStartingBalance());
        assertEquals("27-01-2020 21:00", companyRequest.getStartingTime());
        assertEquals("CompanyRequest(name=Lee Buses, startingBalance=100000.0, playerName=Dave Lee, startingTime=27-01-2020 21:00, scenarioName=Landuff, difficultyLevel=EASY)", companyRequest.toString());
    }

}
