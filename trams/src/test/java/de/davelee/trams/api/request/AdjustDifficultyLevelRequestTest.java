package de.davelee.trams.api.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the AdjustDifficultyLevelRequest class and ensures that its works correctly.
 * @author Dave Lee
 */
public class AdjustDifficultyLevelRequestTest {

    /**
     * Ensure that a AdjustBalanceRequest class can be correctly instantiated.
     */
    @Test
    public void testCreateRequest() {
        AdjustDifficultyLevelRequest adjustDifficultyLevelRequest = new AdjustDifficultyLevelRequest();
        adjustDifficultyLevelRequest.setDifficultyLevel("EASY");
        adjustDifficultyLevelRequest.setCompany("Lee Buses");
        assertEquals("EASY", adjustDifficultyLevelRequest.getDifficultyLevel());
        assertEquals("Lee Buses", adjustDifficultyLevelRequest.getCompany());
        assertEquals("AdjustDifficultyLevelRequest(company=Lee Buses, difficultyLevel=EASY)", adjustDifficultyLevelRequest.toString());
    }

}
