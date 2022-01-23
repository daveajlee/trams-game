package de.davelee.trams.api.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the CompanyResponse class and ensures that its works correctly.
 * @author Dave Lee
 */
public class CompanyResponseTest {

    /**
     * Ensure that a CompanyResponse class can be correctly instantiated.
     */
    @Test
    public void testCreateResponse() {
        CompanyResponse companyResponse = new CompanyResponse();
        companyResponse.setName("Mustermann GmbH");
        companyResponse.setPlayerName("Max Mustermann");
        companyResponse.setBalance(10000.0);
        companyResponse.setSatisfactionRate(100.0);
        companyResponse.setTime("01-12-2020 16:54");
        companyResponse.setScenarioName("Beginner's Scenario");
        companyResponse.setDifficultyLevel("EASY");
        assertEquals(10000.0, companyResponse.getBalance());
        assertEquals("Mustermann GmbH", companyResponse.getName());
        assertEquals("Max Mustermann", companyResponse.getPlayerName());
        assertEquals(100.0, companyResponse.getSatisfactionRate());
        assertEquals("01-12-2020 16:54", companyResponse.getTime());
        assertEquals("Beginner's Scenario", companyResponse.getScenarioName());
        assertEquals("EASY", companyResponse.getDifficultyLevel());
        assertEquals("CompanyResponse(name=Mustermann GmbH, balance=10000.0, playerName=Max Mustermann, time=01-12-2020 16:54, satisfactionRate=100.0, scenarioName=Beginner's Scenario, difficultyLevel=EASY)", companyResponse.toString());
    }

}
