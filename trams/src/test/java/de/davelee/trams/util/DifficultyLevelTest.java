package de.davelee.trams.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This class tests the DifficultyLevel class and ensures that its works correctly.
 * @author Dave Lee
 */
public class DifficultyLevelTest {

    @Test
    public void testDifficultyLevel() {
        //Test easy level.
        assertEquals("Easy", DifficultyLevel.EASY.getName());
        assertNotNull(DifficultyLevel.EASY.getDescription());
        //Test intermediate level.
        assertEquals("Intermediate", DifficultyLevel.INTERMEDIATE.getName());
        assertNotNull(DifficultyLevel.INTERMEDIATE.getDescription());
        //Test medium level.
        assertEquals("Medium", DifficultyLevel.MEDIUM.getName());
        assertNotNull(DifficultyLevel.MEDIUM.getDescription());
        //Test hard level.
        assertEquals("Hard", DifficultyLevel.HARD.getName());
        assertNotNull(DifficultyLevel.HARD.getDescription());
    }

}
