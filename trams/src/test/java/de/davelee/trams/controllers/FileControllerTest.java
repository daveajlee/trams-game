package de.davelee.trams.controllers;

import de.davelee.trams.TramsGameApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes= TramsGameApplication.class)
public class FileControllerTest {

    @Autowired
    private FileController fileController;

    @Autowired
    private GameController gameController;

    @Autowired
    private ScenarioController scenarioController;

    @Test
    public void testSaveFile() {
        assertNotNull(scenarioController);
        gameController.createGameModel("Dave Lee", "Landuff Transport Company", "Mustermann GmbH");
        assertEquals("Dave Lee", gameController.getCurrentPlayerName());
        fileController.saveFile(new File("test-game.json"));
        gameController.createGameModel("Bob Smith", "Landuff Transport Company", "Mustermann GmbH");
        assertEquals("Bob Smith", gameController.getCurrentPlayerName());
        fileController.loadFile(new File("test-game.json"));
        assertEquals("Dave Lee", gameController.getCurrentPlayerName());
    }

    private void assertEquals ( final String expected, final String actual ) {
        Assertions.assertEquals(expected, actual);
    }

}
