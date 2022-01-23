package de.davelee.trams.api.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the DifficultyLevelResponse class and ensures that its works correctly.
 * @author Dave Lee
 */
public class DifficultyLevelResponseTest {

    /**
     * Ensure that a DifficultyLevelResponse class can be correctly instantiated.
     */
    @Test
    public void testCreateResponse() {
       DifficultyLevelResponse difficultyLevelResponse = new DifficultyLevelResponse();
       difficultyLevelResponse.setDifficultyLevel("HARD");
       difficultyLevelResponse.setCompany("Lee Transport");
       assertEquals("HARD", difficultyLevelResponse.getDifficultyLevel());
       assertEquals("Lee Transport", difficultyLevelResponse.getCompany());
       assertEquals("DifficultyLevelResponse(company=Lee Transport, difficultyLevel=HARD)", difficultyLevelResponse.toString());
    }


}
